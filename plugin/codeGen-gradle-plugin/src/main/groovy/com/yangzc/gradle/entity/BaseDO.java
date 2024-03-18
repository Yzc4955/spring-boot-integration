package com.yangzc.gradle.entity;

import com.yangzc.gradle.annotation.PreInsert;
import com.yangzc.gradle.annotation.PreUpdate;
import com.yangzc.gradle.dao.DOBasicOperation;

import java.time.LocalDateTime;

public class BaseDO implements DOBasicOperation {


    private String tmSmp;

    @PreInsert
    public void preInsert() {
        setTmSmp(LocalDateTime.now().toString());
    }

    @PreUpdate
    public void preUpdate() {
        setTmSmp(LocalDateTime.now().toString());
    }

    public String getTmSmp() {
        return tmSmp;
    }

    public void setTmSmp(String tmSmp) {
        this.tmSmp = tmSmp;
    }
}
