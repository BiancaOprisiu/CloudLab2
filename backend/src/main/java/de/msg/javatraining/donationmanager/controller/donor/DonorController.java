package de.msg.javatraining.donationmanager.controller.donor;

import de.msg.javatraining.donationmanager.persistence.model.Donor;
import de.msg.javatraining.donationmanager.service.donor.DonorService;
import de.msg.javatraining.donationmanager.service.donor.DonorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DonorController {

    @Autowired
    private DonorService donorService;

    @GetMapping("/donors")
    public ResponseEntity<?> findAll() {
        try {
            List<Donor> donors = donorService.findAll();
            if (donors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(donors);
            } else {
                return ResponseEntity.ok(donors);
            }
        } catch (DonorServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/donors/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Donor donor = donorService.findById(id);
            return ResponseEntity.ok(donor);
        } catch (DonorServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/donors/{id}")
    @PreAuthorize("hasAuthority('BENEF_MANAGEMENT')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        try {
            donorService.deleteById(id);
            return ResponseEntity.ok("OK");
        } catch (DonorServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/donors")
    @PreAuthorize("hasAuthority('BENEF_MANAGEMENT')")
        public ResponseEntity<?> createDonor(@RequestBody Donor donor) {
        try {
            Donor createdDonor = donorService.createDonor(donor);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonor);
        } catch (DonorServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/donors/{id}")
    @PreAuthorize("hasAuthority('BENEF_MANAGEMENT')")
    public ResponseEntity<Donor> updateDonorById(@PathVariable Integer id, @RequestBody Donor updatedDonor) {
        try {
            Donor updated = donorService.updateDonorById(id, updatedDonor);
            return ResponseEntity.ok(updated);
        } catch (DonorServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
