package org.example.service;


import java.util.List;
import java.util.UUID;

public interface Service<T> {
    T save(T user);

    T findById(UUID uuid);

    List<T> findAll();

    void createTable();

    boolean deleteById(UUID id);
}
