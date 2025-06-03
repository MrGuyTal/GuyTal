package com.cloudservicelogparser.orchestrator;

import com.cloudservicelogparser.model.*;
import com.cloudservicelogparser.parser.*;
import com.cloudservicelogparser.filter.*;
import com.cloudservicelogparser.resolver.*;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO file paths should be configurable via command line arguments or a config file
        File dbFile = new File("/Users/guyta/Dev/Dev/GuyTal/CloudServiceLogParser/ServiceDBv1.csv");
        File logFile = new File("/Users/guyta/Dev/Dev/GuyTal/CloudServiceLogParser/firewall.log");

        // Example filters
        // TODO filters should be configurable via command line arguments or a config file
        List<IpRange> includeRanges = Arrays.asList(new IpRange("11.11.11.0/24"));
        List<IpRange> excludeRanges = Arrays.asList(new IpRange("11.11.12.0/24"));
        IpFilter ipFilter = new IpFilter(includeRanges, excludeRanges);

        // TODO filters should be configurable via command line arguments or a config file
        List<Pattern> userIncludes = Collections.emptyList();
        List<Pattern> userExcludes = Arrays.asList(Pattern.compile("^sys.*"));
        UserFilter userFilter = new UserFilter(userIncludes, userExcludes);

        FilterChain filters = new FilterChain(ipFilter, userFilter);
        ReverseDnsResolver resolver = new ReverseDnsResolver();

        CloudServiceResolutionMap cloudServiceResolutionMap = new CloudServiceLogParser().parse(dbFile);

        cloudServiceResolutionMap = new FirewallLogParser(resolver, filters, cloudServiceResolutionMap).parse(logFile);

        Map<String, String> resolvedDomains = cloudServiceResolutionMap.getResolvedDomains();
        System.out.println("Resolved Domains:");
        for (String domain : cloudServiceResolutionMap.getResolvedDomains().keySet()) {
            System.out.println(domain + ": " + resolvedDomains.get(domain));
        }

        // Checkpoint 4: Concurrency
        // TODO work in progress, did not have enough time to fully test it, but seems to be working fine based on the output
        cloudServiceResolutionMap = new ConcurrentFirewallLogParser(resolver, filters, cloudServiceResolutionMap).parse(logFile);
        resolvedDomains = cloudServiceResolutionMap.getResolvedDomains();
        System.out.println();
        System.out.println("Resolved Domains concurrently:");
        for (String domain : cloudServiceResolutionMap.getResolvedDomains().keySet()) {
            System.out.println(domain + ": " + resolvedDomains.get(domain));
        }
    }
}