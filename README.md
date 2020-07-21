# imagepro

Rest server for image management usign spring boot

Servidor Rest para manipulación de imágenes en Java usando Spring boot

# Requisitos

Git

Maven

jdk 8 o superior 


# Instalación

 - Descargar e instalar Git

 - Descargar e instalar maven 3.5 o superior

 - Descargar e instalar jdk 1.8 o superior


 - Clonar repositorio 

 - git clone https://github.com/jgrateron/imagepro.git


 - Compilar/empaquetar

 - cd imagepro/
 - mvn package


 - Ejecutar jar
 
 - java -jar target/imagepro-0.0.1-SNAPSHOT.jar
 

 - Por defecto el puerto es 8080, se puede cambiar usando la variable de entorno PORT
 
 - export PORT=80
 
# Soporta tres tipos de operaciones
- Resize
- Rotate
- Crop

Se puede enviar la imagen en base64 o la descarga a través de una url.

De fácil configuración de un cluster para paralelizar tareas

# Api Resize
http://localhost:8080/api/v1/resize

```
{
    "width" : "100",
    "height" : "100",
    "typesize" : "px|%",
    "stretch" : "true|false",
    "b64img" : ""
    "urlimg" : ""
}
```
El redimensionado de las imágenes se pueden hacer en base a pixel o porcentajes
El parámetro stretch sirve para estirar la imagen cuando no exista en ancho o el alto 

# Api Rotate
http://localhost:8080/api/v1/rotate

```
{
    "angle" : "90",
    "color" : "0"
    "b64img" : ""
    "urlimg" : ""
}
```
La rotación de imágenes en formato png deja los espacios en transparente si se coloca el parámetro color vacío  
 
# Api Crop
http://localhost:8080/api/v1/crop

```
{
    "x" : "0",
    "y" : "0",
    "width" : "100",
    "height" : "100",
    "b64img" : ""
    "urlimg" : ""
}
```

# Retorno

```
{
	"success" : "true|false",
	"message" : "en caso de false",
	"imagen" : "base64",
	"formatName" : "jpeg|png"
}
```

# Seguridad

Se agrega el módulo spring security con usuario "user" clave "123456"
Desde Postman o un cliente se debe agregar el tipo de seguridad "Authorization: Basic"
https://developer.mozilla.org/es/docs/Web/HTTP/Authentication


