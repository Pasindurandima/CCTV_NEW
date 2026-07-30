package com.example.demo.util;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class SlugUtils {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9\\s-]");
    private static final Pattern MULTI_HYPHENS = Pattern.compile("-+");
    private static final Pattern LEADING_TRAILING_HYPHENS = Pattern.compile("^-|-$");

    private SlugUtils() {
    }

    public static String createSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String normalized = input.toLowerCase(Locale.ENGLISH);
        normalized = NON_ALPHANUMERIC.matcher(normalized).replaceAll("");
        normalized = normalized.replaceAll("\\s+", "-");
        normalized = MULTI_HYPHENS.matcher(normalized).replaceAll("-");
        normalized = LEADING_TRAILING_HYPHENS.matcher(normalized).replaceAll("");
        return normalized;
    }

    public static String createUniqueSlug(String input, Predicate<String> slugExists) {
        String baseSlug = createSlug(input);
        if (baseSlug.isEmpty()) {
            return "product";
        }

        String candidate = baseSlug;
        int suffix = 2;
        while (slugExists.test(candidate)) {
            candidate = baseSlug + "-" + suffix;
            suffix++;
        }
        return candidate;
    }
}
