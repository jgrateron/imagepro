# imagepro
Servidor Rest para manipulación de imágenes en Java
Rest server for image management

Está realizado usando

Maven
Spring boot
Compatible con jdk 8 o superior 

# Soporta tres tipos de operaciones
- Resize
- Rotate
- Crop

Se puede enviar la imagen en base64 o la url.

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
El redimensionado de las imagenes se pueden hacer en base a pixel o porcentajes

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
La rotación de imagenes en formato png deja los espacios en transparente si se coloca el parámetro color vacío  
 
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
	"formatName" : "png"
}
```
