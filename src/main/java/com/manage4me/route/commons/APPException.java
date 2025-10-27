package com.manage4me.route.commons;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class APPException extends RuntimeException{

    private Errors error;

    public APPException(String message) {
        super(message);
    }

    public APPException(Supplier<String> errorCode) {
        super(errorCode.get());
        this.error = new Errors(errorCode.get(), new String[]{});
    }

    public APPException(Supplier<String> errorCode, String ... params) {
        super(errorCode.get());
        this.error = new Errors(errorCode.get(), params);
    }

    public APPException(Throwable t, Supplier<String> errorCode) {
        super(t);
        this.error = new Errors(errorCode.get(), new String[]{});
    }

    public static String getMessage(String msg, String ... param) {
        if (param == null || param.length == 0) {
            return msg;
        }
        return String.format(msg, param);
    }
}
