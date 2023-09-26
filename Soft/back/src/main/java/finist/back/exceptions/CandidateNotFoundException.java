package finist.back.exceptions;

public class CandidateNotFoundException extends Throwable {
    public CandidateNotFoundException(Long candidateId) {
        super("Кандидат с id= " + candidateId + " не найден");
    }
}
