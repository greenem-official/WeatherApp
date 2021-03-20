package com.mireaweb.app.util;

import com.mireaweb.app.db.model.Weight;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class WeightUtil {
    /**
     * Adjusts weights by merging with range[from, to]
     * @param weights list of weights that should be adjusted in toRange
     * @param from starting toRange point
     * @param to ending toRange point
     * @param initialValue initial value of toRange values
     */
    public static List<Weight> toRange(List<Weight> weights, Date from, Date to, double initialValue) {
        TreeMap<String, Weight> tree = new TreeMap<>();
        for (Weight weight : weights) {
            tree.put(new Date(weight.getData().getTime()).toString(), weight);
        }

        Date d = from;
        while(d.before(to) || d.equals(to)) {
            //System.out.println(tree.toString());
            //System.out.println(tree.containsKey(d) + "");
            //System.out.println(tree.get(d.toString())!=null);
            if(!tree.containsKey(d.toString())) {
                tree.put(d.toString(), new Weight(0, 0, d, initialValue));
            }
            // move to next day:
            d = new Date(d.getTime() + 24*60*60*1000);
        }

        List<Weight> result = new ArrayList<>(tree.values());
        return result;
    }

    public static void main(String[] args) {
        String s1 = "1 2 ";
        //s1 = s1.trim();
        String s2 = "dfs";
        System.out.println(s1.trim()+s2);
        if(0==0) {
            return;
        }

        // test:
        List<Weight> weights = new ArrayList<>();
        weights.add(new Weight(0, 0, new Date(2021 - 1900, 0, 11), 11));
        weights.add(new Weight(0, 0, new Date(2021 - 1900, 0, 13), 13));
        weights.add(new Weight(0, 0, new Date(2021 - 1900, 0, 15), 15));

        List<Weight> result = toRange(weights,
                new Date(2021 - 1900, 0, 7),
                new Date(2021 - 1900, 0, 17),
                0
        );

        for (Weight weight : result) {
            System.out.println(weight);
        }
    }
}
