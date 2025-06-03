package com.cloudservicelogparser.filter;

import java.util.List;
import java.util.regex.Pattern;

public class UserFilter {
    private final List<Pattern> includes;
    private final List<Pattern> excludes;

    public UserFilter(List<Pattern> includes, List<Pattern> excludes) {
        this.includes = includes;
        this.excludes = excludes;
        // TODO add input validation for conflicts in includes and excludes
        // for example if an include pattern is a subset of an exclude pattern, or vice versa
    }

    public boolean isAllowed(String user) {
        if (user == null) return false;
        // check if user matches regex pattern
        boolean excluded = excludes.stream().anyMatch(p -> p.matcher(user).matches());
        // if excluded return false, and don't check includes for performance reasons
        if (excluded) return false;

        return includes.isEmpty() || includes.stream().anyMatch(p -> p.matcher(user).matches());
    }
}