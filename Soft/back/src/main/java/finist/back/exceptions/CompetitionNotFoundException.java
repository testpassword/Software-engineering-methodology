package finist.back.exceptions;

public class CompetitionNotFoundException extends Throwable {
    public CompetitionNotFoundException(Long id) {
        super("соревнование с id="+id+"не найдено");
    }
}
