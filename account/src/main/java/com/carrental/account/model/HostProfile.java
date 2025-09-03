package com.carrental.account.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(
        name = "host_profiles",
        uniqueConstraints = @UniqueConstraint(name="uk_host_profiles_user", columnNames="user_id")
)
public class HostProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Account user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HostStatus status = HostStatus.PENDING;

    // Public profile & preferences
    @Column(length = 60) private String displayName;
    @Column(length = 1000) private String about;

    // Profile default template
    @Column(length = 1000) private String defaultPickupInstructions;

    // Host Rating
    private BigDecimal ratingAverage;
    private Integer ratingCount;
    private Integer tripsHosted;
    private Integer responseRatePercent;
    private Integer responseTimeHours;

    @Column(length = 200) private String suspensionReason;

    @CreationTimestamp @Column(nullable=false, updatable=false) private Instant createdAt;
    @UpdateTimestamp @Column(nullable=false)                   private Instant updatedAt;


    // getters
    public Long getId() {return id;}
    public Account getUser() {return user;}
    public HostStatus getStatus() {return status;}
    public String getDisplayName() {return displayName;}
    public String getAbout() {return about;}
    public String getDefaultPickupInstructions() {return defaultPickupInstructions;}
    public BigDecimal getRatingAverage() {return ratingAverage;}
    public Integer getRatingCount() {return ratingCount;}
    public Integer getTripsHosted() {return tripsHosted;}
    public Integer getResponseRatePercent() {return responseRatePercent;}
    public Integer getResponseTimeHours() {return responseTimeHours;}
    public String getSuspensionReason() {return suspensionReason;}

    // setters
    public void setUser(Account account) {this.user = account;}
    public void setStatus(HostStatus status) { this.status = status; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setAbout(String about) { this.about = about; }
    public void setDefaultPickupInstructions(String defaultPickupInstructions) {this.defaultPickupInstructions = defaultPickupInstructions;}
    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }
    public void setTripsHosted(Integer tripsHosted) { this.tripsHosted = tripsHosted; }
    public void setResponseRatePercent(Integer responseRatePercent) { this.responseRatePercent = responseRatePercent; }
    public void setResponseTimeHours(Integer responseTimeHours) { this.responseTimeHours = responseTimeHours; }
    public void setSuspensionReason(String suspensionReason) { this.suspensionReason = suspensionReason; }

}
