<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>UF4_Cajero_SAG</title>
</head>
<body>
	<h1>Cajero</h1>
	
	<h3>Cuenta ${cuenta.getIdCuenta() }</h3>
	
	<p><a href="/cuentas/${cuenta.getIdCuenta() }/ingresos">Ingresar</a></p>
	<p><a href="/cuentas/${cuenta.getIdCuenta() }/retiradas">Retirar</a></p>
	<p><a href="/movimientos">Ver movimientos</a></p>
	<p><a href="/transferencias">Transferencia</a></p>
	
</body>
</html>