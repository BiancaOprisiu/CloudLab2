package de.msg.javatraining.donationmanager.service.campaign;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.validation.CampaignValidation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignValidation campaignValidation;

    @InjectMocks
    private CampaignService campaignService;

    @Test
    void testFindAll_ReturnsExpectedList_WhenCampaignsExist() throws CampaignServiceException {
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(new Campaign());

        when(campaignRepository.findAll()).thenReturn(campaigns);

        List<Campaign> result = campaignService.findAll();

        assertEquals(campaigns, result);
    }

    @Test
    void testFindById_ReturnsCampaign_WhenExistingIdGiven() throws CampaignServiceException {
        int campaignId = 1;
        Campaign campaign = new Campaign();
        campaign.setId((long) campaignId);

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.of(campaign));

        Campaign result = campaignService.findById(campaignId);

        assertEquals(campaign, result);
    }

    @Test
    void testFindById_ThrowsCampaignServiceException_WhenNonExistingIdGiven() {
        int campaignId = 1;

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.empty());

        assertThrows(CampaignServiceException.class, () -> campaignService.findById(campaignId));
    }

    @Test
    void testDeleteById_DeletesCampaign_WhenExistingIdAndNoDonations() {
        int campaignId = 1;
        Campaign campaign = new Campaign();
        campaign.setId((long) campaignId);

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.of(campaign));

        assertDoesNotThrow(() -> campaignService.deleteById((long) campaignId));

        verify(campaignRepository, times(1)).deleteById((long) campaignId);
    }

    @Test
    void testDeleteById_ThrowsCampaignServiceException_WhenExistingIdAndWithDonations() throws CampaignServiceException {
        long campaignId = 1;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.getDonationList().add(new Donation());

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        doThrow(new CampaignServiceException("Campaign with donations cannot be deleted", "CAMPAIGN02"))
                .when(campaignValidation).validateDeleteCampaign(campaign);

        assertThrows(CampaignServiceException.class, () -> campaignService.deleteById(campaignId));

        verify(campaignRepository, times(0)).deleteById(campaignId);
    }



    @Test
    void testDeleteById_ThrowsCampaignServiceException_WhenNonExistingId() {
        int campaignId = 1;

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.empty());

        assertThrows(CampaignServiceException.class, () -> campaignService.deleteById((long) campaignId));
    }

    @Test
    void testCreateCampaign_ThrowsNoException_WhenValidCampaignGiven() throws CampaignServiceException {
        Campaign campaign = new Campaign();
        campaign.setName("Valid Name");
        campaign.setPurpose("Valid Purpose");

        when(campaignRepository.findByName(campaign.getName())).thenReturn(Optional.empty());
        doNothing().when(campaignValidation).validateCreateCampaign(campaign);
        when(campaignRepository.save(campaign)).thenReturn(campaign);

        Campaign result = campaignService.createCampaign(campaign);

        assertEquals(campaign, result);

        verify(campaignRepository, times(1)).save(campaign);
        verify(campaignValidation, times(1)).validateCreateCampaign(campaign);
    }

    @Test
    void testCreateCampaign_ThrowsCampaignServiceException_WhenExistingNameGiven() {
        Campaign campaign = new Campaign();
        campaign.setName("Existing Name");
        campaign.setPurpose("Valid Purpose");

        when(campaignRepository.findByName(campaign.getName())).thenReturn(Optional.of(campaign));

        assertThrows(CampaignServiceException.class, () -> campaignService.createCampaign(campaign));

        verify(campaignRepository, times(0)).save(campaign);
    }

    @Test
    void testCreateCampaign_ThrowsCampaignServiceException_WhenInvalidCampaignGiven() throws CampaignServiceException {
        Campaign campaign = new Campaign(); // Invalid campaign with missing fields

        when(campaignRepository.findByName(campaign.getName())).thenReturn(Optional.empty());
        doThrow(CampaignServiceException.class).when(campaignValidation).validateCreateCampaign(campaign);

        assertThrows(CampaignServiceException.class, () -> campaignService.createCampaign(campaign));

        verify(campaignRepository, times(0)).save(campaign);
        verify(campaignValidation, times(1)).validateCreateCampaign(campaign);
    }

    @Test
    void testUpdateCampaignById_UpdatesCampaign_WhenExistingIdAndValidCampaign() throws CampaignServiceException {
        int campaignId = 1;
        Campaign existingCampaign = new Campaign();
        existingCampaign.setId((long) campaignId);
        String updatedName = "Updated Name";
        Campaign updatedCampaign = new Campaign();
        updatedCampaign.setName(updatedName);

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.of(existingCampaign));
        when(campaignRepository.findByName(updatedName)).thenReturn(Optional.empty());
        doNothing().when(campaignValidation).validateUpdateCampaign(existingCampaign, updatedName, Optional.empty());
        when(campaignRepository.save(existingCampaign)).thenReturn(existingCampaign);

        Campaign result = campaignService.updateCampaignById(campaignId, updatedCampaign);

        assertEquals(existingCampaign, result);

        verify(campaignRepository, times(1)).save(existingCampaign);
        verify(campaignValidation, times(1)).validateUpdateCampaign(existingCampaign, updatedName, Optional.empty());
    }

    @Test
    void testUpdateCampaignById_ThrowsCampaignServiceException_WhenNonExistingId() throws CampaignServiceException {
        int campaignId = 1;
        Campaign updatedCampaign = new Campaign();

        when(campaignRepository.findById((long) campaignId)).thenReturn(Optional.empty());

        assertThrows(CampaignServiceException.class, () -> campaignService.updateCampaignById(campaignId, updatedCampaign));

        verify(campaignRepository, times(0)).save(any(Campaign.class));
        verify(campaignValidation, times(0)).validateUpdateCampaign(any(Campaign.class), anyString(), any(Optional.class));
    }
}
