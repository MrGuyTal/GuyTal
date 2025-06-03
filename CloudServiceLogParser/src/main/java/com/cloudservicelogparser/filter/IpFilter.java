package com.cloudservicelogparser.filter;

import java.util.*;

public class IpFilter {
    private final List<IpRange> includes;
    private final List<IpRange> excludes;

    public IpFilter(List<IpRange> includes, List<IpRange> excludes) {
        this.includes = includes;
        this.excludes = excludes;
    }

    public boolean isAllowed(String ip) {
        boolean included = includes.isEmpty() || includes.stream().anyMatch(r -> r.contains(ip));
        boolean excluded = excludes.stream().anyMatch(r -> r.contains(ip));
        return included && !excluded;
    }
}