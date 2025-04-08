package de.msg.javatraining.donationmanager.controller.donation;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.donation.DonationService;
import de.msg.javatraining.donationmanager.service.donation.DonationServiceException;
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


@ExtendWith(MockitoExtension.class)
class DonationControllerTest {

    @Mock
    private DonationService donationService;

    @InjectMocks
    private DonationController donationController;

    private Donation createTestDonation(long id) {
        Donation donation = new Donation();
        donation.setId(id);
        donation.setAmount(50);
        return donation;
    }

    @Test
    void testFindAll() throws DonationServiceException {
        List<Donation> donationList = new ArrayList<>();
        donationList.add(createTestDonation(1));
        donationList.add(createTestDonation(2));

        when(donationService.findAll()).thenReturn(donationList);

        ResponseEntity<?> response = donationController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(donationList, response.getBody());
    }

    @Test
    void testFindAllException() throws DonationServiceException {
        when(donationService.findAll()).thenThrow(new DonationServiceException("Database error", "DB_ERROR"));

        ResponseEntity<?> response = donationController.findAll();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Database error", response.getBody());
    }

    @Test
    void testFindById() throws DonationServiceException {
        long donationId = 1;
        Donation donation = createTestDonation(donationId);

        when(donationService.findById(donationId)).thenReturn(donation);

        ResponseEntity<?> response = donationController.findById(donationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(donation, response.getBody());
    }

    @Test
    void testFindByIdNotFound() throws DonationServiceException {
        long donationId = 1;
        when(donationService.findById(donationId)).thenThrow(new DonationServiceException("Donation not found", "NOT_FOUND"));

        ResponseEntity<?> response = donationController.findById(donationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String errorMessage = (String) response.getBody();
        assertEquals("Donation not found", errorMessage);
    }

    @Test
    void testCreateDonation() throws DonationServiceException {
        Donation donationToCreate = createTestDonation(1);

        when(donationService.createDonation(donationToCreate)).thenReturn(donationToCreate);

        ResponseEntity<?> response = donationController.createDonation(donationToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(donationToCreate, response.getBody());
    }

    @Test
    void testCreateDonationMissingAmount() throws DonationServiceException {
        Donation donationToCreate = createTestDonation(1);
        donationToCreate.setAmount(null);

        when(donationService.createDonation(donationToCreate))
                .thenThrow(new DonationServiceException("Amount missing", "AMOUNT_MISSING"));

        ResponseEntity<?> response = donationController.createDonation(donationToCreate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Amount missing", response.getBody());
    }



    @Test
    void testUpdateDonationById() throws DonationServiceException {
        long donationId = 1;
        Donation updatedDonation = createTestDonation(donationId);
        updatedDonation.setAmount(100);

        when(donationService.updateDonation(eq(donationId), any(Donation.class))).thenReturn(updatedDonation);

        ResponseEntity<?> response = donationController.updateDonationById(donationId, updatedDonation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDonation, response.getBody());
    }

    @Test
    void testDeleteById() throws DonationServiceException {
        long donationId = 1;

        ResponseEntity<?> response = donationController.deleteById(donationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(donationService).deleteById(donationId);
    }

    @Test
    void testApproveDonation() throws DonationServiceException {
        long donationId = 1;
        long approvingUserId = 1;

        ResponseEntity<?> response = donationController.approveDonation(donationId, approvingUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donation approved successfully", response.getBody());
        verify(donationService).approveDonation(eq(donationId), eq(approvingUserId));
    }

    @Test
    void testApproveDonationNotFound() throws DonationServiceException {
        long donationId = 1;
        long approvingUserId = 1;

        String expectedErrorMessage = "Donation not found";

        doThrow(new DonationServiceException(expectedErrorMessage, "FORBIDEN"))
                .when(donationService).approveDonation(eq(donationId), eq(approvingUserId));

        ResponseEntity<?> response = donationController.approveDonation(donationId, approvingUserId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        String responseBody = (String) response.getBody();
        assertEquals(expectedErrorMessage, responseBody);

        verify(donationService).approveDonation(eq(donationId), eq(approvingUserId));
    }
}
