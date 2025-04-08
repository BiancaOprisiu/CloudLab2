package de.msg.javatraining.donationmanager.service.donor;

import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.persistence.repository.DonorRepository;
import de.msg.javatraining.donationmanager.validation.DonorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService implements IDonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonorValidation donorValidation;

    public List<Donor> findAll() throws DonorServiceException {
        List<Donor> donors = donorRepository.findAll();
        if (donors.isEmpty()) {
            throw new DonorServiceException("No donors found", "NO_DONORS_FOUND");
        }
        return donors;
    }

    public Donor findById(Integer id) throws DonorServiceException {
        Optional<Donor> optionalDonor = donorRepository.findById(Long.valueOf(id));
        if (optionalDonor.isEmpty()) {
            throw new DonorServiceException("Donor not found", "DONOR_NOT_FOUND");
        }
        return optionalDonor.get();
    }

    public void deleteById(Integer id) throws DonorServiceException {
        Optional<Donor> optionalDonor = donorRepository.findById(Long.valueOf(id));
        if (optionalDonor.isEmpty()) {
            throw new DonorServiceException("Donor not found", "DONOR_NOT_FOUND");
        }

        Donor donor = optionalDonor.get();

        if (!donor.getDonationList().isEmpty()) {
            donor.setFirstname("Unknown");
            donor.setLastname("Unknown");
            donor.setAdditionalName("Unknown");
            donor.setMaidenName("Unknown");
            donorRepository.save(donor); // Save the changes to the database
        } else {
            donorRepository.deleteById(Long.valueOf(id));
        }
    }

    public Donor createDonor(Donor donor) throws DonorServiceException {
        donorValidation.validateCreateUpdateDonor(donor);
        return donorRepository.save(donor);
    }

    public Donor updateDonorById(Integer id, Donor updatedDonor) throws DonorServiceException {
        Optional<Donor> optionalDonor = donorRepository.findById(Long.valueOf(id));
        if (optionalDonor.isPresent()) {
            Donor donor = optionalDonor.get();
            donorValidation.validateCreateUpdateDonor(updatedDonor);

            donor.setFirstname(updatedDonor.getFirstname());
            donor.setLastname(updatedDonor.getLastname());
            donor.setAdditionalName(updatedDonor.getAdditionalName());
            donor.setMaidenName(updatedDonor.getMaidenName());
            donor = donorRepository.save(donor);
            return donor;
        }
        throw new DonorServiceException("Donor not found", "DONOR_NOT_FOUND");
    }
}
