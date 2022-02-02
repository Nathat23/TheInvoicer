package com.invoicer.sql;

import java.util.regex.Pattern;

public class EmailValidation implements Validation {

    private Pattern pattern;

    @Override
    public boolean validate(String input) {
        if (pattern == null) {
            pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        }
        return pattern.matcher(input).matches();
    }
}
