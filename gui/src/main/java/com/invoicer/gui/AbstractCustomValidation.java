package com.invoicer.gui;

public interface AbstractCustomValidation {

    ValidationResult validatePage();

    class ValidationResult {

         private final boolean valid;
         private final String errorMessage;

         public ValidationResult(boolean valid, String errorMessage) {
             this.valid = valid;
             this.errorMessage = errorMessage;
         }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
