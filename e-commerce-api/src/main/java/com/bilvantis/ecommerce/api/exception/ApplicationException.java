package com.bilvantis.ecommerce.api.exception;

public class ApplicationException extends RuntimeException {

    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApplicationException(Throwable throwable) { super(throwable);  }
}
