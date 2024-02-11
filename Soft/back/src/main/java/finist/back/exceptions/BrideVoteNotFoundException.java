package finist.back.exceptions;

public class BrideVoteNotFoundException extends Throwable {
    public BrideVoteNotFoundException(Long competitionId) {
        super("не найдено голосование для состязание с id = " + competitionId);
    }
}
