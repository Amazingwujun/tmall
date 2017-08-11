package com.tmall.exception;

public class ArgumentsBindException extends RuntimeException{

    public ArgumentsBindException() {
        super();
    }

    public ArgumentsBindException(String msg) {
        super(msg);
    }

    public ArgumentsBindException(String msg, Throwable cause) {
        super(msg,cause);
    }


    public ArgumentsBindException(Throwable cause) {
        super(cause);
    }
}
