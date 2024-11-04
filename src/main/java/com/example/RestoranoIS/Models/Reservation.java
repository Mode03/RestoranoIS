package com.example.RestoranoIS.Models;

import java.time.LocalDate;

public class Reservation {
    private Integer id;
    public String customerName;
    public LocalDate date;
    public  int people;

    public Reservation(int id, String customerName,LocalDate date, int people) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.people = people;
    }

    public Reservation() {

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

}
