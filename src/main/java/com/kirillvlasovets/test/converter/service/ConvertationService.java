package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.entity.Convertation;

import java.util.List;

public interface ConvertationService {
    public List<Convertation> getAllConvertations();

    public void saveConvertation(Convertation convertation);

    public void deleteConvertation(int id);
}
