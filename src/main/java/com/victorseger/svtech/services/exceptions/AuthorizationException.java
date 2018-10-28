package com.victorseger.svtech.services.exceptions;

public class AuthorizationException extends RuntimeException {
    public static final long serialVersionUID =1L;

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable cause) {
        super(msg,cause);
    }


}
