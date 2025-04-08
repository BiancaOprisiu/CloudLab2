package de.msg.javatraining.donationmanager.controller.donor;

import org.junit.jupiter.api.Test;

import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.service.donor.DonorService;
import de.msg.javatraining.donationmanager.service.donor.DonorServiceException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DonorControllerTest {

    @Mock
    private DonorService donorService;

    @InjectMocks
    private DonorController donorController;

    @Test
    void testFindAllEmptyList() throws DonorServiceException {
        when(donorService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = donorController.findAll();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void testFindAllNonEmptyList() throws DonorServiceException {
        List<Donor> donorList = new ArrayList<>();
        donorList.add(createTestDonor(1));
        donorList.add(createTestDonor(2));

        when(donorService.findAll()).thenReturn(donorList);

        ResponseEntity<?> response = donorController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(donorList, response.getBody());
    }

    @Test
    void testFindById() throws DonorServiceException {
        int donorId = 1;
        Donor donor = createTestDonor(donorId);

        when(donorService.findById(donorId)).thenReturn(donor);

        ResponseEntity<?> response = donorController.findById(donorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(donor, response.getBody());
    }

    @Test
    void testFindByIdNotFound() throws DonorServiceException {
        int donorId = 1;
        when(donorService.findById(donorId)).thenThrow(new DonorServiceException("Donor not found", "NOT_FOUND"));

        ResponseEntity<?> response = donorController.findById(donorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Donor not found", response.getBody());
    }

    @Test
    void testCreateDonor() throws DonorServiceException {
        Donor donorToCreate = createTestDonor(1);

        when(donorService.createDonor(donorToCreate)).thenReturn(donorToCreate);

        ResponseEntity<?> response = donorController.createDonor(donorToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(donorToCreate, response.getBody());
    }

    @Test
    void testCreateDonorBadRequest() throws DonorServiceException {
        Donor donorToCreate = new Donor(); // Invalid donor without firstname and lastname

        when(donorService.createDonor(donorToCreate)).thenThrow(new DonorServiceException("Invalid donor data", "INVALID_DATA"));

        ResponseEntity<?> response = donorController.createDonor(donorToCreate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateDonorById() throws DonorServiceException {
        int donorId = 1;
        Donor updatedDonor = createTestDonor(donorId);
        updatedDonor.setFirstname("Updated FName");

        when(donorService.updateDonorById(eq(donorId), any(Donor.class)))
                .thenReturn(updatedDonor);

        ResponseEntity<Donor> response = donorController.updateDonorById(donorId, updatedDonor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDonor, response.getBody());
    }

    @Test
    void testUpdateDonorByIdNotFound() throws DonorServiceException {
        int donorId = 1;
        Donor updatedDonor = createTestDonor(donorId);

        when(donorService.updateDonorById(eq(donorId), any(Donor.class)))
                .thenThrow(new DonorServiceException("Donor not found", "NOT_FOUND"));

        ResponseEntity<Donor> response = donorController.updateDonorById(donorId, updatedDonor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteById() throws DonorServiceException {
        int donorId = 1;

        ResponseEntity<?> response = donorController.deleteById(donorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OK", response.getBody());

        verify(donorService).deleteById(donorId);
    }

    private Donor createTestDonor(int id) {
        Donor donor = new Donor();
        donor.setFirstname("FName");
        donor.setLastname("LName");
        donor.setId((long) id);
        return donor;
    }
}
