package de.msg.javatraining.donationmanager.controller.donation;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.service.donation.DonationService;
import de.msg.javatraining.donationmanager.service.donation.DonationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DonationController
{
    @Autowired
    private DonationService donationService;

    @GetMapping("/donation")
    public ResponseEntity<?> findAll() {
        try {
            List<Donation> donations = donationService.findAll();
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/donation/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Donation foundDonation = donationService.findById(id);
            return ResponseEntity.ok(foundDonation);
        } catch (DonationServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/donation/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            donationService.deleteById(id);
            return ResponseEntity.ok("OK");
        } catch (DonationServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/donation")
    public ResponseEntity<?> createDonation(@RequestBody Donation donation) {
        try {
            Donation createdDonation = donationService.createDonation(donation);
            return new ResponseEntity<>(createdDonation, HttpStatus.CREATED);
        } catch (DonationServiceException e) {
            if ("AMOUNT_MISSING".equals(e.getErrorCode())) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/donation/{id}")
    public ResponseEntity<?> updateDonationById(@PathVariable Long id, @RequestBody Donation updatedDonation) {
        try {
            Donation updatedDonationResult = donationService.updateDonation(id, updatedDonation);
            return ResponseEntity.ok(updatedDonationResult);
        } catch (DonationServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/donation/{id}/approve/{approvingUser}")
    public ResponseEntity<?> approveDonation(@PathVariable Long id, @PathVariable Long approvingUser) {
        try {
            donationService.approveDonation(id, approvingUser);
            return ResponseEntity.ok("Donation approved successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
