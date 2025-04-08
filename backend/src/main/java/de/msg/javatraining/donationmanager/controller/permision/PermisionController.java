package de.msg.javatraining.donationmanager.controller.permision;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.service.permision.PermisionService;
import de.msg.javatraining.donationmanager.service.permision.PermisionServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PermisionController {

    @Autowired
    private PermisionService permisionService;

    @GetMapping("/permisions")
    public ResponseEntity<?> findAll() {
        try {
            List<Permision> permisions = permisionService.findAll();
            return ResponseEntity.ok(permisions);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
