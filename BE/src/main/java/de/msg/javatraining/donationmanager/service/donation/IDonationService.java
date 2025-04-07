package de.msg.javatraining.donationmanager.service.donation;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;

import java.util.List;

/**
 * This interface defines the operations that can be performed on donations.
 */
public interface IDonationService {

    /**
     * Retrieves a list of all donations.
     *
     * @return A list of all donations.
     * @throws DonationServiceException If an error occurs while retrieving donations.
     */
    List<Donation> findAll() throws DonationServiceException;

    /**
     * Retrieves a donation by its ID.
     *
     * @param id The ID of the donation to retrieve.
     * @return The retrieved donation.
     * @throws DonationServiceException If an error occurs while retrieving the donation or if the donation is not found.
     */
    Donation findById(Long id) throws DonationServiceException;

    /**
     * Deletes a donation by its ID.
     *
     * @param id The ID of the donation to delete.
     * @throws DonationServiceException If an error occurs while deleting the donation or if the donation is not found.
     */
    void deleteById(Long id) throws DonationServiceException;

    /**
     * Creates a new donation.
     *
     * @param donation The donation to create.
     * @return The created donation.
     * @throws DonationServiceException If an error occurs while creating the donation, or if the donation amount, currency,
     *                                 campaign, createdBy, createdDate, or benefactor is null.
     */
    Donation createDonation(Donation donation) throws DonationServiceException;

    /**
     * Updates a donation by its ID.
     *
     * @param id             The ID of the donation to update.
     * @param updatedDonation The updated donation data.
     * @return The updated donation.
     * @throws DonationServiceException If an error occurs while updating the donation, if the donation is not found, or if
     *                                 the updated donation is invalid based on certain validation rules.
     */
    Donation updateDonation(Long id, Donation updatedDonation) throws DonationServiceException;

    /**
     * Approves a donation by its ID.
     *
     * @param id            The ID of the donation to approve.
     * @param approvingUser The user approving the donation.
     * @throws DonationServiceException If an error occurs while approving the donation, if the donation is not found, or if
     *                                 the approving user is the same as the creating user.
     */
    void approveDonation(Long id, Long approvingUser) throws DonationServiceException;
}

