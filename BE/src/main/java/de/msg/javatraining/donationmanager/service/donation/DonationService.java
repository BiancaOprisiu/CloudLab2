package de.msg.javatraining.donationmanager.service.donation;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.user.UserService;
import de.msg.javatraining.donationmanager.service.user.UserServiceException;
import de.msg.javatraining.donationmanager.validation.DonationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService implements IDonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationValidation donationValidation;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Donation> findAll() throws DonationServiceException {
        return donationRepository.findAll();
    }

    @Override
    public Donation findById(Long id) throws DonationServiceException {
        Optional<Donation> optionalDonation = donationRepository.findById(id);
        if (optionalDonation.isEmpty()) {
            throw new DonationServiceException("Entity could not be found", "DONATION01");
        }
        return optionalDonation.get();
    }

    @Override
    public void deleteById(Long id) throws DonationServiceException {
        Optional<Donation> optionalDonation = donationRepository.findById(id);
        if (optionalDonation.isPresent()) {
            Donation donation = optionalDonation.get();

            if (donation.getApproved()) {
                throw new DonationServiceException("Approved donations cannot be deleted", "DONATION04");
            } else {
                try {
                    donationRepository.deleteById(id);
                } catch (Exception e) {
                    throw new DonationServiceException("An error occurred while deleting donation", "DONATION05");
                }
            }
        } else {
            throw new DonationServiceException("Donation not found", "DONATION_NOT_FOUND");
        }
    }

    @Override
    public Donation createDonation(Donation donation) throws DonationServiceException {
        donationValidation.validateCreateDonation(donation); // Validate the donation data

        try {
            return donationRepository.save(donation);
        } catch (Exception e) {
            throw new DonationServiceException("Error creating donation", "CREATE_ERROR");
        }
    }

    @Override
    public Donation updateDonation(Long id, Donation updatedDonation) throws DonationServiceException {
        donationValidation.validateUpdateDonation(id);

        Optional<Donation> existingDonationOptional = donationRepository.findById(id);
        if (existingDonationOptional.isPresent()) {
            Donation existingDonation = existingDonationOptional.get();

            existingDonation.setAmount(updatedDonation.getAmount());
            existingDonation.setBenefactor(updatedDonation.getBenefactor());
            existingDonation.setCurrency(updatedDonation.getCurrency());
            existingDonation.setNotes(updatedDonation.getNotes());
            existingDonation.setCampaign_id(updatedDonation.getCampaign_id());

            donationRepository.save(existingDonation);

            return existingDonation;
        }
        throw new DonationServiceException("Donation not found", "DONATION_NOT_FOUND");
    }

    @Override
    public void approveDonation(Long id, Long approvingUser) throws DonationServiceException {
        Optional<Donation> optionalDonation = donationRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(approvingUser);
        if (optionalDonation.isPresent()) {
            Donation donation = optionalDonation.get();

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (!donation.getCreatedBy().getId().equals(user.getId())) {
                    donation.setApproved(true);
                    donation.setApproveDate(LocalDate.now());
                    donation.setApprovedBy(user);
                    donationRepository.save(donation);
                } else {
                    throw new DonationServiceException("The approving user cannot be the same as the creating user", "DONATION01");
                }

            } else {
                throw new DonationServiceException("User not found", "DONATION02");
            }
        } else {
            throw new DonationServiceException("Donation not found", "DONATION03");
        }
    }
}

