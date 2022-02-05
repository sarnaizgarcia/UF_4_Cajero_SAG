package com.ejercicio.uf4.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejercicio.uf4.modelo.beans.Cuenta;

public interface IntCuentaRepository extends JpaRepository<Cuenta, Integer> {

}
