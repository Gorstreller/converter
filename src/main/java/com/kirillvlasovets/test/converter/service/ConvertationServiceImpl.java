package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.dao.ConvertationRepository;
import com.kirillvlasovets.test.converter.entity.Convertation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertationServiceImpl implements ConvertationService {
    @Autowired
    private ConvertationRepository convertationRepository;

    @Override
    public List<Convertation> getAllConvertations() {
        return convertationRepository.findAll();
    }

    @Override
    public void saveConvertation(Convertation convertation) {
        convertationRepository.save(convertation);
    }

    @Override
    public void deleteConvertation(int id) {
        convertationRepository.deleteById(id);
    }
}
