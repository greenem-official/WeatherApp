package com.mireaweb.app.views;

import com.mireaweb.app.AppUI;
import com.mireaweb.app.UserState;
import com.mireaweb.app.db.dao.WeightDAO;
import com.mireaweb.app.services.WeightService;
import com.vaadin.ui.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A constructor for graphs panel with a few functions for it
 */

public class AddWeightView extends VerticalLayout {
    private WeightService weightService = new WeightService();
    private WeightDAO weightDao = new WeightDAO();

    public TextField weightLast;
    public TextField weightToday;
    public Button confirmSendingWeight;

    public AddWeightView() {
        if(AppUI.get().loginButton!=null) {
            AppUI.get().loginButton.setVisible(false);
        }
        else{
            //System.out.println("loginButton is null");
        }
        if(AppUI.get().registrationButton!=null) {
            AppUI.get().registrationButton.setVisible(false);
        }
        else{
            //System.out.println("registrationButton is null");
        }
        if(AppUI.get().chartsControlling!=null) {
            AppUI.get().chartsControlling.setVisible(false);
        }
        else{
            //System.out.println("chartsControlling is null");
        }
        Panel announcementField = new Panel("");
        Panel panel = new Panel("Ввести вес");
        if(UserState.get().getGraphsEnabled()==false) {
            FormLayout form = new FormLayout();
            form.setMargin(true);

            Label errorLabel = new Label("");
            errorLabel.setVisible(false);

            final TextField lastWeight = new TextField("Вес в прошлый раз");
            lastWeight.setRequiredIndicatorVisible(false);
            lastWeight.setEnabled(false);
            weightLast = lastWeight;
            LastWeight(lastWeight);
            form.addComponent(lastWeight);

            TextField lastDateOfWeight = new TextField("Было введено");
            lastDateOfWeight.setRequiredIndicatorVisible(false);
            lastDateOfWeight.setEnabled(false);
            LastTimeDay(lastDateOfWeight);
            form.addComponent(lastDateOfWeight);

            TextField TodayWeight = new TextField("Вес сейчас");
            TodayWeight.setRequiredIndicatorVisible(false);
            weightToday = TodayWeight;
            form.addComponent(TodayWeight);

            Button saveButton = new Button("Подтвердить");
            confirmSendingWeight = saveButton;
            saveButton.addClickListener(e -> {
                String value = weightToday.getValue();
                value = value.replaceAll(",", ".");
                String sendErr = weightService.isSendWeightFieldCorrect(value);
                if (sendErr.equalsIgnoreCase("not double")) {
                    errorLabel.setVisible(true);
                    errorLabel.setValue("Пожалуйста, введите число");
//                weightToday.wi
                } else if (sendErr.equalsIgnoreCase("negative or 0")) {
                    errorLabel.setVisible(true);
                    errorLabel.setValue("Число должно быть больше нуля");
                } else if (sendErr.equalsIgnoreCase("successful")) {
                    weightService.addWeight(UserState.get().getUser().getId(), new Date(System.currentTimeMillis()), Double.parseDouble(value));
                    errorLabel.setValue("Данные успешно отправленны");
                    errorLabel.setVisible(true);
                    //if(!lastDateOfWeight.isVisible()){
                    LastTimeDay(lastDateOfWeight);
                    LastWeight(lastWeight);
                    EditAnnouncementField(announcementField);
                }
            });
            form.addComponent(saveButton);

            VerticalLayout VLayout = new VerticalLayout();
            VLayout.addComponent(announcementField);
            VLayout.addComponent(form);
            VLayout.addComponent(errorLabel);

            announcementField.setVisible(false);

            EditAnnouncementField(announcementField);

            panel.setSizeUndefined();
            panel.setContent(VLayout);

        }
            addComponent(AppUI.get().createMenu());
        if(UserState.get().getGraphsEnabled()==false) {
            addComponent(announcementField);
            addComponent(panel);
        }
    }

    private void LastTimeDay(TextField lastDateOfWeight){
        Date lastDate = null;
        if (UserState.get().getUser() != null) {
            try {
                lastDate = weightDao.getDateOfLastWeight(UserState.get().getUser().getId());
                if(lastDate == null) {
                    lastDateOfWeight.setVisible(false);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String lastDayStr = null;
        int secondsInDay = 86400000;
        if((new Date(System.currentTimeMillis()) + "").equals(lastDate.toString())){
            lastDayStr = "Сегодня";
        }
        if((new Date(System.currentTimeMillis()-secondsInDay*1) + "").equals(lastDate.toString())){
            lastDayStr = "Вчера";
        }
        if((new Date(System.currentTimeMillis()-secondsInDay*2) + "").equals(lastDate.toString())){
            lastDayStr = "Позавчера";
        }
        //System.out.println((new Date(System.currentTimeMillis()) + "").equals(lastDate.toString()));
        if (lastDate != null) {
            if(lastDayStr == null) {
                lastDateOfWeight.setValue(lastDate.toString());
                lastDateOfWeight.setVisible(true);
            }
            else{
                lastDateOfWeight.setValue(lastDayStr);
                lastDateOfWeight.setVisible(true);
            }
        } else {
            lastDateOfWeight.setVisible(false);
        }
    }

    private void LastWeight(TextField lastWeight) {
        Double lastValue = null;
        if (UserState.get().getUser() != null) {
            try {
                lastValue = weightDao.getLastWeight(UserState.get().getUser().getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (lastValue != null) {
            lastWeight.setValue(Double.toString(lastValue));
        } else {
            lastWeight.setValue("Ещё нет");
        }
    }

    private String HowLongAgoWasAddedLast(long userId){
        Date lastDate = null;
        if (UserState.get().getUser() != null) {
            try {
                lastDate = weightDao.getDateOfLastWeight(userId);
                if(lastDate == null) {
                    return "never";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String DaysAgoStr = null;
        int secondsInDay = 86400000;
        LocalDate d1 = LocalDate.parse(new Date(System.currentTimeMillis()) + "", DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(lastDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        long diffDays = diff.toDays();
        return Long.toString(diffDays);
    }

    private void EditAnnouncementField(Panel announcementField){
        if(UserState.get().getUser()!=null) {
            String daysAgo = HowLongAgoWasAddedLast(UserState.get().getUser().getId());
            if(daysAgo!=null && !daysAgo.equalsIgnoreCase("never")){
                long longDaysAgo = Math.abs(Long.parseLong(daysAgo));
                //System.out.println(daysAgo);
                if(longDaysAgo < 1) {
                    //nothing
                    announcementField.setVisible(false);
                }
                else if(longDaysAgo == 1) {
                    announcementField.setContent(new Label("Вы не вводили данные сегодня!"));
                    announcementField.setVisible(true);
                }
                else if(longDaysAgo == 2) {
                    announcementField.setContent(new Label("Вы не вводили данные вчера!"));
                    announcementField.setVisible(true);
                }
                else{
                    announcementField.setContent(new Label("Вы не вводили данные уже " + longDaysAgo + " дня(ей)!"));
                    announcementField.setVisible(true);
                }
            }
        }
    }
}
