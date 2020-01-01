package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UpdateBillingReferenceIdResponse {

    private int Result;
    private String Message;
    private long ReferenceId;

    public UpdateBillingReferenceIdResponse() {
    }

    public UpdateBillingReferenceIdResponse(int result, String message, long ReferenceId) {
        this.Result = result;
        this.Message = message;
        this.ReferenceId = ReferenceId;
    }

    public int getResult() {
        return this.Result;
    }

    public void setResult(int result) {
        this.Result = result;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public long getReferenceId() {
        return this.ReferenceId;
    }

    public void setReferenceId(long ReferenceId) {
        this.ReferenceId = ReferenceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
