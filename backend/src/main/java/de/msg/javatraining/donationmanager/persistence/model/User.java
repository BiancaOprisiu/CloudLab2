package de.msg.javatraining.donationmanager.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CollectionId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(	name = "user",
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String firstname;

	@Column(nullable = false)
	private String lastname;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String mobilenumber;

	@Column(nullable = false)
	private boolean isActive=true;

	@Column(nullable = false)
	private int consecutiveUnsuccessfulAttempts=0;

	@Column(nullable = false)
	private int loginCount=0;

	@Column(nullable = false)
	private boolean passwordChangeRequired=false;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_role",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_campaign",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "campaign_id"))
	private Set<Campaign> campaigns = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	@JsonIgnore
	private Set<Notification> notifications;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "createdBy")
	@JsonIgnore
	private List<Donation> createdDonations;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "approvedBy")
	@JsonIgnore
	private List<Donation> approvedDonations;
}
