package com.mireaweb.app.views.charts;

import com.mireaweb.app.AppUI;
import com.mireaweb.app.UserState;
import com.mireaweb.app.db.model.Weight;
import com.mireaweb.app.services.WeightService;
import com.mireaweb.app.util.DateUtil;
import com.mireaweb.app.util.WeightUtil;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.sql.Date;
import java.util.List;

/**
 * A constructor of page with graphs for last week
 */

public class OneWeekChartView extends VerticalLayout {
    private WeightService weightService = new WeightService();

    public OneWeekChartView() {
        Panel panel = new Panel("График за неделю");
        panel.setSizeUndefined();
        long userId = UserState.get().getUser().getId();

        List<Weight> weights = weightService.selectLastWeek(userId);
        //System.out.println(weights.toString());
        Date today = DateUtil.today();
        Date nDaysAgo = DateUtil.subtractDays(today, 6);
        panel.setContent(new ChartView("Индекс массы тела", WeightUtil.toRange(weights, nDaysAgo, today, 0)));
        //System.out.println(toRange(weights, sixDaysAgo, today, 0).toString());

        addComponent(AppUI.get().createMenu());
        addComponent(panel);
    }

}
