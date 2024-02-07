import finist.back.BackApplication;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.model.enums.EducationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = {BackApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APITestsDebug {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegisterUser() {
        // Создаем объект запроса с данными пользователя
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setPhone("111");
        requestDTO.setEmail("testTest@mail.com");
        requestDTO.setPassword("12345678");
        // Установите данные пользователя для тестирования в requestDTO

        // Отправляем POST запрос на /users/
        ResponseEntity<UserRegistrationResponseDTO> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/",
                requestDTO,
                UserRegistrationResponseDTO.class);

        // Проверяем, что статус ответа - OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Добавьте другие проверки на ожидаемый ответ, если необходимо
    }


    @Test
    public void testSessionEndpoint() {
        // Создаем объект запроса с данными пользователя
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setEmail("testTest@mail.com");
        requestDTO.setPassword("12345678");

        // Отправляем POST запрос на /session/
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/session/",
                requestDTO,
                Void.class);

        // Проверяем, что статус ответа - OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


//    @ParameterizedTest
//    @MethodSource("getUserDTOCombinationsForUpdate")
    @Test
    public void testUpdateUser() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testTest@mail.com", "12345678");
        FullUserDTO userDTO = new FullUserDTO();

        HttpEntity<FullUserDTO> requestEntity = new HttpEntity<>(userDTO, headers);

        // Отправляем PATCH запрос на /users/{userId}/
        ResponseEntity<FullUserDTO> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/users/13/",
                HttpMethod.PATCH,
                requestEntity,
                FullUserDTO.class);

        // Проверяем, что статус ответа - OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    static Stream<Arguments> getUserDTOCombinationsForUpdate() {
        String userEmail = "test@example.com";
        // Создаем разные комбинации полей FullUserDTO для обновления
        return Stream.of(
                arguments(new FullUserDTO(userEmail, 1L).withName("John")),
                arguments(new FullUserDTO(userEmail, 1L).withPhone("1234567890")),
                arguments(new FullUserDTO(userEmail, 1L).withDateOfBirth(LocalDate.of(1990, 5, 15))),
                arguments(new FullUserDTO(userEmail, 1L).withName("Alice").withPhone("9876543210")),
                arguments(new FullUserDTO(userEmail, 1L).withName("Bob").withDateOfBirth(LocalDate.of(1985, 3, 20))),
                arguments(new FullUserDTO(userEmail, 1L).withPhone("5555555555").withDateOfBirth(LocalDate.of(1970, 10, 10))),
                arguments(new FullUserDTO(userEmail, 1L).withName("Charlie").withPhone("1231231234").withCity("New York")),
                arguments(new FullUserDTO(userEmail, 1L).withName("Emma").withDateOfBirth(LocalDate.of(1995, 8, 25)).withCity("Los Angeles")),
                arguments(new FullUserDTO(userEmail, 1L).withPhone("9998887777").withCity("Chicago").withEducation(EducationType.HIGHER))
        );
    }

    @Test
    public void testUpdateUserSuccess() { //тест успешного редактирования своего аккаунта
        // Arrange
        String baseUrl = "http://localhost:" + port + "/users/17/";
        FullUserDTO userDTO = new FullUserDTO();
        // Set userDTO properties

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBasicAuth("testTest@mail.com", "12345678");

        HttpEntity<FullUserDTO> request = new HttpEntity<>(userDTO, headers);

        // Act
        ResponseEntity<FullUserDTO> response = restTemplate.exchange(baseUrl, HttpMethod.PATCH, request, FullUserDTO.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions if needed
    }

}
