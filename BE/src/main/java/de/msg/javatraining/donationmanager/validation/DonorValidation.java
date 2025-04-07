package de.msg.javatraining.donationmanager.validation;

import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.service.donor.DonorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DonorValidation {

    public void validateCreateUpdateDonor(Donor donor) throws DonorServiceException {
        if (donor.getFirstname() == null || donor.getFirstname().isEmpty() ||
                donor.getLastname() == null || donor.getLastname().isEmpty()) {
            throw new DonorServiceException("Firstname and lastname are required", "DONOR01");
        }
    }
}

