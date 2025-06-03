package com.cloudservicelogparser.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FirewallLogEntry {
    private final String srcIp;
    private final String dstIp;
    private final String domain;
    private final String user;
    // TODO currently not required, but can be used for additional fields in the future
    private final Map<String, String> optionalFields;

    public FirewallLogEntry(String srcIp, String dstIp, String domain, String user, Map<String, String> optionalFields) {
        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.domain = domain;
        this.user = user;
        this.optionalFields = optionalFields != null ? new HashMap<>(optionalFields) : new HashMap<>();
    }

    public String getSrcIp() { return srcIp; }
    public String getDstIp() { return dstIp; }
    public String getDomain() { return domain; }
    public String getUser() { return user; }
    public Map<String, String> getOptionalFields() { return Collections.unmodifiableMap(optionalFields); }
}