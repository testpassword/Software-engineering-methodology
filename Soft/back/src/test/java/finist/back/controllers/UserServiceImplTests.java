package finist.back.controllers;

import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.Competition;
import finist.back.model.Task;
import finist.back.model.User;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.enums.EducationType;
import finist.back.repositories.UserRepository;
import finist.back.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


   static Collection<User> testDataGetAllUsersNullAndNotNullDependencies() {
        User user1 = new User(); // Для пользователя с пустыми задачами и подписками
        user1.setTasks(Collections.emptyList());
        user1.setFollowedCompetitions(Collections.emptyList());

        User user2 = new User(); // Для пользователя с задачами, но без подписок
        user2.setTasks(Arrays.asList(new Task(), new Task()));
        user2.setFollowedCompetitions(Collections.emptyList());

        User user3 = new User(); // Для пользователя с задачами и подписками
        user3.setTasks(Arrays.asList(new Task(), new Task()));
        user3.setFollowedCompetitions(Arrays.asList(new Competition(), new Competition()));

        return Arrays.asList(
                user1,
                user2,
                user3
        );
    }

    @ParameterizedTest
    @MethodSource("testDataGetAllUsersNullAndNotNullDependencies")
    void testGetAllUsersNullAndNotNullDependencies(User user) {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<FullUserDTO> userDTOList = userService.getAllUsers().orElseThrow();

        // Assert
        assertEquals(1, userDTOList.size());
        FullUserDTO userDTO = userDTOList.get(0);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void testGetAllUsersSuccessful() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<FullUserDTO> userDTOList = userService.getAllUsers().orElseThrow();

        // Assert
        assertEquals(1, userDTOList.size());
        FullUserDTO userDTO = userDTOList.get(0);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }


    static Collection<List<User>> testDataForTestGetAllUsersSuccessful() {
        // Создаем набор пользователей для теста
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@example.com");

        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("test3@example.com");

        return Arrays.asList(
                Collections.singletonList(user1),  // случай с одним пользователем
                Arrays.asList(user1, user2, user3)   // случай с несколькими пользователями
        );
    }

    @ParameterizedTest
    @MethodSource("testDataForTestGetAllUsersSuccessful")
    void testGetAllUsersSuccessful(List<User> users) {
        when(userRepository.findAll()).thenReturn(users);

        List<FullUserDTO> userDTOList = userService.getAllUsers().orElseThrow();

        assertEquals(users.size(), userDTOList.size());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            FullUserDTO userDTO = userDTOList.get(i);
            assertEquals(user.getId(), userDTO.getId());
            assertEquals(user.getEmail(), userDTO.getEmail());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, test@example.com",
            "2, test2@example.com"
    })
    void testGetUserSuccessful(long userId, String userEmail) throws UserNotFoundException {
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        FullUserDTO userDTO = userService.getUserAsDTO(userId).orElseThrow();

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }


    static Stream<Arguments> getUserDTOCombinationsForUpdate() {
        String userEmail = "test@example.com";
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

    @ParameterizedTest
    @MethodSource("getUserDTOCombinationsForUpdate")
    void testUpdateUserSuccessful(FullUserDTO userDTO) throws UserNotFoundException, ScenarioException {
        long userId = 1L;
        String userEmail = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
        when(userDetails.getUsername()).thenReturn(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // сохраняем переданный пользователь без изменений

        FullUserDTO updatedUserDTO = userService.updateUser(userId, userDTO, userDetails).orElseThrow();

        assertEquals(userDTO, updatedUserDTO);
    }

    @Test
    void testUpdateUserUnsuccessfulnoPermissions() throws UserNotFoundException, ScenarioException {
        long userId = 1L;
        String userEmail = "test@example.com";
        FullUserDTO userDTO = new FullUserDTO();
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);


        ScenarioException exception = assertThrows(ScenarioException.class, () -> userService.updateUser(userId, userDTO, userDetails));
        assertEquals("пользователь может редактировать только свой аккаунт", exception.getMessage());

    }

    @Test
    void testDeleteUser_Successful() throws UserNotFoundException, ScenarioException {
        long userId = 1L;
        String userEmail = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(userEmail); // Возвращаем правильный идентификатор пользователя
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId, userDetails);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_unsuccessful_no_permissions() {
        long userId = 1L;
        String userDetailsEmail = "test@example.com";
        String userToDeleteEmail = "test1@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(userDetailsEmail);
        User user = new User();
        user.setId(userId);
        user.setEmail(userToDeleteEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ScenarioException exception = assertThrows(ScenarioException.class, () -> userService.deleteUser(userId, userDetails));
        assertEquals("пользователь может редактировать только свой аккаунт", exception.getMessage());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "2, 20"
    })
    void testGetArrowsAmount_Successful(long userId, int arrowsAmount) {
        User user = new User();
        user.setId(userId);
        user.setArrowsAmount(arrowsAmount);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<Integer> result = userService.getArrowsAmount(userId);

        assertTrue(result.isPresent());
        assertEquals(arrowsAmount, result.get());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 5",
            "2, 15"
    })
    void testAddArrows_Successful(long userId, int arrowsAmount) throws UserNotFoundException {
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.addArrows(userId, arrowsAmount);

        assertEquals(arrowsAmount, user.getArrowsAmount());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers_NoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<FullUserDTO>> result = userService.getAllUsers();

        assertEquals(Optional.of(Collections.emptyList()), result);
    }

    @Test
    void testGetUser_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserAsDTO(userId));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        long userId = 1L;
        FullUserDTO userDTO = new FullUserDTO();
        UserDetails userDetails = mock(UserDetails.class);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userDTO, userDetails));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId, userDetails));
    }

    @Test
    void testGetArrowsAmount_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<Integer> result = userService.getArrowsAmount(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testAddArrows_UserNotFound() {
        long userId = 1L;
        int arrowsAmount = 10;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addArrows(userId, arrowsAmount));
    }

}
