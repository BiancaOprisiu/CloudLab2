package de.msg.javatraining.donationmanager.validation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.service.campaign.CampaignService;
import de.msg.javatraining.donationmanager.service.campaign.CampaignServiceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CampaignValidation {
    CampaignService campaignService;
    public void validateCreateCampaign(Campaign campaign) throws CampaignServiceException {
        if (campaign.getName() == null || campaign.getName().isEmpty() ||
                campaign.getPurpose() == null || campaign.getPurpose().isEmpty()) {
            throw new CampaignServiceException("Name and purpose for campaign are required", "CAMPAIGN02");
        }
    }

    public void validateUpdateCampaign(Campaign existingCampaign, String updatedCampaignName,
                                       Optional<Campaign> campaignWithUpdatedName) throws CampaignServiceException {
        if (!existingCampaign.getName().equals(updatedCampaignName) && campaignWithUpdatedName.isPresent()) {
            throw new CampaignServiceException("Campaign with the updated name already exists", "CAMPAIGN02");
        }
    }

    public void validateDeleteCampaign(Campaign campaign) throws CampaignServiceException {
        if (!campaign.getDonationList().isEmpty()) {
            throw new CampaignServiceException("Campaign with donations cannot be deleted", "CAMPAIGN02");
        }
    }

}




