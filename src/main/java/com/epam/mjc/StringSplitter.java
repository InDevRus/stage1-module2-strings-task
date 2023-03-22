package com.epam.mjc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringSplitter {

    /**
     * Splits given string applying all delimeters to it. Keeps order of result substrings as in source string.
     *
     * @param source     source string
     * @param delimiters collection of delimiter strings
     * @return List of substrings
     */
    public List<String> splitByDelimiters(String source, Collection<String> delimiters) {
        var tokenizer = new StringTokenizer(source, String.join("", delimiters));
        return Stream
                .generate(() -> tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null)
                .takeWhile(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
