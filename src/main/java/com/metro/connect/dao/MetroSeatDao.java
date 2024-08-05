package com.metro.connect.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metro.connect.entity.MetroSeat;

@Repository
public interface MetroSeatDao extends JpaRepository<MetroSeat, Integer> {

	List<MetroSeat> findByMetroId(int metroId);

}
