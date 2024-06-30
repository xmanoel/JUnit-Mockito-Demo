package net.k.estore.service;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String failedToCreateUser) {
        super(failedToCreateUser);
    }
}
