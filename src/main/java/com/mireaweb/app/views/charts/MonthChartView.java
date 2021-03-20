package com.mireaweb.app.views.charts;

import com.mireaweb.app.AppUI;
import com.mireaweb.app.UserState;
import com.mireaweb.app.db.model.Weight;
import com.mireaweb.app.services.WeightService;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.sql.Date;
import java.util.List;

import static com.mireaweb.app.util.DateUtil.*;
import static com.mireaweb.app.util.WeightUtil.toRange;

/**
 * A constructor of page with graphs for last month
 */

public class MonthChartView extends VerticalLayout {
    private WeightService weightService = new WeightService();

    public MonthChartView() {
        Panel panel = new Panel("График за месяц");
        panel.setSizeUndefined();
        long userId = UserState.get().getUser().getId();

        List<Weight> weights = weightService.selectLastMonth(userId);
        Date today = today();
        Date nDaysAgo = addDays(subtractMonths(today, 1), 1);
        panel.setContent(new ChartView("Индекс массы тела", toRange(weights, nDaysAgo, today, 0)));

        addComponent(AppUI.get().createMenu());
        addComponent(panel);
    }
}
