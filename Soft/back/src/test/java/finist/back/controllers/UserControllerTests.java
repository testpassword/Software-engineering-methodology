package finist.back.controllers;

import finist.back.exceptions.*;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() throws AuthUserException {
        // Arrange
        String email = "example@example.com";
        String password = "password";
        String phone = "123456789";
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(phone, email, password);
        FullUserDTO fullUserDTO = new FullUserDTO();
        when(userService.login(userAuthRequestDTO)).thenReturn(Optional.of(fullUserDTO));

        ResponseEntity<FullUserDTO> response = userController.login(userAuthRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fullUserDTO, response.getBody());
    }

    @Test
    void testLogin_Unsuccessful() throws AuthUserException {
        String email = "example@example.com";
        String password = "password";
        String phone = "123456789";
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(phone, email, password);
        when(userService.login(userAuthRequestDTO)).thenReturn(Optional.empty());

        ResponseEntity<FullUserDTO> response = userController.login(userAuthRequestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testRegisterUser_Successful() throws InvalidEmailException {
        String email = "example@example.com";
        String password = "password";
        String phone = "123456789";
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(phone, email, password);
        UserRegistrationResponseDTO userRegistrationResponseDTO = new UserRegistrationResponseDTO();
        when(userService.registerNewUser(userAuthRequestDTO)).thenReturn(Optional.of(userRegistrationResponseDTO));

        ResponseEntity<UserRegistrationResponseDTO> response = userController.registerUser(userAuthRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userRegistrationResponseDTO, response.getBody());
    }

    @Test
    void testRegisterUser_InvalidEmail() throws InvalidEmailException {
        String email = "example@example.com";
        String password = "password";
        String phone = "123456789";
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(phone, email, password);
        when(userService.registerNewUser(userAuthRequestDTO)).thenThrow(InvalidEmailException.class);

        assertThrows(InvalidEmailException.class, () -> userController.registerUser(userAuthRequestDTO));
    }

    @Test
    void testGetAllUsers() {
        List<FullUserDTO> users = Collections.singletonList(new FullUserDTO());
        when(userService.getAllUsers()).thenReturn(Optional.of(users));

        ResponseEntity<List<FullUserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetUser_Successful() throws UserNotFoundException {
        Long userId = 1L;
        FullUserDTO userDTO = new FullUserDTO();
        when(userService.getUserAsDTO(userId)).thenReturn(Optional.of(userDTO));

        ResponseEntity<FullUserDTO> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testGetUser_NotFound() throws UserNotFoundException {
        Long userId = 1L;
        when(userService.getUserAsDTO(userId)).thenReturn(Optional.empty());

        ResponseEntity<FullUserDTO> response = userController.getUser(userId);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUser_Successful() throws UserNotFoundException, ScenarioException {
        // Arrange
        Long userId = 1L;
        FullUserDTO userDTO = new FullUserDTO();
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.updateUser(userId, userDTO, userDetails)).thenReturn(Optional.of(userDTO));

        // Act
        ResponseEntity<FullUserDTO> response = userController.updateUser(userId, userDTO, userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testUpdateUser_NotFound() throws UserNotFoundException, ScenarioException {
        // Arrange
        Long userId = 1L;
        FullUserDTO userDTO = new FullUserDTO();
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.updateUser(userId, userDTO, userDetails)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<FullUserDTO> response = userController.updateUser(userId, userDTO, userDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUser_Successful() throws UserNotFoundException, ScenarioException {
        Long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class);

        ResponseEntity<Void> response = userController.deleteUser(userId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUser_NotFound() throws UserNotFoundException, ScenarioException {
        Long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class); // Подставьте UserDetails при необходимости
        doThrow(UserNotFoundException.class).when(userService).deleteUser(userId, userDetails);

        ResponseEntity<Void> response = userController.deleteUser(userId, userDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetArrowsAmount_Successful() {
        Long userId = 1L;
        Integer arrowsAmount = 10;
        when(userService.getArrowsAmount(userId)).thenReturn(Optional.of(arrowsAmount));

        ResponseEntity<Integer> response = userController.getArrowsAmount(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(arrowsAmount, response.getBody());
    }

    @Test
    void testGetArrowsAmount_NotFound() {
        Long userId = 1L;
        when(userService.getArrowsAmount(userId)).thenReturn(Optional.empty());

        ResponseEntity<Integer> response = userController.getArrowsAmount(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
