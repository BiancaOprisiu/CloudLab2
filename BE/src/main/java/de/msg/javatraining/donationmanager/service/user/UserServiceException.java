package de.msg.javatraining.donationmanager.service.user;

public class UserServiceException extends Exception{
    private String errorCode;

    public UserServiceException(String message,String errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }



}
