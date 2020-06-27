package com.jr.data;

/**
 * Packages user error message and passes it to external error handler
 *
 * @author Julian Ragan
 */
public class UserErrorContainer {

    private String msg;

    public UserErrorContainer(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
