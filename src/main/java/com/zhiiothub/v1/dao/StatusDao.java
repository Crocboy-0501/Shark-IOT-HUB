package com.zhiiothub.v1.dao;

import com.zhiiothub.v1.model.Status;

import java.util.List;

public interface StatusDao {
    void save(Status status);
    List<Status> findAll();
}
