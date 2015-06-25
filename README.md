TRABAJO PRACTICO SIMULACION DE SISTEMAS


Requerimientos:
	- Java 8 instalado
	- Maven 3 (en caso que se quiera compilar)

Compilación (opcional):
	Aclaración: Esto debe hacerse solamente si se hacen cambios al código fuente, caso contrario el ejecutable se encuentra dentro del repositorio.

	Ejecutar "mvn clean package" en el root del repositorio.

Forma de uso:

	Ejecutar en línea de comandos: java -jar target/SS.jar [OPTS]
	Donde OPTS puede ser:
		-p				Indica que se utilizará Packet Switching
		-c				Indica que se utilizará Circuit Switching
		-l <value>		Indica que la media de envío de paquetes será <value>
		-b <value>		Indica <value> como el ancho de banda de los routers
		-tr <value>		Indica <value> como la tasa de transferencia de los routers
		-k <value>		Indica que para Packet Switching, los routers estarán dividos en <value> canales
		-u	<value>		Indica que habrá <value> users enviando paquetes
		-r <value>		Indica que la red estará compuesta por <value> routers
		-t <value>		Indica que se ejecutarán <value> ciclos de simulación
