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

	/**
	 * Muestra la vista de inicio.
	 * @return
	 */
	@GetMapping("")
	public String inicio() {
		return "index";
	}
	
	/**
	 * Comprueba si existe alguna cuenta con el id del input.
	 * Si existe se dirige al menú y si no existe permanece en la misma vista
	 * y muestra un mensaje de error.
	 * @param cuenta
	 * @param sesion
	 * @param attr
	 * @return
	 */
	@PostMapping("")
	public String validarCuenta(Cuenta cuenta, HttpSession sesion, RedirectAttributes attr) {
		String redirect = "";
		int idCuenta = cuenta.getIdCuenta();
		Cuenta cuentaValida = cuentaDao.buscarPorId(idCuenta);
		
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
	
	/**
	 * Muestra la vista con el menú de opciones.
	 * Establece un atributo de sesión con la cuenta válida y otro atributo para mostrar la cuenta en la vista.
	 * @param sesion
	 * @param model
	 * @return
	 */
	@GetMapping("/menu")
	public String muestraOpciones(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "menuOpciones";
	}
	
	/**
	 * Muestra la vista para hacer ingresos en la cuenta que se ha validado anteriormente.
	 * @param sesion
	 * @param model
	 * @param idCuenta
	 * @return
	 */
	@GetMapping("/cuentas/{idCuenta}/ingresos")
	public String mostrarIngreso(HttpSession sesion, Model model, @PathVariable("idCuenta") int idCuenta) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "ingreso";
	}

	/**
	 * Procesa el ingreso en una cuenta y crea un nuevo movimiento.
	 * Establece los parámetros del movimiento como un ingreso y actualiza el saldo de la cuenta
	 * en la que tiene lugar el movimiento.
	 * Al finalizar el proceso dirige al menú principal. 
	 * @param movimiento
	 * @param sesion
	 * @param model
	 * @param idCuenta
	 * @return
	 */
	@PostMapping("/cuentas/{idCuenta}/ingresos")
	public String ingresarDinero(Movimiento movimiento, HttpSession sesion, Model model, @PathVariable("idCuenta") int idCuenta) {
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
	
	/**
	 * Muestra la vista para hacer retiradas de efectivo en la cuenta que se ha validado anteriormente.
	 * @param idCuenta
	 * @param sesion
	 * @param model
	 * @return
	 */
	@GetMapping("/cuentas/{idCuenta}/retiradas")
	public String mostrarRetirada(@PathVariable("idCuenta") int idCuenta, HttpSession sesion, Model model) {
		System.out.println("En retiradas");
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		return "retirada";
	}
	
	/**
	 * Procesa la retirada de efectivo de una cuenta y crea un nuevo movimiento.
	 * Establece los parámetros del movimiento como una extracción y actualiza el saldo de la cuenta
	 * en la que tiene lugar el movimiento.
	 * Al finalizar el proceso dirige al menú principal. 
	 * @param movimiento
	 * @param sesion
	 * @param model
	 * @param idCuenta
	 * @return
	 */
	@PostMapping("/cuentas/{idCuenta}/retiradas")
	public String retirarDinero(Movimiento movimiento, HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		
		boolean saldoSuficiente = cuenta.getSaldo() >= movimiento.getCantidad();
		
		if (saldoSuficiente == false) {
			System.out.println("Saldo insuficiente");
			return "saldoInsuficiente";
		}

		movimiento.setOperacion("extracción");
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
	
	/**
	 * Muestra el listado de movimientos de la cuenta que se ha validado anteriormente.
	 * @param sesion
	 * @param model
	 * @return
	 */
	@GetMapping("/movimientos")
	public String muestraMovimientos(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuenta);
		model.addAttribute("movimientos", movimientoDao.buscarTodos());
		
		return "movimientos";
	}
	
	/**
	 * Muestra la vista para hacer transferencias desde la cuenta que se ha validado anteriormente.
	 * @param idCuenta
	 * @param sesion
	 * @param model
	 * @return
	 */
	@GetMapping("/transferencias")
	public String muestraTransferencia(HttpSession sesion, Model model) {
		Cuenta cuentaOrigen = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuentaOrigen);

		return "transferencias";
	}

	
	/**
	 * Procesa la transferencia desde la cuenta validada a otra cuenta existente.
	 * Comrpueba si la cuenta de destino existe. Si no existe, muestra una vista que informa de ello.
	 * Comprueba si en la cuenta de origen hay saldo suficiente. Si no lo hay, muestra una vista que informa de ello.
	 * Calcula el nuevo saldo para las cuentas de origen y de destino y lo actualiza en los dos casos en base de datos.
	 * Crea el movimiento de ingreso en la cuenta de destino y el movimiento de extracción en la de origen en base de datos.
	 * @param sesion
	 * @param model
	 * @param movimiento
	 * @return
	 */
	@PostMapping("/transferencias") // 
	public String procesaTransferencia(HttpSession sesion, Model model, Movimiento movimiento) {
		Cuenta cuentaOrigen = (Cuenta) sesion.getAttribute("cuentaValida");
		model.addAttribute("cuenta", cuentaOrigen);
		
		if (movimiento.getCuenta() == null)
			return "cuentaNoExiste";

		Cuenta cuentaDestino = cuentaDao.buscarPorId(movimiento.getCuenta().getIdCuenta());
		
		boolean saldoSuficiente = cuentaOrigen.getSaldo() >= movimiento.getCantidad();
		 if (saldoSuficiente == false)
			 return "saldoInsuficiente";
		
		double importeTransferencia = movimiento.getCantidad();
		
		double saldoInicialCuentaOrigen = cuentaOrigen.getSaldo();
		double saldoInicialCuentaDestino = cuentaDestino.getSaldo();
		
		cuentaOrigen.setSaldo(saldoInicialCuentaOrigen - importeTransferencia);
		cuentaDestino.setSaldo(saldoInicialCuentaDestino + importeTransferencia);
		
		cuentaDao.actualizarCuenta(cuentaOrigen);
		cuentaDao.actualizarCuenta(cuentaDestino);
		
		movimiento.setOperacion("ingreso");
		movimiento.setFecha(new Date());
		
		Movimiento movimientoCuentaOrigen = new Movimiento();
		movimientoCuentaOrigen.setCantidad(movimiento.getCantidad());
		movimientoCuentaOrigen.setCuenta(cuentaOrigen);
		movimientoCuentaOrigen.setFecha(new Date());
		movimientoCuentaOrigen.setOperacion("extracción");
		
		System.out.println(movimientoCuentaOrigen);
		System.out.println(movimiento);
		
		movimientoDao.crearMovimiento(movimientoCuentaOrigen);
		movimientoDao.crearMovimiento(movimiento);
		
		return "redirect:/menu";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
		
	}
}
