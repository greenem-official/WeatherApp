package com.mireaweb.app.services;

import com.mireaweb.app.db.dao.DateDAO;
import com.mireaweb.app.db.model.Weight;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Actual tools to get useful data about weight for a graph
 */

public class DateService {
    private DateDAO dateDAO = new DateDAO();

    public List<Weight> selectLastWeek(long userId) {
        try {
            return dateDAO.selectLastWeek(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weight> selectLastTwoWeeks(long userId) {
        try {
            return dateDAO.selectLastTwoWeeks(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weight> selectLastMonth(long userId) {
        try {
            return dateDAO.selectLastMonth(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weight> selectLastHalfAYear(long userId) {
        try {
            return dateDAO.selectLastHalfAYear(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weight> selectLastYear(long userId) {
        try {
            return dateDAO.selectLastYear(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weight> selectPeriod(long userId, Date from, Date to) {
        try {
            return dateDAO.selectPeriod(userId, from, to);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addWeight(long userId, Date data, double value) {
        try {
            dateDAO.addWeightDAO(userId, data, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String isSendWeightFieldCorrect(String weight) {
        weight = weight.replaceAll(",", ".");
        double w = 0;
        try{
            w = Double.parseDouble(weight);
        }
        catch(NumberFormatException e){
            return "not double";
        }
        if(w<=0){
            return "negative or 0";
        }
        return "successful";
    }

//    public List<Weight> fixEmptyDates(List<Weight> list){
//        for (int i = 0; i<list.size(); i++){
//            if(list.)
//        }
//        list.add()
//        return list;
//    }
}
