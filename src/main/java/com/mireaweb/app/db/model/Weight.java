package com.mireaweb.app.db.model;

import java.sql.Date;

/**
 * A class Weight, containing data of user to certain day
 */

public class Weight {
    private long id;
    private long userId;
    private Date data; //it could be called "date", but was written as in russian first not to get problems with name (actually not), but it's not so important ;)
    private double value;

    public Weight(long id, long userId, Date data, double value) {
        this.id = id;
        this.userId = userId;
        this.data = data;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public long getUserid() {
        return userId;
    }

    public Date getData() {
        return data;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "id=" + id +
                ", userId=" + userId +
                ", data=" + data +
                ", value=" + value +
                '}';
    }
}
