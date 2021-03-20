package com.mireaweb.app.views;

import com.mireaweb.app.AppUI;
import com.mireaweb.app.UserState;
import com.mireaweb.app.db.model.User;
import com.mireaweb.app.services.UserService;
import com.vaadin.ui.*;

import java.sql.SQLException;

/**
 * A constructor for registration panel
 */

public class RegistrationView extends VerticalLayout {
    private UserService userService = new UserService();

    public RegistrationView() {
        boolean usePasswordField = true;
        if(UserState.get().getUser()!=null){
            usePasswordField = UserState.get().getUsePasswordField(); // before we thought about making two types of password field for testing (TextField
                                                                      // and PasswordField types) and switch them if we change opinion on what is better
        }
        FormLayout form = new FormLayout();
        form.setMargin(true);

        Label errorLabel = new Label("");
        errorLabel.setVisible(false);
        form.addComponent(errorLabel);

        TextField emailField = new TextField("Email");
        emailField.setRequiredIndicatorVisible(true);
        form.addComponent(emailField);

        PasswordField passwordField = new PasswordField("Пароль");
        passwordField.setRequiredIndicatorVisible(true);
        form.addComponent(passwordField);

        PasswordField secondPasswordField = new PasswordField("Подтвердите пароль");
        secondPasswordField.setRequiredIndicatorVisible(true);
        form.addComponent(secondPasswordField);

        Button saveButton = new Button("Зарегистрироваться");
        saveButton.addClickListener(e -> {
            String isErr = "undefined";
            try {
                isErr = userService.register(emailField.getValue().trim(), passwordField.getValue().trim(), secondPasswordField.getValue().trim());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            errorLabel.setVisible(true);
            if (isErr.equalsIgnoreCase("empty email")) {
                errorLabel.setValue("Email не должен быть пустым");
            } else if (isErr.equalsIgnoreCase("empty password")) {
                errorLabel.setValue("Пароль не должен быть пустым");
            } else if (isErr.equalsIgnoreCase("password mismatch")) {
                errorLabel.setValue("Пароли не совпадают");
            } else if (isErr.equalsIgnoreCase("short password")) {
                errorLabel.setValue("Пароль должен содержать не менее "+ userService.minPasswordLength + " символов");
            } else if (isErr.equalsIgnoreCase("incorrect email")) {
                errorLabel.setValue("Введён некорректный Email");
            } else if (isErr.equalsIgnoreCase("registered already")) {
                errorLabel.setValue("Данный пользователь уже зарегистрирован");
            } else if (isErr.equalsIgnoreCase("error")) {
                errorLabel.setValue("Ошибка регистрации");
            } else if (isErr.equalsIgnoreCase("undefined")) {
                errorLabel.setValue("Ошибка регистрации");
            } else if (isErr.equalsIgnoreCase("successful")) {
                try {
                    User user = userService.getUser(emailField.getValue().trim(), passwordField.getValue().trim());
                    UserState.get().setUser(user);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                errorLabel.setValue("Вы успешно зарегистрированы");
                //setContent(createLoginPanel());
                AppUI.get().setContent(new AddWeightView());
            }
        });
        form.addComponent(saveButton);

        Panel panel = new Panel("Регистрация");
        panel.setSizeUndefined();
        panel.setContent(form);

        addComponent(AppUI.get().createMenu());
        addComponent(panel);
    }
}
