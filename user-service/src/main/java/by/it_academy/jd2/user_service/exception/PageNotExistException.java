package by.it_academy.jd2.user_service.exception;

import lombok.Getter;

@Getter
public class PageNotExistException extends RuntimeException{
   private int pageNumber;

    public PageNotExistException(int pageNumber) {
        this.pageNumber = pageNumber;
    }

}
