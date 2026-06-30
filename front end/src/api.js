// =============================================================
//  Configuracion central de las APIs de los microservicios
// =============================================================

import { getToken } from './auth';

const GATEWAY = 'http://localhost:8080';

const HOSTS = {
  gestionacademica: `${GATEWAY}/api/academica`,
  asistencia: `${GATEWAY}/api/asistencia`,
  comunicaciones: `${GATEWAY}/api/comunicaciones`,
};

async function request(url, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` }),
  };

  const res = await fetch(url, { headers, ...options });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`HTTP ${res.status}: ${text || res.statusText}`);
  }
  if (res.status === 204) return null;
  const ct = res.headers.get('content-type') || '';
  return ct.includes('application/json') ? res.json() : null;
}

// ---------- GESTION ACADEMICA (8081) ----------
export const cursosApi = {
  listar: () => request(`${HOSTS.gestionacademica}/curso`),
  crear: (curso) =>
    request(`${HOSTS.gestionacademica}/curso`, {
      method: 'POST',
      body: JSON.stringify(curso),
    }),
  eliminar: (id) =>
    request(`${HOSTS.gestionacademica}/curso/${id}`, { method: 'DELETE' }),
};

export const asignaturasApi = {
  listar: () => request(`${HOSTS.gestionacademica}/asignatura`),
  porCurso: (cursoId) =>
    request(`${HOSTS.gestionacademica}/asignatura/curso/${cursoId}`),
  crear: (cursoId, asignatura) =>
    request(`${HOSTS.gestionacademica}/asignatura`, {
      method: 'POST',
      body: JSON.stringify({ ...asignatura, curso: { id: Number(cursoId) } }),
    }),
};

// ---------- ASISTENCIA (8082) ----------
export const asistenciaApi = {
  listar: () => request(`${HOSTS.asistencia}/asistencia`),
  registrar: (registro) =>
    request(`${HOSTS.asistencia}/asistencia`, {
      method: 'POST',
      body: JSON.stringify({
        estudianteId: registro.estudianteId,
        cursoId: registro.cursoId,
        fecha: registro.fecha,
        estado: registro.estado,
      }),
    }),
};

export const anotacionesApi = {
  registrar: (anotacion) =>
    request(`${HOSTS.asistencia}/anotacion`, {
      method: 'POST',
      body: JSON.stringify({
        estudianteId: anotacion.estudianteId,
        apoderadoId: anotacion.apoderadoId,
        descripcion: anotacion.descripcion,
        tipo: anotacion.tipo,
        fecha: anotacion.fecha,
      }),
    }),
  porEstudiante: (estudianteId) =>
    request(`${HOSTS.asistencia}/anotacion/estudiante/${estudianteId}`),
};

// ---------- COMUNICACIONES (8083) ----------
export const mensajesApi = {
  listar: () => request(`${HOSTS.comunicaciones}/mensaje`),
};