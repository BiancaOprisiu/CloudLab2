package de.msg.javatraining.donationmanager.service.donor;

public class DonorServiceException extends Exception{

    private String errorCode;

    public DonorServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

