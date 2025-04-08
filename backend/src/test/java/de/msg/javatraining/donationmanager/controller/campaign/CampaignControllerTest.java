package de.msg.javatraining.donationmanager.controller.campaign;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.service.campaign.CampaignService;
import de.msg.javatraining.donationmanager.service.campaign.CampaignServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {

    @Mock
    private CampaignService campaignService;

    @InjectMocks
    private CampaignController campaignController;

    @Test
    void testFindAllEmptyList() throws CampaignServiceException {
        when(campaignService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = campaignController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void testFindAllNonEmptyList() throws CampaignServiceException {
        List<Campaign> campaignList = new ArrayList<>();
        campaignList.add(createTestCampaign(1));
        campaignList.add(createTestCampaign(2));

        when(campaignService.findAll()).thenReturn(campaignList);

        ResponseEntity<?> response = campaignController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campaignList, response.getBody());
    }

    @Test
    void testFindById() throws CampaignServiceException {
        int campaignId = 1;
        Campaign campaign = createTestCampaign(campaignId);

        when(campaignService.findById(campaignId)).thenReturn(campaign);

        ResponseEntity<?> response = campaignController.findById(campaignId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campaign, response.getBody());
    }

    @Test
    void testFindByIdNotFound() throws CampaignServiceException {
        int campaignId = 1;
        when(campaignService.findById(campaignId)).thenThrow(new CampaignServiceException("Campaign not found", "NOT_FOUND"));

        ResponseEntity<?> response = campaignController.findById(campaignId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Campaign not found", response.getBody());
    }

    @Test
    void testCreateCampaign() throws CampaignServiceException {
        Campaign campaignToCreate = createTestCampaign(1);

        when(campaignService.createCampaign(campaignToCreate)).thenReturn(campaignToCreate);

        ResponseEntity<?> response = campaignController.createCampaign(campaignToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(campaignToCreate, response.getBody());
    }

    @Test
    void testCreateCampaignBadRequest() throws CampaignServiceException {
        Campaign campaignToCreate = new Campaign();

        when(campaignService.createCampaign(campaignToCreate)).thenThrow(new CampaignServiceException("Invalid campaign data", "INVALID_DATA"));

        ResponseEntity<?> response = campaignController.createCampaign(campaignToCreate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateCampaignById() throws CampaignServiceException {
        int campaignId = 1;
        Campaign updatedCampaign = createTestCampaign(campaignId);
        updatedCampaign.setName("Updated Name");

        when(campaignService.updateCampaignById(eq(campaignId), any(Campaign.class))).thenReturn(updatedCampaign);

        ResponseEntity<?> response = campaignController.updateCampaignById(campaignId, updatedCampaign);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCampaign, response.getBody());
    }

    @Test
    void testUpdateCampaignByIdNotFound() throws CampaignServiceException {
        int campaignId = 1;
        Campaign updatedCampaign = createTestCampaign(campaignId);

        when(campaignService.updateCampaignById(eq(campaignId), any(Campaign.class)))
                .thenThrow(new CampaignServiceException("Campaign not found", "NOT_FOUND"));

        ResponseEntity<?> response = campaignController.updateCampaignById(campaignId, updatedCampaign);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private Campaign createTestCampaign(int id) {
        Campaign campaign = new Campaign();
        campaign.setName("Name");
        campaign.setPurpose("Purpose");
        campaign.setId((long) id);
        return campaign;
    }
}
