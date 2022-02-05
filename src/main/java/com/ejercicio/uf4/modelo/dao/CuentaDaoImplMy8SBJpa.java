package com.ejercicio.uf4.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ejercicio.uf4.modelo.beans.Cuenta;
import com.ejercicio.uf4.modelo.repository.IntCuentaRepository;

@Repository
public class CuentaDaoImplMy8SBJpa implements IntCuentaDao {
	
	@Autowired
	private IntCuentaRepository cuentaRepo;

	@Override
	public Cuenta buscarPorId(int idCuenta) {
		return cuentaRepo.findById(idCuenta).orElse(null);
	}

	@Override
	public List<Cuenta> buscarTodos() {
		return cuentaRepo.findAll();
	}

	@Override
	public Cuenta actualizarCuenta(Cuenta cuenta) {
		return cuentaRepo.save(cuenta);
	}

}
