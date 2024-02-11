package finist.back.services;

import finist.back.exceptions.AuthUserException;
import finist.back.exceptions.InvalidEmailException;
import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.User;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserRegistrationResponseDTO> registerNewUser(UserAuthRequestDTO userAuthRequestDTO) throws InvalidEmailException;
    Optional<List<FullUserDTO>> getAllUsers();
    Optional<FullUserDTO> getUserAsDTO(Long userId) throws UserNotFoundException;
    User getUser(Long userId) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    Optional<FullUserDTO> updateUser(Long userId, FullUserDTO userDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException;
    void deleteUser(Long userId, UserDetails userDetails) throws UserNotFoundException, ScenarioException;
    Optional<Integer> getArrowsAmount(Long userId);
    void addArrows(Long userId, Integer arrowsAmount) throws UserNotFoundException;
    Optional<FullUserDTO> login(UserAuthRequestDTO userAuthRequestDTO) throws AuthUserException;
}
