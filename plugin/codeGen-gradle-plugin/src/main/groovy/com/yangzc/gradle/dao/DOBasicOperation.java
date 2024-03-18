package com.yangzc.gradle.dao;


import com.yangzc.gradle.annotation.PreInsert;
import com.yangzc.gradle.annotation.PreUpdate;

/**
 * DO framework operation interface，所有DO对象实现该接口
 * Dao 方法insert* 自动执行preInsert
 * Dao 方法update* 自动执行preUpdate
 */
public interface DOBasicOperation {

    @PreInsert
    void preInsert();

    @PreUpdate
    void preUpdate();

}

