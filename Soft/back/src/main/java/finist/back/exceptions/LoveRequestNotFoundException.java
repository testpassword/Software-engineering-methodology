package finist.back.exceptions;

public class LoveRequestNotFoundException extends Throwable {
    public LoveRequestNotFoundException(Long requestId) {
        super("Заявка с id= " + requestId + " не найдена");
    }
}
