# imagepro
Servidor Rest para manipulación de imágenes en Java

# Soporta tres tipos de operaciones
- Resize
- Rotate
- Crop

Se puede enviar la imagen en base64 o la url.

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
