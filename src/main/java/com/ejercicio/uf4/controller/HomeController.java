package com.ejercicio.uf4.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ejercicio.uf4.modelo.beans.Cuenta;
import com.ejercicio.uf4.modelo.beans.Movimiento;
import com.ejercicio.uf4.modelo.dao.IntCuentaDao;
import com.ejercicio.uf4.modelo.dao.IntMovimientoDao;

@Controller
public class HomeController {
	
	@Autowired
	private IntCuentaDao cuentaDao;
	
	@Autowired 
	private IntMovimientoDao movimientoDao;

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
			redirect = "redirect:/menu";
		} else {
			String mensaje = "Cuenta no válida";
			attr.addFlashAttribute("mensaje", mensaje);
			redirect = "redirect:/";
		}
		return redirect;
	}
	
	@GetMapping("/menu") // Si la cuenta existe, muesta las opciones
	public String muestraOpciones(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "menuOpciones";
	}
	
	@GetMapping("/cuentas/{idCuenta}/ingresos")
	public String mostrarIngreso(HttpSession sesion, Model model, @PathVariable("idCuenta") int idCuenta) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "ingreso";
	}
		
	@PostMapping("/cuentas/{idCuenta}/ingresos") // Hace el ingreso y muestra la vista de movimientos
	public String ingresarDinero(Movimiento movimiento, HttpSession sesion, Model model, @PathVariable("idCuenta") int idCuenta) {
		System.out.println("Ingresando");
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");

		movimiento.setOperacion("ingreso");
		movimiento.setCuenta(cuenta);
		movimiento.setFecha(new Date());
		movimientoDao.crearMovimiento(movimiento);
		
		double saldo = cuenta.getSaldo();
		double saldoActualizado = saldo + movimiento.getCantidad();
		cuenta.setSaldo(saldoActualizado);
		cuentaDao.actualizarCuenta(cuenta);
		
		model.addAttribute("cuenta", cuenta);
		model.addAttribute("movimientos", movimientoDao.buscarTodos());
		
		return "redirect:/menu";
	}
	
	@GetMapping("/cuentas/{idCuenta}/retiradas")
	public String mostrarRetirada(@PathVariable("idCuenta") int idCuenta, HttpSession sesion, Model model) {
		System.out.println("En retiradas");
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "retirada";
	}
	
	@PostMapping("/cuentas/{idCuenta}/retiradas")
	public String retirarDinero(Movimiento movimiento, HttpSession sesion, Model model) {
		System.out.println("Retirando");
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		
		boolean saldoSuficiente = cuenta.getSaldo() >= movimiento.getCantidad();
		
		if (saldoSuficiente == false) {
			System.out.println("Saldo insuficiente");
			return "saldoInsuficiente";
		}

		movimiento.setOperacion("retirada");
		movimiento.setCuenta(cuenta);
		movimiento.setFecha(new Date());
		movimientoDao.crearMovimiento(movimiento);
		
		double saldo = cuenta.getSaldo();
		double saldoActualizado = saldo - movimiento.getCantidad();
		cuenta.setSaldo(saldoActualizado);
		cuentaDao.actualizarCuenta(cuenta);
		
		model.addAttribute("movimientos", movimientoDao.buscarTodos());
		
		return "redirect:/menu";
	}
	
	@GetMapping("/movimientos")
	public String muestraMovimientos(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		
		List<Movimiento> movimientos = movimientoDao.buscarTodos();
		sesion.setAttribute("movimientos", movimientos);
		model.addAttribute("movimientos", movimientos);
		
		return "movimientos";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
		
	}
	

	@GetMapping("/transferencias") // 
	public String muestraTransferencia(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		
		return "transferencias";
	}
//
//	@PostMapping("") // 
//	public String procesaTransferencia() {
//		return "";
//	}

}
