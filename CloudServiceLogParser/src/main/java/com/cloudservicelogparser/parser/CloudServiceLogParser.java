package com.cloudservicelogparser.parser;

import com.cloudservicelogparser.model.CloudServiceEntry;
import com.cloudservicelogparser.model.CloudServiceResolutionMap;

import java.io.*;
import java.util.*;

public class CloudServiceLogParser {
    // public List<CloudServiceEntry> parse(File csvFile) throws IOException {
    //     List<CloudServiceEntry> services = new ArrayList<>();
    //     try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
    //         br.readLine(); // skip header of csv
    //         // TODO add smark csv column parser, instead of skipping it
            
    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] parts = line.split(",");
    //             if (parts.length < 5) {
                    // System.err.println("Invalid line in CSV (less than 5 parts): "+line);continue;
    //             }    

    //        services.add(new CloudServiceEntry(
    //                 parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
    //                 parts[4].trim().equalsIgnoreCase("yes")
    //             ));
    //         }
    //     }
    //     return services;
    // }
    public CloudServiceResolutionMap parse(File csvFile) throws IOException {
        CloudServiceResolutionMap cloudServiceResolutionMap = new CloudServiceResolutionMap();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header of csv
            // TODO add smark csv column parser, instead of skipping it

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.err.println("Invalid line in CSV (less than 5 parts): " + line);
                    continue; // skip the invalid line (need to clarify the requirements, maybe should throw an exception instead and abort parsing)
                }
                String serviceName = parts[0].trim();
                String domain = parts[1].trim();
                cloudServiceResolutionMap.addCloudServiceEntry(serviceName, domain);
            }
        }
        return cloudServiceResolutionMap;
    }
}