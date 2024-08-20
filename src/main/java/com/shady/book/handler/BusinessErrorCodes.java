package com.shady.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {

    NO_CODE(0,HttpStatus.NOT_IMPLEMENTED ,"No Code"),
    INCORRECT_CURRENT_PASSWORD(300,HttpStatus.BAD_REQUEST,"Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301,HttpStatus.BAD_REQUEST,"New password does not match"),
    ACCOUNT_LOCKED(302,HttpStatus.FORBIDDEN,"User account is locked"),
    ACCOUNT_DISABLED(302,HttpStatus.FORBIDDEN,"User account is Disabled"),
    BAD_CREDENTIALS(302,HttpStatus.FORBIDDEN,"Login id or password is incorrect"),


    ;
  //this code is

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code,HttpStatus httpStatus,String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
