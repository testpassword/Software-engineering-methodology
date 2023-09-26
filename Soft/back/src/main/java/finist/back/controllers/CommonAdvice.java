package finist.back.controllers;

import finist.back.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;


@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handleNoSuchElementException(UserNotFoundException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CompetitionNotFoundException.class})
    public ResponseEntity<String> handleNoSuchElementException(CompetitionNotFoundException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CandidateNotFoundException.class})
    public ResponseEntity<String> handleNoSuchElementException(CandidateNotFoundException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({BrideVoteNotFoundException.class})
    public ResponseEntity<String> handleNoSuchElementException(BrideVoteNotFoundException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({AlreadyMadeVoteException.class})
    public ResponseEntity<String> handleNoSuchElementException(AlreadyMadeVoteException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthUserException.class})
    public ResponseEntity<String> handleNoSuchElementException(AuthUserException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({ScenarioException.class})
    public ResponseEntity<String> handleNoSuchElementException(ScenarioException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({InvalidEmailException.class})
    public ResponseEntity<String> handleNoSuchElementException(InvalidEmailException exception){
        System.err.println(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
