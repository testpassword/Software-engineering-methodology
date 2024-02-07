package api;

import finist.back.BackApplication;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.model.enums.EducationType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = {BackApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APISimpleUserLifecycle {

    private static Long userId;
    private static final String userEmail = "newUser@mail.com";
    private static final String password = "12345678";

    private static FullUserDTO currentUserState = new FullUserDTO();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    static Stream<Arguments> getUserDTOCombinationsForUpdate() {
        // Создаем разные комбинации полей FullUserDTO для обновления
        return Stream.of(
                arguments(currentUserState.withName("John")),
                arguments(currentUserState.withPhone("1234567890")),
                arguments(currentUserState.withDateOfBirth(LocalDate.of(1990, 5, 15))),
                arguments(currentUserState.withName("Alice").withPhone("9876543210")),
                arguments(currentUserState.withName("Bob").withDateOfBirth(LocalDate.of(1985, 3, 20))),
                arguments(currentUserState.withPhone("5555555555").withDateOfBirth(LocalDate.of(1970, 10, 10))),
                arguments(currentUserState.withName("Charlie").withPhone("1231231234").withCity("New York")),
                arguments(currentUserState.withName("Emma").withDateOfBirth(LocalDate.of(1995, 8, 25)).withCity("Los Angeles")),
                arguments(currentUserState.withPhone("9998887777").withCity("Chicago").withEducation(EducationType.HIGHER))
        );
    }

    @Test
    @Order(1)
    public void testRegisterUser() {
        // Создаем объект запроса с данными пользователя
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setEmail(userEmail);
        requestDTO.setPassword(password);

        // Отправляем POST запрос на /users/
        ResponseEntity<UserRegistrationResponseDTO> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/",
                requestDTO,
                UserRegistrationResponseDTO.class);

        // Проверяем, что статус ответа - OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Получаем userId из ответа
        userId = responseEntity.getBody().getId();
        currentUserState.setId(userId);
        currentUserState.setEmail(userEmail);
    }

    @Test
    @Order(2)
    public void testSessionEndpoint() {
        // Создаем объект запроса с данными пользователя
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setEmail(userEmail);
        requestDTO.setPassword(password);

        // Отправляем POST запрос на /session/
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/session/",
                requestDTO,
                Void.class);

        // Проверяем, что статус ответа - OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("getUserDTOCombinationsForUpdate")
    @Order(3)
    public void testUpdateUser(FullUserDTO userDTO) {
        // Проверяем, что userId был получен в предыдущем тесте
        if (userId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(userEmail, password);

            HttpEntity<FullUserDTO> requestEntity = new HttpEntity<>(userDTO, headers);

            // Отправляем PATCH запрос на /users/{userId}/
            ResponseEntity<FullUserDTO> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/users/" + userId + "/",
                    HttpMethod.PATCH,
                    requestEntity,
                    FullUserDTO.class);

            // Проверяем, что статус ответа - OK (200)
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(userDTO, responseEntity.getBody());
            currentUserState = responseEntity.getBody();

        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }

    }


    @Test
    @Order(5)
    public void testDeleteUser() {
        if (userId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(userEmail, password);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/users/" + userId + "/",
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }
}
