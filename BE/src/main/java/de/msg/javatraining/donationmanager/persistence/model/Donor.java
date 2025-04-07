package de.msg.javatraining.donationmanager.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "donor")
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column
    private String additionalName;

    @Column
    private String maidenName;

    @ManyToMany(mappedBy = "donors")
    @JsonIgnore
    private List<Campaign> campaigns = new ArrayList<>();


    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "benefactor"
    )
    @JsonIgnore
    private List<Donation> donationList = new ArrayList<>();
}

