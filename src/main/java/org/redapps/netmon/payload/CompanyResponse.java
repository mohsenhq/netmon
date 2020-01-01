package org.redapps.netmon.payload;

import org.redapps.netmon.util.NetmonTypes;

public class CompanyResponse {

    private Long id;
    private String name;
    private NetmonTypes.COMPANY_TYPES type;
    private String nationalID;
    private String financialID;
    private String registrationID;
    private String telNumber;

    public CompanyResponse() {
    }

    public CompanyResponse(Long id, String name, NetmonTypes.COMPANY_TYPES type, String nationalID, String financialID,
                           String registrationID, String telNumber) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.nationalID = nationalID;
        this.financialID = financialID;
        this.registrationID = registrationID;
        this.telNumber = telNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
