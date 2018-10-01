package com.victorseger.cursomc.domain;

import java.util.Date;

public class Filter {
    private String object;
    private Date initialDate;
    private Date finalDate;

    public Filter() {
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
}
