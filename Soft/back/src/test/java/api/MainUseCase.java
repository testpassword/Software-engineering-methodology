package api;

import finist.back.BackApplication;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.model.enums.UserRole;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {BackApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainUseCase {

    private static Long userId;
    private static final String userEmail = "testUser2@mail.com";
    private static final String password = "12345678";

    private static FullUserDTO currentUserState = new FullUserDTO();
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userEmail, password);
        return headers;
    }

    private <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = createHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }

    private <T> ResponseEntity<T> sendRequestNoAuth(String url, HttpMethod method, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }

    @Test
    @Order(1)
    public void testRegisterUser() {
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setEmail(userEmail);
        requestDTO.setPassword(password);

        ResponseEntity<UserRegistrationResponseDTO> responseEntity = sendRequestNoAuth(
                "http://localhost:" + port + "/users/",
                HttpMethod.POST,
                requestDTO,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        userId = responseEntity.getBody().getId();
        currentUserState.setId(userId);
        currentUserState.setEmail(userEmail);
    }

    @Test
    @Order(2)
    public void testSessionEndpoint() {
        UserAuthRequestDTO requestDTO = new UserAuthRequestDTO();
        requestDTO.setEmail(userEmail);
        requestDTO.setPassword(password);

        ResponseEntity<Void> responseEntity = sendRequest(
                "http://localhost:" + port + "/session/",
                HttpMethod.POST,
                requestDTO,
                Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    public void testChangeUserRole() {
        if (userId != null) {
            testChangeUserRoleHelper(UserRole.GROOM);
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(4)
    public void testGetUserListAsGroom() {
        if (userId != null) {
            ResponseEntity<Object> responseEntity = sendRequest(
                    "http://localhost:" + port + "/users/",
                    HttpMethod.GET,
                    null,
                    Object.class);

            assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(5)
    public void testChangeUserRole2() {
        if (userId != null) {
            testChangeUserRoleHelper(UserRole.MATCHMAKER);
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(6)
    public void testGetUserListAsMatchmaker() {
        if (userId != null) {
            ResponseEntity<Object[]> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/users/",
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    Object[].class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertTrue(responseEntity.getBody().length > 0);
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(7)
    public void testDeleteUser() {
        if (userId != null) {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/users/" + userId + "/",
                    HttpMethod.DELETE,
                    new HttpEntity<>(createHeaders()),
                    Void.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }


    private void testChangeUserRoleHelper(UserRole role) {
        if (userId != null) {
            HttpHeaders headers = createHeaders();
            FullUserDTO userDto = new FullUserDTO().withEmail(userEmail).withRole(role);
            HttpEntity<FullUserDTO> requestEntity = new HttpEntity<>(userDto, headers);

            ResponseEntity<FullUserDTO> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/users/" + userId + "/",
                    HttpMethod.PATCH,
                    requestEntity,
                    FullUserDTO.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(role.getCode(), responseEntity.getBody().getRole());
            currentUserState = responseEntity.getBody();
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }
}
