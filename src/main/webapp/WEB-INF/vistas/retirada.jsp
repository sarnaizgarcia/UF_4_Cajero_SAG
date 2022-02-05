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
	
	<form action="/cuentas/${cuenta.getIdCuenta() }/retiradas" method="post">
		Introduce cantidad:
		<input type="number" step="0.01" name="cantidad">
		<input type="submit" value="Entrar">
	</form>
	
	<c:if test="${saldoSuficiente == false }">
		<p>${mensaje }</p>
	</c:if>
	
	<p><a href="/menu">Volver</a></p>
	
</body>
</html>