package com.invoicer.main;

import com.invoicer.gui.*;

import java.io.IOException;

public class StartDialog extends Dialog {

    private final LoginHandler loginHandler;

    public StartDialog(LoginHandler loginHandler) {
        super("Start", DialogSize.SMALL);
        this.loginHandler = loginHandler;
    }

    @Override
    public void populate() {
        DialogPage dialogPage = new DialogPage("Login");
        StringTextFieldElement username = new StringTextFieldElement("Username");
        StringTextFieldElement password = new PasswordTextFieldElement("Password");
        dialogPage.addElement(username);
        dialogPage.addElement(password);
        dialogPage.setValidation(new CustomValidation() {
            @Override
            public ValidationResult validatePage() {
                if (getLoginHandler().isValid(username.getValue(), password.getValue())) {
                    return new AbstractCustomValidation.ValidationResult(true, "");
                }
                return new AbstractCustomValidation.ValidationResult(false, "Incorrect password!");
            }
        });
        DialogPage newUser = new DialogPage("New User");
        LabelElement labelElement = new LabelElement("Welcome to TheInvoicer! Set your username and password below:");
        StringTextFieldElement newUsername = new StringTextFieldElement("Username");
        StringTextFieldElement newPassword = new PasswordTextFieldElement("Password");
        StringTextFieldElement newPasswordConfirm = new PasswordTextFieldElement("Confirm password ");
        newUser.addElement(labelElement);
        newUser.addElement(newUsername);
        newUser.addElement(newPassword);
        newUser.addElement(newPasswordConfirm);
        newUser.setValidation(new CustomValidation() {
            @Override
            public ValidationResult validatePage() {
                if (!newPassword.getValue().equals(newPasswordConfirm.getValue())) {
                    return new AbstractCustomValidation.ValidationResult(false, "Passwords do not match!");
                }
                return new AbstractCustomValidation.ValidationResult(true, "");
            }
        });
        if (getLoginHandler().isNewUser()) {
            addPage(newUser);
            return;
        }
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        if (!getDialogPage().getPageTitle().equals("New User")) {
            return;
        }
        StringTextFieldElement username = (StringTextFieldElement) getDialogPage().getElementList().get(0);
        StringTextFieldElement password = (StringTextFieldElement) getDialogPage().getElementList().get(1);
        try {
            getLoginHandler().hashAndStore(username.getValue(), password.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LoginHandler getLoginHandler() {
        return loginHandler;
    }
}
