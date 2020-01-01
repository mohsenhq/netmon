package org.redapps.netmon.payload;

public class TechnicalPersonResponse {

    private Long id;
    private String name;
    private String email;
    private String telNumber;
    private String mobile;
    private long nationalId;

    public TechnicalPersonResponse(Long id, String name, String email, String telNumber,
                                   String mobile, long nationalId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telNumber = telNumber;
        this.mobile = mobile;
        this.nationalId = nationalId;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getNationalId() {
        return nationalId;
    }

    public void setNationalId(long nationalId) {
        this.nationalId = nationalId;
    }
}
