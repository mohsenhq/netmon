package org.redapps.netmon.model;
import org.redapps.netmon.model.audit.UserDateAudit;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.NaturalId;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.redapps.netmon.util.NetmonTypes;

@Entity
@Table(name = "company")
public class Company extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotBlank
    @Size(max = 140)
    private String name;

    private NetmonTypes.COMPANY_TYPES type; // UNIVERSITY, PERSONAL, COMPANY
    private String nationalID;
    private String financialID;
    private String registrationID;
    private String telNumber;

    @Type(type="yes_no")
    private boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Company(String name, String nationalID, String financialID,
                   String registrationID, String telNumber, NetmonTypes.COMPANY_TYPES type, User user) {
        this.name = name;
        this.type = type;
        this.nationalID = nationalID;
        this.financialID = financialID;
        this.registrationID = registrationID;
        this.telNumber = telNumber;
        this.active = false;
        this.user = user;
    }

    public Company()
    {

    }

    public NetmonTypes.COMPANY_TYPES getType() {
        return type;
    }

    public void setType(NetmonTypes.COMPANY_TYPES type) {
        this.type = type;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getFinancialID() {
        return financialID;
    }

    public void setFinancialID(String financialID) {
        this.financialID = financialID;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String question) {
        this.name = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
