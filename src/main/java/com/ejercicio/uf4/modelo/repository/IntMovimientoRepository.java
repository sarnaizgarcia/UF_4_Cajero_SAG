package com.ejercicio.uf4.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejercicio.uf4.modelo.beans.Movimiento;

public interface IntMovimientoRepository extends JpaRepository<Movimiento, Integer> {

}
