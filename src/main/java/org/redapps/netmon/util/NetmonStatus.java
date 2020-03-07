package org.redapps.netmon.util;

public class NetmonStatus {

    public enum BillingStatus{
        CREATED,
        PAID,
        VOID
    }

    public enum ServiceStatus {
        REQUEST_FOR_NEW_SERVICE,
        ACCEPTED_BY_MANAGER,
        REJECTED_BY_MANAGER,
        ACCEPTED_BY_CUSTOMER,
        REJECTED_BY_CUSTOMER,
        RUNNING,
        DISABLE,
        REQUEST_FOR_RENEW_SERVICE
    }

    public enum DEVICE_STATUS{
        INIT,
        ACTIVE,
        DISABLED,
        REMOVED
    }

    public enum LOG_STATUS{
        SUCCESS,
        FAILED
    }
}
