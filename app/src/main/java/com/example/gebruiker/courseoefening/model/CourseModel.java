package com.example.gebruiker.courseoefening.model;

/**
 * Created by mjboere on 24-5-2017.
 */

public class CourseModel {

    public String name;
    public String ects;
    public String period;
    public String grade;
    public boolean voltooid;

    CourseModel(String name, String ects, String periode, String grade){
        this.name = name;
        this.ects = ects;
        this.grade = grade;
        this.period = periode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEcts() {
        return ects;
    }

    public void setEcts(String ects) {
        this.ects = ects;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isVoltooid() {
        return voltooid;
    }

    public void setVoltooid(boolean voltooid) {
        this.voltooid = voltooid;
    }
}
