package com.victorseger.svtech.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Filter {
    private String object;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate initialDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate finalDate;

    public Filter() {
        this.initialDate = LocalDate.now().minusMonths(1);
        this.finalDate = LocalDate.now();
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    @Override
    public String toString() {
        return "Filter " +
                "object '" + object + '\'' +
                ", initialDate " + initialDate +
                ", finalDate " + finalDate;
    }
}
