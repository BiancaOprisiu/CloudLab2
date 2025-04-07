package de.msg.javatraining.donationmanager.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "campaign")
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String purpose;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "campaign_donor",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "donor_id")
    )
    private List<Donor> donors = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "campaign_id"
    )
    @JsonIgnore
    private List<Donation> donationList = new ArrayList<>();
}

