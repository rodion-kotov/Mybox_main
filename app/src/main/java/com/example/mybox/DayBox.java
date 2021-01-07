package com.example.mybox;

import java.util.ArrayList;
import java.util.HashMap;

public class DayBox {
    int money;
    int min;
    int plus;

    HashMap<String,Operation> operations;

    public DayBox() {
    }

    public DayBox(int money) {
        this.money = money;
    }

    public DayBox(int money, HashMap<String, Operation> operations) {
        this.money = money;
        this.operations = operations;
    }

    public HashMap<String, Operation> getOperations() {
        return operations;
    }

    public void setOperations(HashMap<String, Operation> operations) {
        this.operations = operations;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


}
