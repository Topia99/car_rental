package com.carrental.account.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@org.hibernate.annotations.DynamicUpdate
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50) private String username;
    @Column(nullable = false, unique=true) private String email;


    @Column(nullable = false, length=100) private String passwordHash;

    @Embedded private Address address;
    @Embedded private ContactInfo contact;
    @Embedded private LicenseInfo license;


    // User Role (GUEST / HOST)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_user_roles_user_role", columnNames = {"user_id", "role"}
            )
    )
    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();



    @CreationTimestamp @Column(nullable=false, updatable=false) private Instant createdAt;
    @UpdateTimestamp @Column(nullable = false) private Instant updatedAt;

    public Account(){}

    // getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Address getAddress() { return address; }
    public ContactInfo getContact() { return contact; }
    public LicenseInfo getLicense() { return license; }

    // setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setAddress(Address address) { this.address = address; }
    public void setContact(ContactInfo contact) { this.contact = contact; }
    public void setLicense(LicenseInfo license) { this.license = license; }


    // Getters & setters for roles
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public void addRoles(Role r) { roles.add(r); }
    public boolean hashRole(Role r) { return roles.contains(r); }

}

