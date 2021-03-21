package com.mireaweb.app;

import com.mireaweb.app.db.model.User;
import com.vaadin.server.VaadinSession;

/**
 * A class for keeping any data of current user's session
 */

public class UserState {
    private static String SESSION_KEY_USER_STATE = "UserState";

    private User user;
    private boolean graphsEnabled = true;
    private boolean usePasswordField = true;

    public String getTownSelected() {
        return TownSelected;
    }

    public void setTownSelected(String townSelected) {
        TownSelected = townSelected;
    }

    public String getTimeSelected() {
        return TimeSelected;
    }

    public void setTimeSelected(String timeSelected) {
        TimeSelected = timeSelected;
    }

    private String TownSelected;
    private String TimeSelected;

    private String year;
    private String month;
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public static UserState get() {
        UserState userState = (UserState) VaadinSession.getCurrent().getAttribute(SESSION_KEY_USER_STATE);
        if (userState == null) {
            userState = new UserState();
            VaadinSession.getCurrent().setAttribute(SESSION_KEY_USER_STATE, userState);
        }
        return userState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getGraphsEnabled() {
        return graphsEnabled;
    }

    public void setGraphsEnabled(boolean graphsEnabled) {
        this.graphsEnabled = graphsEnabled;
    }

    public boolean getUsePasswordField() {
        return usePasswordField;
    }

    public void setUsePasswordField(boolean usePasswordField) {
        this.usePasswordField = usePasswordField;
    }
}