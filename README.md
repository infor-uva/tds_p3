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

## TestCode ratio (Actualizar cuando esté finiquitado)
----------------------------------

|Clase|Lineas clase|Lineas de Test|Ratio|
|--|:--:|:--:|:--:|
|Billete                            |  35|  87|~2.48|
|BusRecorrido                       | 124| 221|~1.78|
|Connection                         |  35|  51|~1.45|
|DatabaseManager                    | 256| 228|~0.89|
|SistemaPersistenciaSinAislamiento  | 205| 656|~3.2 |
|SistemaPersistencia                | 213|1320|~6.19|
|System                             | 232| 645|~2.78|
|TrainRecorrido                     | 125| 228|~1.82|
|Usuario                            |  44|  73|~1.65|
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

Refactor
- Refactorizar Recorrido
- Expandir refactorizaciones a las clases afectadas
- Ayudar a conectar la base de datos y a realizar tests para las conexiones

Tiempo empleado:
- Fase Red: `4h 25min` 
- Fase Green: `9h 22min`
- Fase Refactor: `5h 56min`

## Refactorizaciones aplicadas
----------------------------------
### Usar objeto compuesto
En recorrido en vez de pasar y tratar de forma independiente la fecha ([`LocalDate`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)) y hora ([`LocalTime`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalTime.html)) usar la estructura de datos de java que une ambas ([`LocalDateTime`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDateTime.html))

De esta manera logramos reducir la complejidad de la clase y el número de parámetros del constructor de recorrido.
### Objecto parámetro
En recorrido agrupar los parámetros `origin`, `destination` y `duration` en una objeto llamado `Connection` que se encargará de tratarlos y manejarlos.

Esto reduce la complejidad en el constructor de Recorrido reduciendo en número de parámetros y el número de comprobaciones que se realizan en el mismo (la instantiation de esta clase es externa al constructor y por tanto las comprobaciones correspondientes serán entes).
### Clase abstracta padre e clases hijas
La clase recorrido se ha convertido en abstracta para que sus hijos (BusRecorrido y TrainRecorrido) tengan el comportamiento definido de la clase padre teniendo sus diferencias. 

TrainRecorrido tiene un comportamiento especial llamado `getPriceWithDiscount()` en el cual se calcula el precio resultante de aplicar un descuento que este tipo de recorridos tienen.

Además, con esta especialización logramos olvidarnos del tipo de recorrido, ya que cada clase representa un tipo diferente, reduciendo un atributo en la clase recorrido.
### Eliminación de constantes mágicas
Siguiendo uno de los casos anteriores, el descuento es convertido como un valor estático y final de la clase TrainRecorrido 
### Extraer métodos
Sobre todo para algunas comprobaciones repetitivas aplicamos esta refactorización para reducir el número de lineas de código repetido
