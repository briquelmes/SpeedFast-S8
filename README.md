![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)
# 🧠 Semana 8 – Desarrollo Orientado a Objetos II

## 👤 Autor del proyecto
- **Nombre completo:** Benjamin Antonio Riquelme Salgado
- **Sección:** 003A
- **Carrera:** Analista Programador Computacional
- **Sede:** Online

---

## 📘 Descripción general del sistema

Este proyecto corresponde a una versión concurrente del sistema de entregas para SpeedFast, una empresa de reparto a domicilio.

El sistema permite realizar operaciones CRUD (crear, leer, actualizar y eliminar) sobre las distintas entidades del modelo utilizando una interfaz gráfica y conexión a base de datos.

El proyecto refleja la aplicación práctica del patrón DAO, fomentando buenas prácticas de diseño, separación de responsabilidades y organización estructurada del código.


---

## 🧱 Estructura general del proyecto

```plaintext
📁 src/
├── main/
    ├── java/
      ├── cl.duoc.speedfast/
         ├── conexion/     # Clase ConexionDB para realizar la conexión con la base de datos.
         ├── modelo/       # Clases de dominio (Pedido, Repartidor, Entrega).
         ├── dao/          # Clases DAO (PedidoDAO, RepartidorDAO, EntregaDAO).
         ├── main/         # Clase principal con el método main.
         └── vista/        # Clases de la interfaz gráfica con cada una de las Ventanas requeridas (VistaPrincipal, VistaPedido, VistaRepartidor, VistaEntrega)

````

---



## ⚙️ Instrucciones para clonar y ejecutar el proyecto

1. Clona el repositorio desde GitHub:

```bash
git clone https://github.com/briquelmes/SpeedFast-S8.git
```

2. Abre el proyecto en IntelliJ IDEA.

3. Verifica la estructura de paquetes.

4. Ejecuta el archivo `Main.java` desde el paquete `main`.

5. Se mostrará una ventana menú para acceder a las funcionalidades del sistema.


---

**Repositorio GitHub:** https://github.com/briquelmes/SpeedFast-S8
**Fecha de entrega:** 01/03/2026

---

© Duoc UC | Escuela de Informática y Telecomunicaciones | Semana 8
