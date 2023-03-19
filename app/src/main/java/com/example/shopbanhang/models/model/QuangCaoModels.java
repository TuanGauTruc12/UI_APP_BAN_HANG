package com.example.shopbanhang.models.model;

import com.example.shopbanhang.models.object.QuangCao;
import java.util.List;

public class QuangCaoModels {
    boolean success;
    List<QuangCao> listQuangCaos;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<QuangCao> getListQuangCaos() {
        return listQuangCaos;
    }
}
