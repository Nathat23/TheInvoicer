package com.invoicer.sql;

import java.util.regex.Pattern;

public class PostcodeValidation implements Validation {

    private Pattern pattern;

    @Override
    public boolean validate(String input) {
        if (pattern == null) {
            pattern = Pattern.compile("([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})");
        }
        return pattern.matcher(input).matches();
    }
}
