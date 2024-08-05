package com.metro.connect.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metro.connect.entity.Metro;

@Repository
public interface MetroDao extends JpaRepository<Metro, Integer> {

	Metro findByNumber(String metroNumber);

	List<Metro> findByNumberContainingIgnoreCaseAndStatus(String metroNumber, int status);

	List<Metro> findByFromLocationIdAndToLocationIdAndStatus(int fromLocationId, int toLocationId, int status);

	List<Metro> findByStatus(int status);
}
