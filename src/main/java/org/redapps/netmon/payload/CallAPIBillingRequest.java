package org.redapps.netmon.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CallAPIBillingRequest {

    @JsonProperty("Gateway")
    private String Gateway;
    @JsonProperty("TerminalID")
    private int TerminalID;
    @JsonProperty("Username")
    private String Username;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("Function")
    private String Function;
    @JsonProperty("ID2")
    private int ID2;
    @JsonProperty("NC")
    private String NC;
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Family")
    private String Family;
    @JsonProperty("Tel")
    private String Tel;
    @JsonProperty("Mobile")
    private String Mobile;
    @JsonProperty("EMail")
    private String EMail;
    @JsonProperty("Amount")
    private long Amount;
    @JsonProperty("Memo")
    private String Memo;
    @JsonProperty("CallbackURL")
    private String CallbackURL;

    public CallAPIBillingRequest() {
    }

    public CallAPIBillingRequest(String Gateway, int terminalID, String username, String password,
                             String function, int ID2, String NC, String name, String family,
                             String tel, String mobile, String EMail, long amount, String memo,
                             String callbackURL) {
        this.Gateway = Gateway;
        this.TerminalID = terminalID;
        this.Username = username;
        this.Password = password;
        this.Function = function;
        this.ID2 = ID2;
        this.NC = NC;
        this.Name = name;
        this.Family = family;
        this.Tel = tel;
        this.Mobile = mobile;
        this.EMail = EMail;
        this.Amount = amount;
        this.Memo = memo;
        this.CallbackURL = callbackURL;
    }

    public String getGateway() {
        return this.Gateway;
    }

    public void setGateway(String Gateway) {
        this.Gateway = Gateway;
    }

    public int getTerminalID() {
        return this.TerminalID;
    }

    public void setTerminalID(int terminalID) {
        this.TerminalID = terminalID;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getFunction() {
        return this.Function;
    }

    public void setFunction(String function) {
        this.Function = function;
    }

    public int getID2() {
        return this.ID2;
    }

    public void setID2(int ID2) {
        this.ID2 = ID2;
    }

    public String getNC() {
        return this.NC;
    }

    public void setNC(String NC) {
        this.NC = NC;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getFamily() {
        return this.Family;
    }

    public void setFamily(String family) {
        this.Family = family;
    }

    public String getTel() {
        return this.Tel;
    }

    public void setTel(String tel) {
        this.Tel = tel;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getEMail() {
        return this.EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public long getAmount() {
        return this.Amount;
    }

    public void setAmount(long amount) {
        this.Amount = amount;
    }

    public String getMemo() {
        return this.Memo;
    }

    public void setMemo(String memo) {
        this.Memo = memo;
    }

    public String getCallbackURL() {
        return this.CallbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.CallbackURL = callbackURL;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
