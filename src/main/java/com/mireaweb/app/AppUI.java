package com.mireaweb.app;

import com.mireaweb.app.views.AddWeightView;
import com.mireaweb.app.views.LoginView;
import com.mireaweb.app.views.RegistrationView;
import com.mireaweb.app.views.charts.*;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.sql.Date;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@PreserveOnRefresh
@Title("Weight")
@Theme("mytheme")

public class AppUI extends UI {
    public static AppUI get() {
        return (AppUI) UI.getCurrent();
    }

    public Button loginButton;
    public Button registrationButton;
    public Button exitButton;

    public Button addWeightButton;

    public Button weekChartButton;
    public Button twoWeeksChartButton;
    public Button monthChartButton;
    public Button halfYearChartButton;
    public Button yearChartButton;
    public DateField fromDate;
    public DateField toDate;
    public Button periodButton;
    public FormLayout chartsControlling;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new LoginView());
    }

    public GridLayout createMenu() {
        HorizontalLayout line1 = new HorizontalLayout();
        HorizontalLayout line2 = new HorizontalLayout();
        HorizontalLayout line3 = new HorizontalLayout();
        HorizontalLayout line4 = new HorizontalLayout();
        GridLayout grid = new GridLayout(1, 3);

        if (UserState.get().getUser() == null) {
            loginButton = new Button("Войти");
            loginButton.addClickListener(e -> setContent(new LoginView()));
            line1.addComponent(loginButton);

            registrationButton = new Button("Регистрация");
            registrationButton.addClickListener(e -> setContent(new RegistrationView()));
            line1.addComponent(registrationButton);

            grid.addComponent(line1, 0, 0);
        } else {
            if(UserState.get().getUser()!=null) {
                Label usernameText = new Label("Вы вошли как " + UserState.get().getUser().getEmail());
                grid.addComponent(usernameText, 0, 0);
            }
            exitButton = new Button("Выйти");
            exitButton.addClickListener(e -> {
                UserState.get().setUser(null);
                setContent(new LoginView());
            });
            line1.addComponent(exitButton);

            addWeightButton = new Button("Ввести вес");
            addWeightButton.addClickListener(e -> {
                UserState.get().setGraphsEnabled(false);
                setContent(new AddWeightView());
            });
            line1.addComponent(addWeightButton);

            addWeightButton = new Button("Графики");
            addWeightButton.addClickListener(e -> {
                UserState.get().setGraphsEnabled(true);
                setContent(new AddWeightView());
            });
            line1.addComponent(addWeightButton);

            if (UserState.get().getGraphsEnabled() == true) {
                FormLayout charts = new FormLayout();
                chartsControlling = charts;
                //charts.setMargin(true);

                Label textAboutCahrts = new Label("Графики веса");

                charts.addComponent(textAboutCahrts);

                NativeSelect<String> selectBox = new NativeSelect<>("Посмотреть график");
                selectBox.setEmptySelectionAllowed(false);
                selectBox.setItems("График за неделю", "График за 2 недели", "График за месяц", "График за полгода", "График за год");
                selectBox.addValueChangeListener(event -> {
                    //System.out.println("listener " + event.getValue());
                    if (!event.getValue().equals(event.getOldValue())) {
                        String selected = event.getValue();
//                    Notification.show(selected);
                        if (selected.equalsIgnoreCase("График за неделю")) {
                            setContent(new OneWeekChartView());
                        }
                        if (selected.equalsIgnoreCase("График за 2 недели")) {
                            setContent(new TwoWeeksChartView());
                        }
                        if (selected.equalsIgnoreCase("График за месяц")) {
                            setContent(new MonthChartView());
                        }
                        if (selected.equalsIgnoreCase("График за полгода")) {
                            setContent(new HalfAYearChartView());
                        }
                        if (selected.equalsIgnoreCase("График за год")) {
                            setContent(new YearChartView());
                        }
                    }
                });
                line2.addComponent(selectBox);

                Label text1 = new Label("Выбрать другой период:");
                line3.addComponent(text1);

                fromDate = new DateField();
//            fromDate.setValue(new LocalDate());
                fromDate.setDateFormat("dd-MM-yyyy");
                line4.addComponent(fromDate);

                toDate = new DateField();
//            fromDate.setValue(new LocalDate());
                toDate.setDateFormat("dd-MM-yyyy");
                line4.addComponent(toDate);

                periodButton = new Button("График за период");
                periodButton.addClickListener(e -> {
                    if (fromDate.getValue() == null || toDate.getValue() == null) {
                        Notification.show("Не указана дата");
                    } else {
                        setContent(new PeriodWeeksChartView(Date.valueOf(fromDate.getValue()), Date.valueOf(toDate.getValue())));
                    }
                });
                line4.addComponent(periodButton);
                charts.addComponent(line2);
                charts.addComponent(line3);
                charts.addComponent(line4);
                grid.addComponent(charts, 0, 2);
            }

//            charts.addComponent(line1);
            grid.addComponent(line1, 0, 1);
        }

        return grid;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}