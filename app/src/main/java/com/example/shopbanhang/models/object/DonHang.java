package com.example.shopbanhang.models.object;

import java.util.List;

public class DonHang {
    private int id;
    private List<Item> item;

    public int  getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<Item> getItem() {
        return item;
    }
    public void setItem(List<Item> item) {
        this.item = item;
    }
}
