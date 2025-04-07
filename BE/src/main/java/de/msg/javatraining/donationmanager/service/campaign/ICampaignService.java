package de.msg.javatraining.donationmanager.service.campaign;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;

import java.util.List;

/**
 * Service interface to define operations related to campaigns.
 */
public interface ICampaignService {

    /**
     * Retrieves a list of all campaigns.
     *
     * @return A list of all campaigns.
     * @throws CampaignServiceException If an error occurs while retrieving campaigns or if no campaigns are found.
     */
    List<Campaign> findAll() throws CampaignServiceException;

    /**
     * Retrieves a campaign by its ID.
     *
     * @param id The ID of the campaign to retrieve.
     * @return The retrieved campaign.
     * @throws CampaignServiceException If an error occurs while retrieving the campaign or if the campaign is not found.
     */
    Campaign findById(Integer id) throws CampaignServiceException;

    /**
     * Creates a new campaign.
     *
     * @param campaign The campaign to create.
     * @return The created campaign.
     * @throws CampaignServiceException If an error occurs while creating the campaign or if the campaign already exists.
     */
    Campaign createCampaign(Campaign campaign) throws CampaignServiceException;

    /**
     * Updates a campaign by its ID.
     * @param id             The ID of the campaign to update.
     * @param updatedCampaign The updated campaign data.
     * @return The updated campaign.
     * @throws CampaignServiceException If an error occurs while updating the campaign, if the campaign is not found
     * or the updated campaign leads to duplicate entities
     */
    Campaign updateCampaignById(Integer id, Campaign updatedCampaign) throws CampaignServiceException;

    /**
     * @param id The ID of the campaign to delete.
     * @throws CampaignServiceException If an error occurs while deleting the campaign, if the campaign is not found,
     *                                  or if the campaign has paid donations.
     */
    void deleteById(Long id) throws CampaignServiceException;
}
