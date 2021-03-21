package com.mireaweb.app;

import com.mireaweb.app.views.AddWeightView;
import com.mireaweb.app.views.LoginView;
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

    public DateField fromDate;
    public DateField toDate;
    public Button periodButton;
    public FormLayout chartsControlling;

    public TextField yearField;
    public TextField monthField;
    public TextField dayField;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new LoginView());
    }

    public GridLayout createMenu() {
        HorizontalLayout line1 = new HorizontalLayout();
        HorizontalLayout line2 = new HorizontalLayout();
        HorizontalLayout line2p1 = new HorizontalLayout();
        HorizontalLayout line2p2 = new HorizontalLayout();
        HorizontalLayout line2p3 = new HorizontalLayout();
        HorizontalLayout line3 = new HorizontalLayout();
        HorizontalLayout line4 = new HorizontalLayout();
        GridLayout grid = new GridLayout(1, 3);
            if (UserState.get().getGraphsEnabled() == true) {
                FormLayout charts = new FormLayout();
                chartsControlling = charts;
                //charts.setMargin(true);

                Label textAboutCharts = new Label("Графики температуры воздуха");
                charts.addComponent(textAboutCharts);

                NativeSelect<String> selectTownBox = new NativeSelect<>("Выбрать город");
                selectTownBox.setEmptySelectionAllowed(false);
                selectTownBox.setItems("Алмазный", "Западный", "Курортный", "Лесной", "Научный", "Полярный", "Портовый", "Приморский", "Садовый", "Северный", "Степной", "Таежный", "Южный");
                selectTownBox.addValueChangeListener(event -> {
                    //System.out.println("listener " + event.getValue());
                    if (!event.getValue().equals(event.getOldValue())) {
                        String selected = event.getValue();
//                    Notification.show(selected);
                        UserState.get().setTownSelected(selected);
                        tryToGenerateGraph();
                    }
                });
                line2.addComponent(selectTownBox);

                yearField = new TextField("Год");
//                yearField.setRequiredIndicatorVisible(true);
                line2p1.addComponent(yearField);

                monthField = new TextField("Месяц");
//                monthField.setRequiredIndicatorVisible(true);
                line2p2.addComponent(monthField);

                dayField = new TextField("День");
//                dayField.setRequiredIndicatorVisible(true);
                line2p3.addComponent(dayField);

                NativeSelect<String> selectBox = new NativeSelect<>("Посмотреть график");
                selectBox.setEmptySelectionAllowed(false);
                selectBox.setItems("График за день", "График за неделю", "График за месяц", "График за полгода", "График за год");
                selectBox.addValueChangeListener(event -> {
                    //System.out.println("listener " + event.getValue());
                    if (!event.getValue().equals(event.getOldValue())) {
                        String selected = event.getValue();
                        UserState.get().setTimeSelected(selected);
//                    Notification.show(selected);
                        tryToGenerateGraph();
                    }
                });
                line2.addComponent(selectBox);

                Label text1 = new Label("Выбрать произвольный период:");
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
                charts.addComponent(line2p1);
                charts.addComponent(line2p2);
                charts.addComponent(line2p3);
                charts.addComponent(line3);
                charts.addComponent(line4);
                grid.addComponent(charts, 0, 2);
            }

//            charts.addComponent(line1);
            grid.addComponent(line1, 0, 1);
        return grid;
    }

    public void tryToGenerateGraph(){
        if(isInt(yearField.getValue())) UserState.get().setYear(yearField.getValue());
        if(isInt(monthField.getValue())) UserState.get().setYear(monthField.getValue());
        if(isInt(dayField.getValue())) UserState.get().setYear(dayField.getValue());

        String town = UserState.get().getTownSelected();
        String time = UserState.get().getTimeSelected();
        String year = UserState.get().getYear();
        String month = UserState.get().getMonth();
        String day = UserState.get().getDay();
        System.out.println("town = " + town + "; time = " + time);
        if(town==null) return;
        if(time==null) return;
        if(year==null) return;
        if(month==null) return;
        if(day==null) return;
        if (time.equalsIgnoreCase("График за день")) {
            setContent(new OneDayChartView());
        }
        if (time.equalsIgnoreCase("График за неделю")) {
            setContent(new OneWeekChartView());
        }
        if (time.equalsIgnoreCase("График за месяц")) {
            setContent(new MonthChartView());
        }
        if (time.equalsIgnoreCase("График за полгода")) {
            setContent(new HalfAYearChartView());
        }
        if (time.equalsIgnoreCase("График за год")) {
            setContent(new YearChartView());
        }
    }

    public boolean isInt(String s){
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}