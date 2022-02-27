package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.dao.ConvertationRepository;
import com.kirillvlasovets.test.converter.entity.Converting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvertingServiceImpl implements ConvertingService {
    @Autowired
    private ConvertationRepository convertationRepository;

    @Override
    public List<Converting> getAllConvertings() {
        return convertationRepository.findAll();
    }

    @Override
    public void saveConvertation(Converting convertation) {
        convertationRepository.save(convertation);
    }

    @Override
    public void deleteConvertation(int id) {
        convertationRepository.deleteById(id);
    }
}
