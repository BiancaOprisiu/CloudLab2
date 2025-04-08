package de.msg.javatraining.donationmanager.service.permision;

import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.repository.PermisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisionService implements IPermisionService {
    @Autowired
    private PermisionRepository permisionRepository;

    @Override
    public List<Permision> findAll() throws PermisionServiceException {
        List<Permision> permisions = permisionRepository.findAll();
        if (permisions.isEmpty()) {
            throw new PermisionServiceException("No permission found", "NO_PERMISSION_FOUND");
        }
        return permisions;
    }
}
