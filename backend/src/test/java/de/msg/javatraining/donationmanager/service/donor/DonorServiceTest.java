package de.msg.javatraining.donationmanager.service.donor;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.persistence.repository.DonorRepository;
import de.msg.javatraining.donationmanager.validation.DonorValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonorServiceTest {

    @Mock
    private DonorRepository donorRepository;

    @Mock
    private DonorValidation donorValidation;

    @InjectMocks
    private DonorService donorService;


    @Test
    void testFindAll_ThrowsException_WhenListIsEmpty() throws DonorServiceException{
        when(donorRepository.findAll()).thenReturn(new ArrayList<Donor>());
        assertThrows(DonorServiceException.class, () -> donorService.findAll());
        try{
            donorService.findAll();
        }catch (DonorServiceException dse){
            assertEquals("NO_DONORS_FOUND",dse.getErrorCode());
        }catch (Exception e){
            assert(false);
        }
    }

    @Test
    void testFindAll_ReturnsExpectedList_WhenCampaignsExist() throws DonorServiceException {
        List<Donor> donors = new ArrayList<>();
        donors.add(new Donor());

        when(donorRepository.findAll()).thenReturn(donors);

        List<Donor> result = donorService.findAll();

        assertEquals(donors, result);
    }

    @Test
    void testFindById_ExistingId_ReturnsDonor_WhenValidIdGiven() throws DonorServiceException {
        int donorId = 1;
        Donor donor = new Donor();
        donor.setId((long) donorId);

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.of(donor));

        Donor result = donorService.findById(donorId);

        assertEquals(donor, result);
    }

    @Test
    void testFindById_ThrowsDonorServiceException_WhenNonExistingIdGiven() {
        int donorId = 1;

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.empty());

        assertThrows(DonorServiceException.class, () -> donorService.findById(donorId));
    }

    @Test
    void testDeleteById_DeletesDonor_WhenExistingIdAndNoDonations() {
        int donorId = 1;
        Donor donor = new Donor();
        donor.setId((long) donorId);

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.of(donor));
        donor.setDonationList(new ArrayList<>());

        assertDoesNotThrow(() -> donorService.deleteById(donorId));

        verify(donorRepository, times(1)).deleteById((long) donorId);
    }

    @Test
    void testDeleteById_UpdatesDonor_WhenExistingIdAndDonationsExist() throws DonorServiceException {
        int donorId = 1;
        Donor donor = new Donor();
        donor.setId((long) donorId);
        donor.getDonationList().add(new Donation()); // Add a donation to simulate donor with donations

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.of(donor));
        when(donorRepository.save(any(Donor.class))).thenReturn(donor); // Mock the save method

        donorService.deleteById(donorId);

        // Verify that the donor's fields have been updated to "Unknown"
        assertEquals("Unknown", donor.getFirstname());
        assertEquals("Unknown", donor.getLastname());
        assertEquals("Unknown", donor.getAdditionalName());
        assertEquals("Unknown", donor.getMaidenName());

        verify(donorRepository, times(1)).save(donor); // Ensure save was called
        verify(donorRepository, times(0)).deleteById((long) donorId); // Ensure deleteById was not called
    }

    @Test
    void testDeleteById_ThrowsDonorServiceException_WhenNonExistingIdGiven() {
        int donorId = 1;

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.empty());

        assertThrows(DonorServiceException.class, () -> donorService.deleteById(donorId));
    }

    @Test
    void testCreateDonor_CreatesValidDonor_WhenValidDonorGiven() throws DonorServiceException {
        Donor donor = new Donor();
        donor.setFirstname("John");
        donor.setLastname("Doe");

        doNothing().when(donorValidation).validateCreateUpdateDonor(donor);
        when(donorRepository.save(donor)).thenReturn(donor);

        Donor result = donorService.createDonor(donor);

        assertEquals(donor, result);

        verify(donorRepository, times(1)).save(donor);
        verify(donorValidation, times(1)).validateCreateUpdateDonor(donor);
    }

    @Test
    void testCreateDonor_ThrowsDonorServiceException_WhenInvalidDonorGiven() throws DonorServiceException {
        Donor donor = new Donor(); // Invalid donor with missing fields

        doThrow(DonorServiceException.class).when(donorValidation).validateCreateUpdateDonor(donor);

        assertThrows(DonorServiceException.class, () -> donorService.createDonor(donor));

        verify(donorRepository, times(0)).save(donor);
        verify(donorValidation, times(1)).validateCreateUpdateDonor(donor);
    }

    @Test
    void testUpdateDonorById_UpdatesExistingDonor_WhenValidDonorAndExistingIdGiven() throws DonorServiceException {
        int donorId = 1;
        Donor existingDonor = new Donor();
        existingDonor.setId((long) donorId);
        String updatedFirstName = "Jane";
        String updatedLastName = "Smith";
        Donor updatedDonor = new Donor();
        updatedDonor.setFirstname(updatedFirstName);
        updatedDonor.setLastname(updatedLastName);

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.of(existingDonor));
        doNothing().when(donorValidation).validateCreateUpdateDonor(updatedDonor);
        when(donorRepository.save(existingDonor)).thenReturn(existingDonor);

        Donor result = donorService.updateDonorById(donorId, updatedDonor);

        assertEquals(existingDonor, result);
        assertEquals(updatedFirstName, existingDonor.getFirstname());
        assertEquals(updatedLastName, existingDonor.getLastname());

        verify(donorRepository, times(1)).save(existingDonor);
        verify(donorValidation, times(1)).validateCreateUpdateDonor(updatedDonor);
    }

    @Test
    void testUpdateDonorById_ThrowsDonorServiceException_WhenNonExistingIdGiven() throws DonorServiceException {
        int donorId = 1;
        Donor updatedDonor = new Donor();

        when(donorRepository.findById((long) donorId)).thenReturn(Optional.empty());

        assertThrows(DonorServiceException.class, () -> donorService.updateDonorById(donorId, updatedDonor));

        verify(donorRepository, times(0)).save(any(Donor.class));
        verify(donorValidation, times(0)).validateCreateUpdateDonor(any(Donor.class));
    }
}
