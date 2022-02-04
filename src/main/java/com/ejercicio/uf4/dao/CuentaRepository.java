package com.ejercicio.uf4.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ejercicio.uf4.beans.Cuenta;

public interface CuentaRepository extends CrudRepository <Cuenta, Integer> {
	
//	List<Cuenta> findAll();

}
