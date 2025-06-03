package com.cloudservicelogparser.model;

public class CloudServiceEntry {
    private final String name;
    private final String domain;
    private final String risk;
    private final String country;
    private final boolean gdprCompliant;

    public CloudServiceEntry(String name, String domain, String risk, String country, boolean gdprCompliant) {
        this.name = name;
        this.domain = domain;
        this.risk = risk;
        this.country = country;
        this.gdprCompliant = gdprCompliant;
    }

    public String getName() { return name; }
    public String getDomain() { return domain; }
    public String getRisk() { return risk; }
    public String getCountry() { return country; }
    public boolean isGdprCompliant() { return gdprCompliant; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CloudServiceEntry)) return false;
        CloudServiceEntry cs = (CloudServiceEntry) o;
        return name.equals(cs.name) && domain.equals(cs.domain);
    }
}
