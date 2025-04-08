package de.msg.javatraining.donationmanager.service.permision;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.service.donation.DonationServiceException;

import java.util.List;


public interface IPermisionService {

    List<Permision> findAll() throws DonationServiceException, PermisionServiceException;

}
