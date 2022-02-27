package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.entity.Converting;

import java.util.List;

public interface ConvertingService {
    public List<Converting> getAllConvertings();

    public void saveConvertation(Converting convertation);

    public void deleteConvertation(int id);
}
