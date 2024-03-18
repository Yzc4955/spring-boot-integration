package com.yangzc.gradle.dao;

import java.util.List;

public interface BaseDao<T extends DOBasicOperation, K> {
    /**
     * 根据ID查找
     *
     * @param id
     * @return
     */
    T get(K id);

    /**
     * 根据条件查找
     *
     * @param entity
     * @return
     */
    List<T> find(T entity);

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    int delete(K id);
}
