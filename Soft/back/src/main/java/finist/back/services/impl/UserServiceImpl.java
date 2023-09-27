package finist.back.services.impl;

import finist.back.exceptions.AuthUserException;
import finist.back.exceptions.InvalidEmailException;
import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.Role;
import finist.back.model.User;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.model.enums.EducationType;
import finist.back.model.enums.UserRole;
import finist.back.repositories.RoleRepository;
import finist.back.repositories.TaskRepository;
import finist.back.repositories.UserRepository;
import finist.back.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserRegistrationResponseDTO> registerNewUser(UserAuthRequestDTO userAuthRequestDTO) throws InvalidEmailException {
        if (!isEmailUnique(userAuthRequestDTO.getEmail())) throw new InvalidEmailException("К данной почте уже привязан аккаунт");
        Role guestRole = roleRepository.findByName("GUEST").get();
        return Optional.of(new UserRegistrationResponseDTO(
                userRepository.save(new User(userAuthRequestDTO.getEmail(),
                        passwordEncoder.encode(userAuthRequestDTO.getPassword()),
                        userAuthRequestDTO.getPhone(), guestRole))));

    }

    private boolean isEmailUnique(String email) {
        boolean reuslt = !userRepository.existsByEmail(email);
        return reuslt;
    }

    @Override
    public Optional<List<FullUserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return Optional.of(users.stream()
                .map(this::convertUserToFullUserDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<FullUserDTO> getUser(Long userId) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findById(userId)
                .map(this::convertUserToFullUserDTO)
                .orElseThrow(() ->
                        new UserNotFoundException(userId)));
    }


    public Optional<FullUserDTO> updateUser(Long userId, FullUserDTO userDTO, UserDetails userDetails) throws UserNotFoundException, ScenarioException {
        Optional<User> wrappedUser = userRepository.findById(userId);
        if (wrappedUser.isEmpty()) throw new UserNotFoundException(userId);
        User user =  wrappedUser.get();
        if (!user.getEmail().equals(userDetails.getUsername()))
            throw new ScenarioException("пользователь может редактировать только свой аккаунт");
        updateUserAttributes(user, userDTO);
        if (user.getRole() == UserRole.GROOM) user.setArrowsAmount(1);
        return Optional.of(convertUserToFullUserDTO(userRepository.save(user)));
    }


    private void updateUserAttributes(User user, FullUserDTO userDTO) {
        Optional.ofNullable(userDTO.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDTO.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(userDTO.getDateOfBirth()).ifPresent(user::setBirthDate);
        Optional.ofNullable(userDTO.getCity()).ifPresent(user::setCity);
        Optional.ofNullable(userDTO.getEducation()).ifPresent(newEducation -> user.setEducationType(EducationType.getByCode(newEducation)));
        Optional.ofNullable(userDTO.getRole()).ifPresent(newRole -> {
            user.setRole(UserRole.getByCode(newRole));
            Optional<Role> wrappedRole = roleRepository.findByName(user.getRole().toString());
            wrappedRole.ifPresent(role -> {
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                user.setRoles(roles);
            });
        });
        Optional.ofNullable(userDTO.getAboutSelf()).ifPresent(user::setAboutSelf);
        Optional.ofNullable(userDTO.getAboutPartner()).ifPresent(user::setAboutPartner);
        Optional.ofNullable(userDTO.getIsPairing()).ifPresent(user::setIsPairing);
    }

    @Override
    public void deleteUser(Long userId, UserDetails userDetails) throws UserNotFoundException, ScenarioException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            if (!userOptional.get().getEmail().equals(userDetails.getUsername()))
                throw new ScenarioException("пользователь может редактировать только свой аккаунт");
            User user = userOptional.get();
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public Optional<Integer> getArrowsAmount(Long userId) {
        return userRepository.findById(userId)
                .map(User::getArrowsAmount);
    }

    @Override
    public void addArrows(Long userId, Integer arrowsAmount) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setArrowsAmount(arrowsAmount);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public Optional<FullUserDTO> login(UserAuthRequestDTO userAuthRequestDTO) throws AuthUserException {
        Optional<User> wrappedUser = userRepository.findByEmail(userAuthRequestDTO.getEmail());

        if (wrappedUser.isPresent()) {
            User user = wrappedUser.get();
            if (passwordEncoder.matches(userAuthRequestDTO.getPassword(), user.getPassword())) {
                return Optional.of(convertUserToFullUserDTO(user));
            }
        }

        throw new AuthUserException();
    }

    private FullUserDTO convertUserToFullUserDTO(User user) {
        return new FullUserDTO(user);
    }
}
