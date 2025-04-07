package de.msg.javatraining.donationmanager.service.permision;

public class PermisionServiceException extends Exception{

    private String errorCode;

    public PermisionServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
