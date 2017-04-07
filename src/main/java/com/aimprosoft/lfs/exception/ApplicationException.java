package com.aimprosoft.lfs.exception;

/**
 * The exception which warps all exceptions in the application
 *
 * @author AimProSoft
 */
public class ApplicationException extends Exception {

    public ApplicationException() {
        super("ts-internal-server-error");
    }

    public ApplicationException(String message, Object... args) {
        super(String.format(message, args));
    }

}