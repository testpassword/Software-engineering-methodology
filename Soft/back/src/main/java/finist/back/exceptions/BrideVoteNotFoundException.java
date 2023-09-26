package finist.back.exceptions;

public class BrideVoteNotFoundException extends Throwable {
    public BrideVoteNotFoundException(Long brideVoteId) {
        super("не найдена запись с brideVoteId = " + brideVoteId);
    }
}
