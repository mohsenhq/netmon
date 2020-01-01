package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.redapps.netmon.util.NetmonTypes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompanyRequest {
    @NotBlank
    @Size(max = 140)
    private String name;

    private NetmonTypes.COMPANY_TYPES type;
    private String nationalID;
    private String financialID;
    private String registrationID;
    private String telNumber;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

    public String getName() {
        return name;
    }

    public void setName(String question) {
        this.name = question;
    }
}
