package com.alazeprt.utils;

public class PixelException {
    private final int code;
    private final String message;
    private final Exception e;
    public PixelException(Exception e) {
        this.e = e;
        if(e == null) {
            this.code = 0;
            this.message = "Successfully created!";
        } else {
            this.code = 1;
            this.message = "Failed to create!";
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        if(code == 0) {
            return message;
        } else {
            return message + " Reason: \n" + e.toString();
        }
    }

    public Exception getException() {
        return e;
    }
}
