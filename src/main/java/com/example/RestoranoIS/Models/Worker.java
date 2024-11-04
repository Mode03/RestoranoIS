package com.example.RestoranoIS.Models;

import org.hibernate.jdbc.Work;

public class Worker {
    private Integer Tab_nr;
    public String Name;
    public String LastName;
    public  String Address;

    public Worker(int tab_nr, String name, String lastName, String address){
        setTab_nr(tab_nr);
        this.Name = name;
        this.LastName = lastName;
        this.Address = address;
    }

    public Integer getTab_nr(){
        return this.Tab_nr;
    }
    public void setTab_nr(Integer tab_nr) {
        Tab_nr = tab_nr;
    }

}
