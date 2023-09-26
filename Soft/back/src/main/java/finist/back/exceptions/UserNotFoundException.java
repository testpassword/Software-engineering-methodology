package finist.back.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(Long userId) {
        super("Пользователь с id= " + userId + " не найден");
    }
    public UserNotFoundException(String username) {
        super("Пользователь " + username + " не найден");
    }
}
