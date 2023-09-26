package finist.back.exceptions;

public class AlreadyMadeVoteException extends Throwable {
    public AlreadyMadeVoteException(Long userId, Long competitionId) {
        super("Пользователь с id= " + userId + " уже голосовал в соревновании id= " + competitionId);
    }
}
