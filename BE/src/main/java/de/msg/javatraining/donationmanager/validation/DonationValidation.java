package de.msg.javatraining.donationmanager.validation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonorRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.donation.DonationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DonationValidation {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRepository donationRepository;

    public void validateCreateDonation(Donation donation) throws DonationServiceException {
        if (donation.getAmount() == null) {
            throw new DonationServiceException("Amount cannot be null", "NULL_AMOUNT_ERROR");
        }

        if (donation.getCurrency() == null) {
            throw new DonationServiceException("Currency cannot be null", "NULL_CURRENCY_ERROR");
        }

        if (donation.getCampaign_id() == null) {
            throw new DonationServiceException("Campaign cannot be null", "NULL_CAMPAIGN_ERROR");
        } else {
            Optional<Campaign> campaignOptional = campaignRepository.findById(Long.valueOf(donation.getCampaign_id().getId()));
            if (campaignOptional.isEmpty()) {
                throw new DonationServiceException("Invalid campaign ID", "INVALID_CAMPAIGN");
            }
        }

        if (donation.getCreatedBy() == null) {
            throw new DonationServiceException("CreatedBy cannot be null", "NULL_CREATEDBY_ERROR");
        } else {
            Optional<User> userOptional = userRepository.findById(donation.getCreatedBy().getId());
            if (userOptional.isEmpty()) {
                throw new DonationServiceException("Invalid creator ID", "INVALID_CREATOR");
            }
        }

        if (donation.getCreatedDate() == null) {
            throw new DonationServiceException("CreatedDate cannot be null", "NULL_CREATEDDATE_ERROR");
        }

        if (donation.getBenefactor() == null) {
            throw new DonationServiceException("Benefactor cannot be null", "NULL_BENEFACTOR_ERROR");
        } else {
            Optional<Donor> donorOptional = donorRepository.findById(Long.valueOf(donation.getBenefactor().getId()));
            if (donorOptional.isEmpty()) {
                throw new DonationServiceException("Invalid benefactor ID", "INVALID_BENEFACTOR");
            }
        }
    }

    public void validateUpdateDonation(Long id) throws DonationServiceException {
        Optional<Donation> existingDonationOptional = donationRepository.findById(id);
        if(existingDonationOptional.isEmpty()) {
            throw new DonationServiceException("Donation cannot be null", "NULL_DONATION_ERROR");
        } else {
            Donation existingDonation = existingDonationOptional.get();
            if(existingDonation.getApproved()) {
                throw new DonationServiceException("Approved donations cannot be updated", "DONATION05");
            }
        }
    }
}
