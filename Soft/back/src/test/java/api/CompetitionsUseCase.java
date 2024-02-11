package api;

import finist.back.BackApplication;
import finist.back.model.Task;
import finist.back.model.dto.*;
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
public class CompetitionsUseCase {

    private static Long userId;
    private static Long executorId = 70L;
    private static final String userEmail = "iftTestUser1@mail.com";
    private static final String password = "12345678";

    private static FullUserDTO currentUserState = new FullUserDTO();
    private static FullCompetitionDTO currentCompetitionState = new FullCompetitionDTO();
    private static FullTaskDTO currentTaskState = new FullTaskDTO();
    static String competitionName = "newTestCompetition";
    static String competitionCity = "Sarov";
    private static Long competitionId;
    private Integer candidatePoints;

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
            testChangeUserRoleHelper(UserRole.MATCHMAKER);
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(4)
    public void testCreateCompetitionSuccess() {
        if (userId != null) {
            NewCompetitionReqDTO competitionReqDTO = new NewCompetitionReqDTO();
            competitionReqDTO.setName(competitionName);
            competitionReqDTO.setCity(competitionCity);

            ResponseEntity<FullCompetitionDTO> responseEntity = sendRequest(
                    "http://localhost:" + port + "/competitions/",
                    HttpMethod.POST,
                    competitionReqDTO,
                    FullCompetitionDTO.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(competitionName, responseEntity.getBody().getName());
            assertEquals(competitionCity, responseEntity.getBody().getCity());

            currentCompetitionState = responseEntity.getBody();
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(5)
    public void testAddTaskToCompetitionSuccess() {
        if (userId != null) {
            FullTaskDTO taskDTO = new FullTaskDTO();
            taskDTO.setText("new task");
            taskDTO.setExecutorId(executorId);
            taskDTO.setReport("");
            taskDTO.setCompleted(false);

            ResponseEntity<FullTaskDTO> responseEntity = sendRequest(
                    "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/tasks/",
                    HttpMethod.POST,
                    taskDTO,
                    FullTaskDTO.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(taskDTO.getExecutorId(), responseEntity.getBody().getExecutorId());
            assertEquals(taskDTO.getText(), responseEntity.getBody().getText());

            currentTaskState = responseEntity.getBody();
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

    @Test
    @Order(6)
    public void testChangeCompetitionToVoting() {
            HttpHeaders headers = createHeaders();
            FullCompetitionDTO competitionDTO = currentCompetitionState;
            competitionDTO.setStatus("VOTING");
            HttpEntity<FullCompetitionDTO> requestEntity = new HttpEntity<>(competitionDTO, headers);

            ResponseEntity<FullCompetitionDTO> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/",
                    HttpMethod.PATCH,
                    requestEntity,
                    FullCompetitionDTO.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(competitionDTO.getName(), responseEntity.getBody().getName());
            assertEquals(competitionDTO.getStatus(), responseEntity.getBody().getStatus());
            currentCompetitionState = responseEntity.getBody();
    }

    @Test
    @Order(7)
    public void testGetBrightVoteWithCandidate() {
        ResponseEntity<FullBrideVoteDTO> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/bride_vote/",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                FullBrideVoteDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(executorId, responseEntity.getBody().getCandidates().get(0).getBrideId());
        candidatePoints = responseEntity.getBody().getCandidates().get(0).getPoints();
    }


    @Test
    @Order(8)
    public void testMakeVoteFirstTime() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/bride_vote/"+
                        executorId + "/vote/",
                HttpMethod.PATCH,
                new HttpEntity<>(createHeaders()),
                Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    public void testVoiceTracking() {
        ResponseEntity<FullBrideVoteDTO> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/bride_vote/",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                FullBrideVoteDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(executorId, responseEntity.getBody().getCandidates().get(0).getBrideId());
        assertEquals(1, responseEntity.getBody().getCandidates().get(0).getPoints());
    }

    @Test
    @Order(10)
    public void testMakeVoteSecondTime() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/bride_vote/"+
                        executorId + "/vote/",
                HttpMethod.PATCH,
                new HttpEntity<>(createHeaders()),
                Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    public void testDeleteCompetition() {
        if (userId != null) {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/competitions/" + currentCompetitionState.getId() + "/",
                    HttpMethod.DELETE,
                    new HttpEntity<>(createHeaders()),
                    Void.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } else {
            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
        }
    }

//    @Test
//    @Order(12)
//    public void testDeleteUser() {
//        if (userId != null) {
//            ResponseEntity<Void> responseEntity = restTemplate.exchange(
//                    "http://localhost:" + port + "/users/" + userId + "/",
//                    HttpMethod.DELETE,
//                    new HttpEntity<>(createHeaders()),
//                    Void.class);
//
//            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        } else {
//            throw new IllegalStateException("UserId is not set, make sure to run testRegisterUser first.");
//        }
//    }

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
