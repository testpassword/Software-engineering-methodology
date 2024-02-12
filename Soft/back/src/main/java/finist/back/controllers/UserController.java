package finist.back.controllers;

import finist.back.exceptions.AuthUserException;
import finist.back.exceptions.InvalidEmailException;
import finist.back.exceptions.ScenarioException;
import finist.back.exceptions.UserNotFoundException;
import finist.back.model.dto.FullUserDTO;
import finist.back.model.dto.UserArrowsDTO;
import finist.back.model.dto.UserAuthRequestDTO;
import finist.back.model.dto.UserRegistrationResponseDTO;
import finist.back.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/session/")
    public ResponseEntity<FullUserDTO> login(@RequestBody UserAuthRequestDTO userAuthRequestDTO) throws AuthUserException {
        return userService.login(userAuthRequestDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @PostMapping("/users/")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody UserAuthRequestDTO userAuthRequestDTO) throws InvalidEmailException {
        return userService.registerNewUser(userAuthRequestDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/users/")
    public ResponseEntity<List<FullUserDTO>> getAllUsers(){
        return userService.getAllUsers().map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/users/{userId}/")
    public ResponseEntity<FullUserDTO> getUser(@PathVariable(name = "userId") Long userId) throws UserNotFoundException {
        return userService.getUserAsDTO(userId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/users/{userId}/")
    public ResponseEntity<FullUserDTO> updateUser(@PathVariable(name = "userId") Long userId,
                                                  @RequestBody FullUserDTO userDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, ScenarioException {
        return userService.updateUser(userId,userDTO, userDetails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/users/{userId}/")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId,
                                           @AuthenticationPrincipal UserDetails userDetails) throws ScenarioException {
        try {
            userService.deleteUser(userId, userDetails);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{userId}/arrows/")
    public ResponseEntity<Integer> getArrowsAmount(@PathVariable(name = "userId") Long userId){
        return userService.getArrowsAmount(userId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/users/{userId}/arrows/")
    public ResponseEntity<Void> getArrows(@PathVariable(name = "userId") Long userId, @RequestBody UserArrowsDTO amount){
        try {
            userService.addArrows(userId, amount.getAmount());
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/users/brides/free/")
    public ResponseEntity<List<FullUserDTO>> getFreeBrides(@AuthenticationPrincipal UserDetails userDetails){
        return userService.getFreeBrides(userDetails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
