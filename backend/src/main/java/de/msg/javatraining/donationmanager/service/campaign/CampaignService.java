package de.msg.javatraining.donationmanager.service.campaign;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.validation.CampaignValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService implements ICampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignValidation campaignValidation;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Campaign> findAll() throws CampaignServiceException {
        List<Campaign> campaigns = campaignRepository.findAll();
        if (campaigns.isEmpty()) {
            throw new CampaignServiceException("No campaigns found", "NO_CAMPAIGNS_FOUND");
        }
        return campaigns;
    }

    @Override
    public Campaign findById(Integer id) throws CampaignServiceException {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(Long.valueOf(id));
        if (optionalCampaign.isEmpty()) {
            throw new CampaignServiceException("Entity could not be found", "CAMPAIGN01");
        }
        return optionalCampaign.get();
    }

    @Override
    public void deleteById(Long id) throws CampaignServiceException {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if (optionalCampaign.isEmpty()) {
            throw new CampaignServiceException("Entity could not be found", "CAMPAIGN01");
        }

        Campaign campaign = optionalCampaign.get();

        campaignValidation.validateDeleteCampaign(campaign);

        campaignRepository.deleteById(id);
    }

    @Override
    public Campaign createCampaign(Campaign campaign) throws CampaignServiceException {
        Optional<Campaign> optionalCampaign = campaignRepository.findByName(campaign.getName());
        if (optionalCampaign.isPresent()) {
            throw new CampaignServiceException("Entity already exists", "CAMPAIGN01");
        }

        campaignValidation.validateCreateCampaign(campaign);

        return campaignRepository.save(campaign);
    }

    @Override
    public Campaign updateCampaignById(Integer id, Campaign updatedCampaign) throws CampaignServiceException {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(Long.valueOf(id));
        if (optionalCampaign.isPresent()) {
            Campaign existingCampaign = optionalCampaign.get();
            String updatedCampaignName = updatedCampaign.getName();

            campaignValidation.validateUpdateCampaign(existingCampaign, updatedCampaignName,
                    campaignRepository.findByName(updatedCampaignName));

            existingCampaign.setName(updatedCampaignName);
            existingCampaign.setPurpose(updatedCampaign.getPurpose());
            return campaignRepository.save(existingCampaign);
        }
        throw new CampaignServiceException("Entity could not be found", "CAMPAIGN01");
    }
}

