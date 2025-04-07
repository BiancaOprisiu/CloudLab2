package de.msg.javatraining.donationmanager.service.donor;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donor;

import java.util.List;

/**
 * This interface defines the operations that can be performed on donors.
 */
public interface IDonorService {

    /**
     * Retrieves a list of all donors.
     *
     * @return A list of all donors.
     * @throws DonorServiceException If an error occurs while retrieving donors or if no donors are found.
     */
    List<Donor> findAll() throws DonorServiceException;

    /**
     * Retrieves a donor by its ID.
     *
     * @param id The ID of the donor to retrieve.
     * @return The retrieved donor.
     * @throws DonorServiceException If an error occurs while retrieving the donor or if the donor is not found.
     */
    Donor findById(Integer id) throws DonorServiceException;

    /**
     * Deletes a donor by its ID.
     * <p>
     * If the donor has associated donations, the donor's name, firstname, additional name, and maiden name will be set to "Unknown".
     * </p>
     * @param id The ID of the donor to delete.
     * @throws DonorServiceException If an error occurs while deleting the donor or if the donor is not found.
     */
    void deleteById(Integer id) throws DonorServiceException;

    /**
     * Creates a new donor.
     * @param donor The donor to create.
     * @return The created donor.
     * @throws DonorServiceException If an error occurs while creating the donor, Firstname or Lastname are missing
     */
    Donor createDonor(Donor donor) throws DonorServiceException;

    /**
     * Updates a donor by its ID.
     * @param id           The ID of the donor to update.
     * @param updatedDonor The updated donor data.
     * @return The updated donor.
     * @throws DonorServiceException If an error occurs while updating the donor or if the donor is not found, the updated donor leads to duplicate entities
     */
    Donor updateDonorById(Integer id, Donor updatedDonor) throws DonorServiceException;
}


