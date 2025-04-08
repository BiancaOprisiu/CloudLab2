package de.msg.javatraining.donationmanager.service.donation;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DonationServiceException extends Exception {

    private String errorCode;
    private HttpStatusCode statusCode;

    public DonationServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
