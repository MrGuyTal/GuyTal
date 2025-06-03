package com.cloudservicelogparser.resolver;

import java.net.*;
import java.util.concurrent.*;

public class ReverseDnsResolver {
    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    public String resolve(String ip) {
        // first check if present in the local cache
        // if not - call the system DNS resolver and then store the result in the cache
        return cache.computeIfAbsent(ip, this::doReverseLookup);
    }

    private String doReverseLookup(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.getCanonicalHostName();
        } catch (Exception e) {
            return null;
        }
    }
}