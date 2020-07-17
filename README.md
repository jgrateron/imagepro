# imagepro
Servidor Rest para manipulación de imágenes en Java

Soporta tres tipos de operaciones
Resize
Rotate
Crop

Se puede enviar la imagen como base64 o pasar la url del mismo

Api Resize
http://localhost:8080/api/v1/resize
  {
    "width" : "100",
    "height" : "100",
    "typesize" : "px|%",
    "stretch" : "true|false",
    "b64img" : ""
    "urlimg" : ""
}

