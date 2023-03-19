package com.example.shopbanhang.models.model;

import com.example.shopbanhang.models.object.ThongBao;
import com.example.shopbanhang.models.object.User;
import java.util.List;

public class UserModels extends ThongBao {

    private List<User> users;
    public List<User> getUsers() {
        return users;
    }
}
