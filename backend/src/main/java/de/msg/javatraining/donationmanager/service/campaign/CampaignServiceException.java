package de.msg.javatraining.donationmanager.service.campaign;

public class CampaignServiceException extends Exception{

    private String errorCode;

    public CampaignServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
