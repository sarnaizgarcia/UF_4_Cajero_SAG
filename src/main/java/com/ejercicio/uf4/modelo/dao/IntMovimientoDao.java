package com.ejercicio.uf4.modelo.dao;

import java.util.List;

import com.ejercicio.uf4.modelo.beans.Cuenta;
import com.ejercicio.uf4.modelo.beans.Movimiento;

public interface IntMovimientoDao {
	
	List<Movimiento> buscarTodos();
	Movimiento crearMovimiento(Movimiento movimiento);

}
