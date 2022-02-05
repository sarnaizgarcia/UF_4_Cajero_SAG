package com.ejercicio.uf4.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ejercicio.uf4.modelo.beans.Cuenta;
import com.ejercicio.uf4.modelo.beans.Movimiento;
import com.ejercicio.uf4.modelo.repository.IntMovimientoRepository;

@Repository
public class MovimientoDaoImplMy8SBJpa implements IntMovimientoDao {

	@Autowired
	private IntMovimientoRepository movimientoRepo;

	@Override
	public List<Movimiento> buscarTodos() {
		return movimientoRepo.findAll();
	}

	@Override
	public Movimiento crearMovimiento(Movimiento movimiento) {
		return movimientoRepo.save(movimiento);
	}

}
