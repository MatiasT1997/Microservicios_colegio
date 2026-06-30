# Frontend — Libro de Clases Digital

Componente frontend de la plataforma, desarrollado en **React**. Ofrece una interfaz de página única (SPA) con autenticación JWT y cinco secciones para gestionar la información académica.

## Requisitos previos

- Node.js 18+ y npm 8+
- eurekaServer, los tres microservicios de dominio (8081, 8082, 8083) y el API Gateway (8080) deben estar en ejecución antes de iniciar el frontend, ya que **todas las peticiones pasan a través del Gateway**

## Tecnologías

- React 18
- Fetch API para el consumo de los servicios REST
- Autenticación con JSON Web Token (JWT) almacenado en el navegador

## Estructura

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── App.jsx        # Componente principal y las 5 secciones
│   ├── api.js         # Endpoints centralizados + header Authorization
│   ├── auth.js        # Login, logout y gestión del token
│   ├── index.js
│   └── index.css
└── package.json
```

## Instalación

```bash
cd frontend
npm install
```

## Ejecución

```bash
npm start
```

La aplicación se abre en `http://localhost:3000`.

## Uso

1. Al iniciar se muestra la **pantalla de login**. Credenciales de demostración: **admin / admin123**.
2. Tras autenticarse, se accede a las cinco secciones:
   - **Cursos** — crear, listar y eliminar cursos
   - **Asignaturas** — crear y listar asignaturas por curso
   - **Asistencia** — registrar asistencia por estudiante
   - **Anotaciones** — registrar anotaciones (las de tipo GRAVE/NEGATIVA notifican automáticamente al apoderado vía RabbitMQ)
   - **Notificaciones** — visualizar los mensajes generados
3. El botón **Salir** cierra la sesión y elimina el token.

## Notas

- El frontend envía el token JWT en la cabecera `Authorization: Bearer {token}` de cada petición.
- En Windows, ejecutar `npm start` desde **CMD** (no PowerShell) evita restricciones de ExecutionPolicy.
- Las URLs de los microservicios se configuran de forma centralizada en `src/api.js`, todas apuntando al API Gateway (`http://localhost:8080/api/...`). El Gateway valida el token JWT antes de reenviar la petición al microservicio correspondiente.
