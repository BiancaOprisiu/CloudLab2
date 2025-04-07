package de.msg.javatraining.donationmanager.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign_id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User createdBy;

    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "benefactor")
    private Donor benefactor;

    private Boolean approved;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approvedBy;

    private LocalDate approveDate;

    private String notes;
}
