package de.msg.javatraining.donationmanager.controller.campaign;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.service.campaign.CampaignService;
import de.msg.javatraining.donationmanager.service.campaign.CampaignServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/campaigns")
    @PreAuthorize("hasAuthority('CAMP_REPORTING')")
    public ResponseEntity<?> findAll() {
        try{
            List<Campaign> campaigns = campaignService.findAll();
            return ResponseEntity.ok(campaigns);
        } catch (CampaignServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/campaigns/{id}")
    @PreAuthorize("hasAuthority('CAMP_REPORT_RESTRICTED')")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Campaign campaign = campaignService.findById(id);
            return ResponseEntity.ok(campaign);
        } catch (CampaignServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/campaigns/{id}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        try {
            campaignService.deleteById(Long.valueOf(id));
            return ResponseEntity.ok("OK");
        } catch (CampaignServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/campaigns")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT')")
    public ResponseEntity<?> createCampaign(@RequestBody Campaign campaign) {
        try {
            Campaign createdCampaign = campaignService.createCampaign(campaign);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
        } catch (CampaignServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/campaigns/{id}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT')")
    public ResponseEntity<?> updateCampaignById(@PathVariable Integer id, @RequestBody Campaign updatedCampaign) {
        try {
            Campaign updated = campaignService.updateCampaignById(id, updatedCampaign);
            return ResponseEntity.ok(updated);
        } catch (CampaignServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
