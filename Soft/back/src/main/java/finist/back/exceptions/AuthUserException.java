package finist.back.exceptions;

public class AuthUserException extends Throwable {
    public AuthUserException() {
        super("Неверный логин или пароль");
    }
}
