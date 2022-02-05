package com.ejercicio.uf4.modelo.dao;

import java.util.List;

import com.ejercicio.uf4.modelo.beans.Cuenta;

public interface IntCuentaDao {
	
	List<Cuenta> buscarTodos();
	Cuenta buscarPorId(int idCuenta);
//	Cuenta actualizarSaldo(int idCuenta, double incremento);
	
}
