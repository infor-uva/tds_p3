# TDS_PRACTICA1_GRUPO6
## Resumen

Este proyecto tiene como fin la creación de un sistema de gestión de usuarios, billetes y recorridos asociados a él.<br>
Este proyecto esta formado por las siguientes clases:

### [`Billete.java`](./src/main/java/uva/tds/practica2_grupo6/Billete.java)
Esta clase representa los billetes del supuesto, conformada por varios atributos como el usuario al que pertenece, el recorrido asociado y su localizador único. Además, cuenta con un estado para diferenciar si está reservado o comprado. <br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/Billete.java) 

### [`Recorrido.java`](./src/main/java/uva/tds/practica2_grupo6/Recorrido.java)
Clase encargada de representar y manejar la información de los recorridos (o rutas), esta cuenta con un origen y un destino, una duración, un precio, un identificador, un  medio de transporte y la hora y fecha de inicio del viaje. Con esta clase se puede conocer el número de asientos disponibles, ya sean los totales o los que actualmente están disponibles, y número de asientos que no hayan sido comprados o reservados. <br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/Recorrido.java)

### [`Usuario.java`](./src/main/java/uva/tds/practica2_grupo6/Usuario.java)
Esta clase representa los usuarios del supuesto. La información acerca del usuario son su nombre y su identificador único. Será este usuario el que esté asociado a un billete.<br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/Usuario.java)

### [`System.java`](./src/main/java/uva/tds/practica2_grupo6/System.java)
Clase encargada de la gestión del conjunto de clases: recorrido, usuario y billetes.

El sistema ofrece servicios de compra y reserva de billetes para un viaje (recorrido), con sus respectivas gestiones (devolución de billete y anulación de reserva respectivamente). 

Además, el sistema ofrece consultar información generada a través de las operaciones anteriores, como el precio total que le corresponde a un usuario en concreto (en base a sus billetes reservados y comprados) y el listado de recorridos en una fecha determinada.
<br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/System.java)

### [`IDatabaseManager.java`](./src/main/java/uva/tds/practica2_grupo6/IDatabaseManager.java)
La interfaz de la base de datos externa. 
<br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/IDatabaseManager.java)

### [`SistemaPersistencia.java`](./src/main/java/uva/tds/practica2_grupo6/SistemaPersistencia.java)
Sistema que delega la gestión de la información en una Database externa. 
<br>[`+ información →`](./src/main/java/uva/tds/practica2_grupo6/SistemaPersistencia.java)

### [Tests](./src/test/java/uva/tds/practica2_grupo6/)
Las clases anteriormente mencionadas estarán testeadas mediante le método Test-First o TDD (Test Driven Development), en base a las restricciones planteadas en el supuesto.
<br>[`+ información →`](./src/test/java/uva/tds/practica2_grupo6/)

## TestCode ratio
----------------------------------

|Clase|Lineas clase|Lineas de Test|Ratio|
|--|:--:|:--:|:--:|
|Billete|33|91|~2.75|
|Usuario|40|75|~1.88|
|Recorrido|131|434|~3.31|
|System|232|646|~2.78|
|SistemaPersistencia|209|1224|~5.86|

## Tiempo utilizado
----------------------------------
### Diego Bombín Sanz
Trabajo asignado:
- Feature 1 - Usuario
- Feature 5.1 - Comprar billetes
- Feature 7 - Obtención precio total billetes de un usuario y obtención recorridos disponibles en una fecha

Tiempo empleado: 
- Fase Red: `4h 30min`
- Fase green: `10h 36min`
    
### Hugo Cubino Cubino
Trabajo asignado:
- Feature 3 - Billete
- Feature 5.2 - Devolver billetes
- Feature 6 - Reserva y anulación de reserva de billetes

Tiempo empleado
- Fase red: `3h 55min`
- Fase green: `14h 45min`

### Miguel de las Moras Sastre
Trabajo asignado:
- Feature 2 - Recorrido
- Feature 4 - Gestión de recorridos (añadir, eliminar y actualizar fecha y/o hora)
- Feature 5.3 - Comprar billetes reservados

Tiempo empleado:
- Fase Red: `4h 25min` 
- Fase Green: `9h 22min`

