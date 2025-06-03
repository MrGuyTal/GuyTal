package com.cloudservicelogparser.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CloudServiceResolutionMap {
    Map<String, String> serviceNameToDomainMapping;
    Set<String> domainsToResolve;
    // mapping from domain to resolved IP address
    Map<String, String> resolvedDomains;

    public CloudServiceResolutionMap() {
        this.serviceNameToDomainMapping = new HashMap<>();
        this.domainsToResolve = new HashSet<>();
        this.resolvedDomains = new HashMap<>();
    }

    public void addCloudServiceEntry(String serviceName, String domain) {
        serviceNameToDomainMapping.put(serviceName, domain);
        domainsToResolve.add(domain);
    }

    public boolean shouldResolve(String domain) {
        return domainsToResolve.contains(domain);
    }

    public void resolveDomain(String domain, String ip) {
        // add extra validation (maybe not needed, but just in case)
        if (resolvedDomains.containsKey(domain) && !resolvedDomains.get(domain).equals(ip)) {
            throw new IllegalArgumentException("Domain " + domain + " already resolved to a different IP: " + resolvedDomains.get(domain) + ", cannot resolve to " + ip);
        }
        resolvedDomains.put(domain, ip);
        domainsToResolve.remove(domain); // remove from domains to resolve, so we don't need to resolve it again
    }

    public Map<String, String> getResolvedDomains() {
        return resolvedDomains;
    }
}
