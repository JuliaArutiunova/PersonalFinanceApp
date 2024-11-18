package by.it_academy.jd2.user_service.errorHandler;

import by.it_academy.jd2.user_service.dto.error.ErrorResponse;
import by.it_academy.jd2.user_service.dto.error.FieldErrorDTO;
import by.it_academy.jd2.user_service.dto.error.StructuredErrorResponse;
import by.it_academy.jd2.user_service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        List<FieldErrorDTO> fieldErrorDTOS = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            fieldErrorDTOS.add(new FieldErrorDTO(fieldError.getDefaultMessage(), fieldError.getField()));
        });
        StructuredErrorResponse structuredErrorResponse = new StructuredErrorResponse(fieldErrorDTOS);
        return new ResponseEntity<>(structuredErrorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = MailAlreadyExistException.class)
    public ResponseEntity<Object> handleMailAlreadyExist(MailAlreadyExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Пользователь с email "
                + ex.getEmail() + " уже существует");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = CodeNotValidException.class)
    public ResponseEntity<Object> handleCodeNotValid(CodeNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PasswordNotValidException.class)
    public ResponseEntity<Object> handlePasswordNotValid(PasswordNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PageNotExistException.class)
    public ResponseEntity<Object> handlePageNotExist(PageNotExistException e) {
        ErrorResponse errorResponse = new ErrorResponse("Страницы с таким номером  не существует");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = DataChangedException.class)
    public ResponseEntity<Object> handleDataChanged(DataChangedException e) {
        ErrorResponse errorResponse = new ErrorResponse("Данные уже были изменены");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName() +
                " Сервер не смог корректно обработать запрос."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
