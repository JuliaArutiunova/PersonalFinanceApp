package by.it_academy.jd2.classifier_service.errorHandler;


import by.it_academy.lib.error.ErrorResponse;
import by.it_academy.lib.error.FieldErrorDTO;
import by.it_academy.lib.error.StructuredErrorResponse;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<StructuredErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> fieldErrorDTOS = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        new FieldErrorDTO(fieldError.getDefaultMessage(), fieldError.getField()))
                .collect(Collectors.toList());

        StructuredErrorResponse structuredErrorResponse = new StructuredErrorResponse(fieldErrorDTOS);

        return new ResponseEntity<>(structuredErrorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = RecordAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleRecordAlreadyExists(RecordAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PageNotExistException.class)
    public ResponseEntity<ErrorResponse> handlePageNotExist(PageNotExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName() +
                " Сервер не смог корректно обработать запрос."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
