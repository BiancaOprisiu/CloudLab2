package de.msg.javatraining.donationmanager.service.donation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.validation.DonationValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private DonationValidation donationValidation;

    @InjectMocks
    private DonationService donationService;

    @Test
    void givenDonationsExist_whenFindAll_thenReturnsListOfDonations() throws DonationServiceException {
        List<Donation> donationList = new ArrayList<>();
        donationList.add(new Donation());

        when(donationRepository.findAll()).thenReturn(donationList);

        List<Donation> result = donationService.findAll();

        assertThat(result, hasSize(1));
        assertThat(result, equalTo(donationList));
    }

    @Test
    void givenNoDonationsExist_whenFindAll_thenReturnsEmptyList() throws DonationServiceException {
        when(donationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Donation> result = donationService.findAll();

        assertThat(result, emptyIterable());
    }

    @Test
    void givenExistingDonationId_whenFindById_thenReturnsCorrectDonation() throws DonationServiceException {
        long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

        Donation result = donationService.findById(donationId);

        assertThat(result, equalTo(donation));
    }

    @Test
    void givenNonExistingDonationId_whenFindById_thenThrowsDonationServiceException() throws DonationServiceException {
        long donationId = 1L;

        when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

        assertThrows(DonationServiceException.class, () -> donationService.findById(donationId));
    }

    @Test
    void givenExistingDonationIdAndNotApproved_whenDeleteById_thenDeleted() throws DonationServiceException {
        long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setApproved(false);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

        donationService.deleteById(donationId);

        verify(donationRepository).deleteById(donationId);
    }

    @Test
    void givenExistingDonationIdAndApproved_whenDeleteById_thenThrowsDonationServiceException() {
        long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setApproved(true);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

        assertThrows(
                DonationServiceException.class,
                () -> donationService.deleteById(donationId),
                "Approved donations cannot be deleted"
        );
    }

    @Test
    void givenExistingDonationId_whenDeleteById_thenDeleted() throws DonationServiceException {
        long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setApproved(false);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

        donationService.deleteById(donationId);

        verify(donationRepository).deleteById(donationId);
    }

    @Test
    void givenNonExistingDonationId_whenDeleteById_thenThrowsDonationServiceException() {
        long nonExistingDonationId = 999L; // Assuming this ID doesn't exist

        when(donationRepository.findById(nonExistingDonationId)).thenReturn(Optional.empty());

        assertThrows(
                DonationServiceException.class,
                () -> donationService.deleteById(nonExistingDonationId),
                "Donation not found"
        );
    }

    @Test
    void givenAllAttributes_whenCreatingDonation_thenAllAttributesSet() {
        Campaign campaign = new Campaign();
        campaign.setId(1L);

        User creator = new User();
        creator.setId(101L);

        Donor donor = new Donor();
        donor.setId(201L);

        User approver = new User();
        approver.setId(301L);

        Donation donation = new Donation();
        donation.setId(1L);
        donation.setCampaign_id(campaign);
        donation.setAmount(100);
        donation.setCurrency("USD");
        donation.setCreatedBy(creator);
        donation.setCreatedDate(LocalDate.now());
        donation.setBenefactor(donor);
        donation.setApproved(true);
        donation.setApprovedBy(approver);
        donation.setApproveDate(LocalDate.now());
        donation.setNotes("Test notes");

        assertThat(donation.getId(), is(1L));
        assertThat(donation.getCampaign_id(), is(equalTo(campaign)));
        assertThat(donation.getAmount(), is(100));
        assertThat(donation.getCurrency(), is("USD"));
        assertThat(donation.getCreatedBy(), is(equalTo(creator)));
        assertThat(donation.getCreatedDate(), is(LocalDate.now()));
        assertThat(donation.getBenefactor(), is(equalTo(donor)));
        assertThat(donation.getApproved(), is(true));
        assertThat(donation.getApprovedBy(), is(equalTo(approver)));
        assertThat(donation.getApproveDate(), is(LocalDate.now()));
        assertThat(donation.getNotes(), is("Test notes"));
    }


    @Test
    void testUpdateDonation_ExistingId_ValidDonation() throws DonationServiceException {
        int donationId = 1;
        Donation existingDonation = new Donation();
        existingDonation.setId((long) donationId);
        existingDonation.setAmount(100);
        existingDonation.setCurrency("USD");
        existingDonation.setNotes("Existing notes");
        existingDonation.setBenefactor(new Donor());

        Donation updatedDonation = new Donation();
        updatedDonation.setAmount(200);
        updatedDonation.setCurrency("EUR");
        updatedDonation.setNotes("Updated notes");
        existingDonation.setBenefactor(new Donor());


        when(donationRepository.findById((long) donationId)).thenReturn(Optional.of(existingDonation));
        when(donationRepository.save(argThat(donation -> donation.getId() == 1))).thenReturn(updatedDonation);

        Donation result = donationService.updateDonation((long) donationId, updatedDonation);

        assertThat(result.getAmount(), is(equalTo(updatedDonation.getAmount())));
        assertThat(result.getCurrency(), is(equalTo(updatedDonation.getCurrency())));
        assertThat(result.getCreatedBy(), is(equalTo(updatedDonation.getCreatedBy())));
        assertThat(result.getCreatedDate(), is(equalTo(updatedDonation.getCreatedDate())));
        assertThat(result.getBenefactor(), is(equalTo(updatedDonation.getBenefactor())));
        assertThat(result.getApproved(), is(equalTo(updatedDonation.getApproved())));
        assertThat(result.getApproveDate(), is(equalTo(updatedDonation.getApproveDate())));
        assertThat(result.getNotes(), is(equalTo(updatedDonation.getNotes())));

        verify(donationRepository, times(1)).save(existingDonation);
    }
}
