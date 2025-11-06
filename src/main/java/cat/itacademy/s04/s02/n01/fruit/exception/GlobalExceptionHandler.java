package cat.itacademy.s04.s02.n01.fruit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFruitNameException.class)
    public ResponseEntity<Object> handleInvalidName(InvalidFruitNameException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "InvalidFruitNameException", ex.getMessage());
    }

    @ExceptionHandler(InvalidFruitWeightException.class)
    public ResponseEntity<Object> handleInvalidWeight(InvalidFruitWeightException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "InvalidFruitWeightException", ex.getMessage());
    }

    @ExceptionHandler(FruitNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(FruitNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "FruitNotFoundException", ex.getMessage());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "ValidationError");
        body.put("message", ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}