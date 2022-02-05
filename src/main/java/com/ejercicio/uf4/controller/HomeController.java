package com.ejercicio.uf4.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ejercicio.uf4.modelo.beans.Cuenta;
import com.ejercicio.uf4.modelo.dao.IntCuentaDao;

@Controller
public class HomeController {
	
	@Autowired
	private IntCuentaDao cuentaDao;

	@GetMapping("")
	public String inicio() {
		return "index";
	}
	
	@PostMapping("") // Evalúa si la cuenta existe
	public String validarCuenta(Cuenta cuenta, HttpSession sesion, RedirectAttributes attr) {
		String redirect = "";
		int idCuenta = cuenta.getIdCuenta();
		Cuenta cuentaValida = cuentaDao.buscarPorId(idCuenta); // Controlar error si no existe: javax.persistence.EntityNotFoundException
		
		if (cuentaValida != null) {
			sesion.setAttribute("cuentaValida", cuentaValida);
			attr.addFlashAttribute("idCuenta", idCuenta);
			redirect = "redirect:/menu";
		} else {
			// TODO
//			String mensaje = "Cuenta no válida";
//			model.addAttribute("mensaje", mensaje);
			redirect = "redirect:/menu";
		}
		return redirect;
	}
	
	@GetMapping("menu") // Si la cuenta existe, muesta las opciones
	public String muestraOpciones() {
		return "menuOpciones";
	}
//	
//	@PostMapping("") // Hace el ingreso y muestra la vista de movimientos
//	public String ingresarDinero() {
//		return "";
//	}
//	
//	@PostMapping("") // Retira dinero y muestra la vista de movimientos
//	public String extraerDinero() {
//		return "";
//	}
//	
//	@GetMapping("") // 
//	public String muestraTransferencia() {
//		return "";
//	}
//
//	@PostMapping("") // 
//	public String procesaTransferencia() {
//		return "";
//	}
	
//	@GetMapping("") // Si la cuenta existe, muesta las opciones
//	public String muestraMovimientos() {
//		return "menuOpciones";
//	}

}
