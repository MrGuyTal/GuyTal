package com.cloudservicelogparser.filter;

import java.net.InetAddress;
import java.net.UnknownHostException;

// Represents an IPv4 range
public class IpRange {
    private final int base; // The base IP address as an integer
    private final int mask; // The subnet mask as an integer

    // build an IpRange from a CIDR string (e.g., "192.168.1.0/24").
    public IpRange(String cidr) {
        String[] parts = cidr.split("/");
        base = ipToInt(parts[0]); // Convert base IP to integer
        int prefix = Integer.parseInt(parts[1]); // Get prefix length
        mask = ~((1 << (32 - prefix)) - 1); // Calculate subnet mask
    }

    // Checks if the given IP address is within this range.
    public boolean contains(String ip) {
        int ipInt = ipToInt(ip);
        return (ipInt & mask) == (base & mask);
    }

    // Converts an IPv4 address string to its integer representation.
    private int ipToInt(String ip) {
        try {
            byte[] bytes = InetAddress.getByName(ip).getAddress();
            int val = 0;
            for (byte b : bytes) val = (val << 8) | (b & 0xFF);
            return val;
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IP: " + ip);
        }
    }
}