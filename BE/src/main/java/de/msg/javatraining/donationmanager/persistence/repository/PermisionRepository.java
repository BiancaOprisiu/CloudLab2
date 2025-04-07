package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.PRole;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermisionRepository extends JpaRepository<Permision, Long> {
    Permision findByName(PRole name);
}
