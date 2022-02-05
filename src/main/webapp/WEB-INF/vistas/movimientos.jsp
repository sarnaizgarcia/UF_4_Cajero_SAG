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
	
	<h2>Saldo ${cuenta.getSaldo() }</h2>
	${movimientos}
	<table border="2">
		<tr>
			<th>Cantidad</th>
			<th>Fecha</th>
			<th>Tipo</th>
		</tr>
		<c:forEach var="ele" items="${movimientos}">
			<tr>
				<td>${ele}</td>
				<td>${ele.fecha}</td>
				<td>${ele.precioUnitario}</td>
			</tr>
		</c:forEach>
	</table>

	<p><a href="/menu">Volver</a></p>
	
</body>
</html>