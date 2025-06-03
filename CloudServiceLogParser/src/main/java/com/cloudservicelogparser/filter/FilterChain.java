package com.cloudservicelogparser.filter;

public class FilterChain {
    private final IpFilter ipFilter;
    private final UserFilter userFilter;

    public FilterChain(IpFilter ipFilter, UserFilter userFilter) {
        this.ipFilter = ipFilter;
        this.userFilter = userFilter;
    }

    public boolean isAllowed(String user, String srcIp, String dstIp) {
        boolean ipAllowed = (ipFilter == null) || ipFilter.isAllowed(srcIp) || ipFilter.isAllowed(dstIp);
        boolean userAllowed = (userFilter == null) || userFilter.isAllowed(user);
        return ipAllowed && userAllowed;
    }
}