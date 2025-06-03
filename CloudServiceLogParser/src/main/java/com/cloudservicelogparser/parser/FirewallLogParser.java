package com.cloudservicelogparser.parser;

import com.cloudservicelogparser.model.CloudServiceResolutionMap;
import com.cloudservicelogparser.model.FirewallLogEntry;
import com.cloudservicelogparser.parser.FirewallLogFields;
import com.cloudservicelogparser.resolver.ReverseDnsResolver;
import com.cloudservicelogparser.filter.FilterChain;
import com.cloudservicelogparser.filter.IpFilter;
import com.cloudservicelogparser.filter.UserFilter;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FirewallLogParser {
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(\\w+)=([^\\s]+)");
    private final ReverseDnsResolver reverseDnsResolver;
    private final FilterChain filters;
    private CloudServiceResolutionMap cloudServiceResolutionMap;

    public FirewallLogParser(ReverseDnsResolver reverseDnsResolver, FilterChain filters, CloudServiceResolutionMap cloudServiceResolutionMap) {
        this.reverseDnsResolver = reverseDnsResolver;
        this.filters = filters;
        this.cloudServiceResolutionMap = cloudServiceResolutionMap;
    }

    public CloudServiceResolutionMap parse(File logFile) throws IOException {
        // no need to store in the memory after parsed
        // List<FirewallLogEntry> entries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            // read line by line, so we don't load the entire file into memory, as the requirement is to handle huge files
            while ((line = br.readLine()) != null) {
                Map<String, String> fields = parseLine(line);
                String srcIp = fields.get(FirewallLogFields.SRC);
                String dstIp = fields.get(FirewallLogFields.DST);

                // Requirement: Swap srcIp and dstIp if OUTG CONN is present in the line
                if (line.contains("OUTG CONN")) {
                    String temp = srcIp;
                    srcIp = dstIp;
                    dstIp = temp;
                }

                String domain = fields.getOrDefault(FirewallLogFields.DOMAIN, null); // DOMAIN may be null if not present (per requirement)
                String user = fields.get(FirewallLogFields.USER);
                // Remove known fields from optionalFields
                // TODO currently not required, but can be used for additional fields in the future
                Map<String, String> optionalFields = new HashMap<>(fields);
                optionalFields.remove(FirewallLogFields.SRC);
                optionalFields.remove(FirewallLogFields.DST);
                optionalFields.remove(FirewallLogFields.DOMAIN);
                optionalFields.remove(FirewallLogFields.USER);

                // Checkpoint 3: apply filters to filter out unwanted entries
                boolean isAllowed = (filters == null) || filters.isAllowed(user, srcIp, dstIp);
                if (!isAllowed) {
                    // Skip this line if it doesn't pass the filters, so it doesn't waste time on reverse DNS lookup
                    continue;
                }

                // Checkpoint 2: Reverse DNS lookup if domain is missing
                if (domain == null && dstIp != null) {
                    try {
                        domain = reverseDnsResolver.resolve(dstIp);
                    } catch (Exception e) {
                        System.err.println("Failed to resolve domain for IP " + dstIp + ": " + e.getMessage());
                    }
                }

                if (srcIp == null || dstIp == null) {
                    System.err.println("Invalid log entry (missing SRC or DST): " + line);
                    continue;
                }

                // add the resolved log entry to the cloud service resolution map
                if (cloudServiceResolutionMap.shouldResolve(domain)) {
                    cloudServiceResolutionMap.resolveDomain(domain, srcIp);
                }
            }
        }
        return cloudServiceResolutionMap;
    }

    private Map<String, String> parseLine(String line) {
        Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
        Map<String, String> fields = new HashMap<>();
        while (matcher.find()) {
            fields.put(matcher.group(1), matcher.group(2));
        }
        return fields;
    }
}