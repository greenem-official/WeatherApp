package com.mireaweb.app.views.charts;

import com.mireaweb.app.UserState;
import com.mireaweb.app.db.model.Weight;
import com.vaadin.server.Page;
import com.vaadin.ui.VerticalLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.vaadin.addon.JFreeChartWrapper;

import java.util.List;

/**
 * A constructor for generating any graph using JFreeChart
 */

public class ChartView extends VerticalLayout {
    String title;

    String xTitle = "День";
    String yTitle = "Значение";

    List<Weight> weights;

    public ChartView(String title, List<Weight> weights) {
        this.weights = weights;
        addComponent(creditGraphLayout());
    }

    public VerticalLayout creditGraphLayout() {
        String rowKey = "Индекс массы тела";
        VerticalLayout layout = new VerticalLayout();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Weight weight : weights) {
            dataset.setValue(weight.getValue(), rowKey, weight.getData().toString());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                xTitle,
                yTitle,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );


        JFreeChartWrapper wrapper = new JFreeChartWrapper(chart);
        float width = (float) (Page.getCurrent().getBrowserWindowWidth() * 0.8);
        float height = (float) (Page.getCurrent().getBrowserWindowHeight() * 0.8);
        if((float) (height/width)>1){
            height = (float) (width * 0.6);
        }
        wrapper.setWidth((float) (width), Unit.PIXELS);
        wrapper.setHeight((float) (height), Unit.PIXELS);

        layout.addComponent(wrapper);

        UserState.get().setTownSelected(null);
        UserState.get().setTimeSelected(null);

        return layout;
    }

}
