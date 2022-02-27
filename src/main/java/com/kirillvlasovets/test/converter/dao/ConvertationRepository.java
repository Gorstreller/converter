package com.kirillvlasovets.test.converter.dao;

import com.kirillvlasovets.test.converter.entity.Converting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvertationRepository extends JpaRepository<Converting, Integer> {
}
