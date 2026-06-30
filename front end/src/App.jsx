import React, { useState, useEffect, useCallback } from 'react';
import {
  cursosApi,
  asignaturasApi,
  asistenciaApi,
  anotacionesApi,
  mensajesApi,
} from './api';
import { getToken, logout, login as authLogin } from './auth';

// ============ Paleta y estilos compartidos ============
// Identidad institucional sobria: azul marino, dorado heraldico y crema.
const C = {
  primary: '#13294b',      // azul marino profundo
  primaryDark: '#0c1c34',  // marino casi negro (degradado / hover)
  accent: '#a4843f',       // dorado heraldico
  accentDark: '#856a30',   // dorado oscuro
  bg: '#f4f1ea',           // crema / blanco hueso
  surface: '#ffffff',
  border: '#ddd6c8',       // borde calido sutil
  text: '#1d2433',         // tinta azul-negra
  muted: '#6c7280',        // gris sobrio
  success: '#2f6b4f',      // verde bosque
  successBg: '#e7efe9',
  danger: '#8c2f2f',       // granate / burdeos
  dangerBg: '#f1e5e5',
  warning: '#8a661f',      // ambar oscuro
  warningBg: '#f1e9d8',
};

const card = {
  background: C.surface,
  border: `1px solid ${C.border}`,
  borderRadius: '4px',
  padding: '24px',
  boxShadow: '0 1px 2px rgba(19,41,75,0.04)',
};

const input = {
  width: '100%',
  padding: '11px 13px',
  border: `1px solid ${C.border}`,
  borderRadius: '3px',
  fontSize: '14px',
  boxSizing: 'border-box',
  outline: 'none',
  background: '#fdfcf9',
  fontFamily: 'inherit',
  color: C.text,
};

const label = {
  display: 'block',
  marginBottom: '6px',
  fontWeight: 600,
  color: C.text,
  fontSize: '12px',
  textTransform: 'uppercase',
  letterSpacing: '0.04em',
};

const btnPrimary = (disabled) => ({
  padding: '11px 22px',
  background: disabled ? '#9aa0ab' : C.primary,
  color: '#fff',
  border: 'none',
  borderRadius: '3px',
  cursor: disabled ? 'not-allowed' : 'pointer',
  fontSize: '13px',
  fontWeight: 600,
  letterSpacing: '0.03em',
  textTransform: 'uppercase',
  transition: 'background 0.2s',
});

function Badge({ children, tone }) {
  const tones = {
    success: { bg: C.successBg, fg: C.success },
    danger: { bg: C.dangerBg, fg: C.danger },
    warning: { bg: C.warningBg, fg: C.warning },
    neutral: { bg: '#eeeae0', fg: C.muted },
  };
  const t = tones[tone] || tones.neutral;
  return (
    <span style={{
      padding: '3px 10px', borderRadius: '3px', fontSize: '11px',
      fontWeight: 600, background: t.bg, color: t.fg, whiteSpace: 'nowrap',
      textTransform: 'uppercase', letterSpacing: '0.04em',
      border: `1px solid ${t.fg}22`,
    }}>
      {children}
    </span>
  );
}

function EmptyState({ icon, text }) {
  return (
    <div style={{ ...card, textAlign: 'center', color: C.muted, padding: '48px 24px' }}>
      <div style={{ fontSize: '32px', marginBottom: '8px' }}>{icon}</div>
      {text}
    </div>
  );
}

// Escudo heraldico institucional (aguila sobre escudo)
function Crest() {
  return (
    <svg width="46" height="52" viewBox="0 0 46 52" fill="none" xmlns="http://www.w3.org/2000/svg" aria-label="Escudo institucional">
      {/* Cuerpo del escudo */}
      <path d="M2 3 H44 V28 C44 41 34 48 23 51 C12 48 2 41 2 28 Z"
        fill="#13294b" stroke="#a4843f" strokeWidth="2" />
      {/* Banda superior */}
      <path d="M2 3 H44 V13 H2 Z" fill="#a4843f" opacity="0.18" />
      {/* Aguila estilizada */}
      <g fill="#a4843f">
        <path d="M23 17 L27 22 C30 20 33 20 35 22 C32 23 31 25 31 27 L25 25 Z" />
        <path d="M23 17 L19 22 C16 20 13 20 11 22 C14 23 15 25 15 27 L21 25 Z" />
        <path d="M23 19 L25 25 L23 35 L21 25 Z" />
        <circle cx="23" cy="16" r="2.4" />
      </g>
      {/* Estrella inferior */}
      <path d="M23 38 l1.3 2.7 3 .3 -2.2 2 .6 2.9 -2.7 -1.5 -2.7 1.5 .6 -2.9 -2.2 -2 3 -.3 Z" fill="#a4843f" />
    </svg>
  );
}

// =====================================================
//  TABS
// =====================================================
const TABS = [
  { id: 'cursos', label: 'Cursos', icon: '📚' },
  { id: 'asignaturas', label: 'Asignaturas', icon: '📘' },
  { id: 'asistencia', label: 'Asistencia', icon: '✓' },
  { id: 'anotaciones', label: 'Anotaciones', icon: '📝' },
  { id: 'comunicaciones', label: 'Notificaciones', icon: '🔔' },
];

export default function App() {
  const [tab, setTab] = useState('cursos');
  const [toast, setToast] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(!!getToken());

  const notify = useCallback((msg, tone = 'success') => {
    setToast({ msg, tone });
    setTimeout(() => setToast(null), 3500);
  }, []);

  // Si no está logueado, mostrar pantalla de login
  if (!isLoggedIn) {
    return <LoginPage onLoginSuccess={() => setIsLoggedIn(true)} />;
  }

  return (
    <div style={{ minHeight: '100vh', background: C.bg, color: C.text }}>
      {/* Header */}
      <header style={{ background: C.primary, color: '#fff' }}>
        <div style={{ maxWidth: '1080px', margin: '0 auto', padding: '24px', display: 'flex', alignItems: 'center', gap: '16px', justifyContent: 'space-between' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <Crest />
            <div>
              <h1 style={{
                margin: 0, fontSize: '23px', fontWeight: 600,
                fontFamily: 'Georgia, "Times New Roman", serif', letterSpacing: '0.01em',
              }}>
                Libro de Clases Digital
              </h1>
              <p style={{
                margin: '4px 0 0', fontSize: '12px', letterSpacing: '0.16em',
                textTransform: 'uppercase', color: C.accent, fontWeight: 600,
              }}>
                Colegio Bernardo O'Higgins
              </p>
            </div>
          </div>
          <button onClick={() => { logout(); setIsLoggedIn(false); }} 
            style={{
              padding: '8px 16px',
              background: 'rgba(255,255,255,0.2)',
              color: '#fff',
              border: `1px solid ${C.accent}`,
              borderRadius: '3px',
              cursor: 'pointer',
              fontSize: '11px',
              fontWeight: 600,
              textTransform: 'uppercase',
              letterSpacing: '0.04em',
            }}>
            Salir
          </button>
        </div>
        {/* Banda dorada institucional */}
        <div style={{ height: '3px', background: C.accent }} />
      </header>

      {/* Tabs */}
      <nav style={{ background: C.surface, borderBottom: `1px solid ${C.border}`, position: 'sticky', top: 0, zIndex: 10 }}>
        <div style={{ maxWidth: '1080px', margin: '0 auto', padding: '0 24px', display: 'flex', gap: '2px', overflowX: 'auto' }}>
          {TABS.map(t => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              style={{
                padding: '15px 18px', border: 'none', background: 'transparent',
                color: tab === t.id ? C.primary : C.muted,
                borderBottom: tab === t.id ? `3px solid ${C.accent}` : '3px solid transparent',
                cursor: 'pointer', fontSize: '12px', fontWeight: 600,
                whiteSpace: 'nowrap', fontFamily: 'inherit',
                textTransform: 'uppercase', letterSpacing: '0.05em',
              }}
            >
              {t.label}
            </button>
          ))}
        </div>
      </nav>

      {/* Toast */}
      {toast && (
        <div style={{
          position: 'fixed', top: '78px', right: '24px', zIndex: 100,
          background: toast.tone === 'danger' ? C.danger : C.success,
          color: '#fff', padding: '13px 20px', borderRadius: '3px',
          fontSize: '14px', fontWeight: 500, maxWidth: '340px',
          boxShadow: '0 8px 24px rgba(0,0,0,0.18)',
          borderLeft: `3px solid ${C.accent}`,
        }}>
          {toast.msg}
        </div>
      )}

      {/* Content */}
      <main style={{ maxWidth: '1080px', margin: '0 auto', padding: '32px 24px 64px' }}>
        {tab === 'cursos' && <CursosView notify={notify} />}
        {tab === 'asignaturas' && <AsignaturasView notify={notify} />}
        {tab === 'asistencia' && <AsistenciaView notify={notify} />}
        {tab === 'anotaciones' && <AnotacionesView notify={notify} />}
        {tab === 'comunicaciones' && <ComunicacionesView notify={notify} />}
      </main>

      <footer style={{ borderTop: `3px solid ${C.accent}`, background: C.primary, padding: '20px', textAlign: 'center', color: '#cdd4df', fontSize: '11px', letterSpacing: '0.06em', textTransform: 'uppercase' }}>
        DSY1106 · Plataforma de Gestion Academica · Spring Boot · React · RabbitMQ
      </footer>
    </div>
  );
}

// =====================================================
//  SECCION: CURSOS  (gestionacademica 8081)
// =====================================================
function CursosView({ notify }) {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ nombre: '', nivel: '', anioLectivo: '' });
  const [showForm, setShowForm] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setLoading(true);
      setCursos(await cursosApi.listar() || []);
    } catch (e) {
      notify('No se pudo conectar con gestionacademica (8081)', 'danger');
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => { cargar(); }, [cargar]);

  const crear = async (e) => {
    e.preventDefault();
    if (!form.nombre.trim()) return notify('El nombre es obligatorio', 'danger');
    try {
      await cursosApi.crear(form);
      setForm({ nombre: '', nivel: '', anioLectivo: '' });
      setShowForm(false);
      notify('Curso creado correctamente');
      cargar();
    } catch {
      notify('Error al crear el curso', 'danger');
    }
  };

  const eliminar = async (id) => {
    if (!window.confirm('¿Eliminar este curso?')) return;
    try {
      await cursosApi.eliminar(id);
      notify('Curso eliminado');
      cargar();
    } catch {
      notify('Error al eliminar', 'danger');
    }
  };

  return (
    <div>
      <SectionHeader
        title="Cursos"
        subtitle="Gestiona los cursos del colegio (ej: 1ro Medio A)"
        action={
          <button onClick={() => setShowForm(s => !s)} style={btnPrimary(false)}>
            {showForm ? '✕ Cancelar' : '+ Nuevo curso'}
          </button>
        }
      />

      {showForm && (
        <form onSubmit={crear} style={{ ...card, marginBottom: '24px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '16px', marginBottom: '18px' }}>
            <div>
              <label style={label}>Nombre del curso *</label>
              <input style={input} value={form.nombre}
                onChange={e => setForm({ ...form, nombre: e.target.value })}
                placeholder="1ro Medio A" />
            </div>
            <div>
              <label style={label}>Nivel</label>
              <input style={input} value={form.nivel}
                onChange={e => setForm({ ...form, nivel: e.target.value })}
                placeholder="Enseñanza Media" />
            </div>
            <div>
              <label style={label}>Año lectivo</label>
              <input style={input} value={form.anioLectivo}
                onChange={e => setForm({ ...form, anioLectivo: e.target.value })}
                placeholder="2026" />
            </div>
          </div>
          <button type="submit" style={btnPrimary(false)}>✓ Guardar curso</button>
        </form>
      )}

      {loading ? <EmptyState icon="⏳" text="Cargando cursos..." />
        : cursos.length === 0 ? <EmptyState icon="📭" text="No hay cursos. Crea el primero." />
          : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: '16px' }}>
              {cursos.map(c => (
                <div key={c.id} style={card}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                    <h3 style={{ margin: '0 0 4px', color: C.primary, fontSize: '17px' }}>{c.nombre}</h3>
                    <Badge tone="neutral">#{c.id}</Badge>
                  </div>
                  <p style={{ margin: '0 0 4px', color: C.muted, fontSize: '13px' }}>
                    {c.nivel || 'Sin nivel'} · {c.anioLectivo || 's/año'}
                  </p>
                  <p style={{ margin: '0 0 16px', color: C.muted, fontSize: '13px' }}>
                    {(c.asignaturas?.length || 0)} asignatura(s)
                  </p>
                  <button onClick={() => eliminar(c.id)} style={{
                    padding: '7px 14px', background: 'transparent', color: C.danger,
                    border: `1px solid ${C.danger}`, borderRadius: '8px', cursor: 'pointer',
                    fontSize: '13px', fontWeight: 600, fontFamily: 'inherit',
                  }}>
                    Eliminar
                  </button>
                </div>
              ))}
            </div>
          )}
    </div>
  );
}

// =====================================================
//  SECCION: ASIGNATURAS
// =====================================================
function AsignaturasView({ notify }) {
  const [asignaturas, setAsignaturas] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ nombre: '', docente: '', cursoId: '' });
  const [showForm, setShowForm] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setLoading(true);
      const [a, c] = await Promise.all([asignaturasApi.listar(), cursosApi.listar()]);
      setAsignaturas(a || []);
      setCursos(c || []);
    } catch {
      notify('No se pudo conectar con gestionacademica (8081)', 'danger');
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => { cargar(); }, [cargar]);

  const crear = async (e) => {
    e.preventDefault();
    if (!form.nombre.trim() || !form.cursoId) return notify('Nombre y curso son obligatorios', 'danger');
    try {
      await asignaturasApi.crear(form.cursoId, { nombre: form.nombre, docente: form.docente });
      setForm({ nombre: '', docente: '', cursoId: '' });
      setShowForm(false);
      notify('Asignatura creada');
      cargar();
    } catch {
      notify('Error al crear la asignatura', 'danger');
    }
  };

  return (
    <div>
      <SectionHeader
        title="Asignaturas"
        subtitle="Materias impartidas dentro de cada curso"
        action={
          <button onClick={() => setShowForm(s => !s)} style={btnPrimary(false)}>
            {showForm ? '✕ Cancelar' : '+ Nueva asignatura'}
          </button>
        }
      />

      {showForm && (
        <form onSubmit={crear} style={{ ...card, marginBottom: '24px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '16px', marginBottom: '18px' }}>
            <div>
              <label style={label}>Nombre *</label>
              <input style={input} value={form.nombre}
                onChange={e => setForm({ ...form, nombre: e.target.value })}
                placeholder="Matematica" />
            </div>
            <div>
              <label style={label}>Docente</label>
              <input style={input} value={form.docente}
                onChange={e => setForm({ ...form, docente: e.target.value })}
                placeholder="Prof. Gonzalez" />
            </div>
            <div>
              <label style={label}>Curso *</label>
              <select style={input} value={form.cursoId}
                onChange={e => setForm({ ...form, cursoId: e.target.value })}>
                <option value="">Selecciona...</option>
                {cursos.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
              </select>
            </div>
          </div>
          <button type="submit" style={btnPrimary(false)}>✓ Guardar asignatura</button>
        </form>
      )}

      {loading ? <EmptyState icon="⏳" text="Cargando..." />
        : asignaturas.length === 0 ? <EmptyState icon="📭" text="No hay asignaturas." />
          : <DataTable
            cols={['Asignatura', 'Docente', 'Curso']}
            rows={asignaturas.map(a => [
              a.nombre,
              a.docente || '—',
              cursos.find(c => c.id === a.curso?.id)?.nombre || (a.curso?.nombre ?? '—'),
            ])}
          />}
    </div>
  );
}

// =====================================================
//  SECCION: ASISTENCIA  (asistencia 8082)
// =====================================================
function AsistenciaView({ notify }) {
  const [registros, setRegistros] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const hoy = new Date().toISOString().split('T')[0];
  const [form, setForm] = useState({
    estudianteNombre: '', estudianteId: '', cursoId: '', fecha: hoy, estado: 'PRESENTE',
  });

  const cargar = useCallback(async () => {
    try {
      setLoading(true);
      const [r, c] = await Promise.all([
        asistenciaApi.listar(),
        cursosApi.listar().catch(() => []),
      ]);
      setRegistros(r || []);
      setCursos(c || []);
    } catch {
      notify('No se pudo conectar con asistencia (8082)', 'danger');
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => { cargar(); }, [cargar]);

  const registrar = async (e) => {
    e.preventDefault();
    if (!form.estudianteNombre.trim()) return notify('El nombre del estudiante es obligatorio', 'danger');
    try {
      await asistenciaApi.registrar({
        estudianteId: form.estudianteId ? Number(form.estudianteId) : null,
        estudianteNombre: form.estudianteNombre,
        cursoId: form.cursoId ? Number(form.cursoId) : null,
        fecha: form.fecha,
        estado: form.estado,
      });
      setForm({ ...form, estudianteNombre: '', estudianteId: '' });
      notify('Asistencia registrada');
      cargar();
    } catch {
      notify('Error al registrar asistencia', 'danger');
    }
  };

  const estadoTone = (e) => ({
    PRESENTE: 'success', AUSENTE: 'danger', ATRASO: 'warning', JUSTIFICADO: 'neutral',
  }[e] || 'neutral');
  return (
    <div>
      <SectionHeader title="Asistencia" subtitle="Registro diario de asistencia por estudiante" />

      <form onSubmit={registrar} style={{ ...card, marginBottom: '24px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: '16px', marginBottom: '18px' }}>
          <div>
            <label style={label}>Estudiante *</label>
            <input style={input} value={form.estudianteNombre}
              onChange={e => setForm({ ...form, estudianteNombre: e.target.value })}
              placeholder="Juan Perez" />
          </div>
          <div>
            <label style={label}>ID estudiante</label>
            <input style={input} type="number" value={form.estudianteId}
              onChange={e => setForm({ ...form, estudianteId: e.target.value })}
              placeholder="1" />
          </div>
          <div>
            <label style={label}>Curso</label>
            <select style={input} value={form.cursoId}
              onChange={e => setForm({ ...form, cursoId: e.target.value })}>
              <option value="">—</option>
              {cursos.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
            </select>
          </div>
          <div>
            <label style={label}>Fecha</label>
            <input style={input} type="date" value={form.fecha}
              onChange={e => setForm({ ...form, fecha: e.target.value })} />
          </div>
          <div>
            <label style={label}>Estado</label>
            <select style={input} value={form.estado}
              onChange={e => setForm({ ...form, estado: e.target.value })}>
              <option value="PRESENTE">Presente</option>
              <option value="AUSENTE">Ausente</option>
              <option value="ATRASO">Atrasado</option>
              <option value="JUSTIFICADO">Justificado</option>
            </select>
          </div>
        </div>
        <button type="submit" style={btnPrimary(false)}>✓ Registrar asistencia</button>
      </form>

      {loading ? <EmptyState icon="⏳" text="Cargando..." />
        : registros.length === 0 ? <EmptyState icon="📭" text="Sin registros de asistencia." />
          : (
            <div style={{ ...card, padding: 0, overflow: 'hidden' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px' }}>
                <thead>
                  <tr style={{ background: '#f6f3ec' }}>
                    {['Estudiante', 'Curso', 'Fecha', 'Estado'].map(h => (
                      <th key={h} style={{ padding: '14px 16px', textAlign: 'left', fontWeight: 600, color: C.text, borderBottom: `1px solid ${C.border}` }}>{h}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {registros.map((r, i) => (
                    <tr key={r.id} style={{ background: i % 2 ? '#faf8f2' : '#fff' }}>
                      <td style={{ padding: '13px 16px' }}>{r.estudianteNombre || `#${r.estudianteId}`}</td>
                      <td style={{ padding: '13px 16px', color: C.muted }}>{r.cursoId ? `Curso ${r.cursoId}` : '—'}</td>
                      <td style={{ padding: '13px 16px', color: C.muted }}>{r.fecha}</td>
                      <td style={{ padding: '13px 16px' }}><Badge tone={estadoTone(r.estado)}>{r.estado}</Badge></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
    </div>
  );
}

// =====================================================
//  SECCION: ANOTACIONES  (flujo RabbitMQ estrella)
// =====================================================
function AnotacionesView({ notify }) {
  const hoy = new Date().toISOString().slice(0, 16);
  const [form, setForm] = useState({
    estudianteNombre: '', estudianteId: '', apoderadoId: '',
    descripcion: '', docente: '', tipo: 'POSITIVA', fecha: hoy,
  });
  const [ultimaEnviada, setUltimaEnviada] = useState(null);

  const registrar = async (e) => {
    e.preventDefault();
    if (!form.estudianteNombre.trim() || !form.descripcion.trim())
      return notify('Estudiante y descripcion son obligatorios', 'danger');
    try {
      const saved = await anotacionesApi.registrar({
        estudianteId: form.estudianteId ? Number(form.estudianteId) : null,
        estudianteNombre: form.estudianteNombre,
        apoderadoId: form.apoderadoId ? Number(form.apoderadoId) : null,
        descripcion: form.descripcion,
        docente: form.docente,
        tipo: form.tipo,
        fecha: form.fecha,
      });
      setUltimaEnviada(saved);
      const disparaEvento = form.tipo === 'NEGATIVA' || form.tipo === 'GRAVE';
      notify(disparaEvento
        ? '✓ Anotacion registrada · evento enviado a RabbitMQ'
        : '✓ Anotacion positiva registrada');
      setForm({ ...form, estudianteNombre: '', descripcion: '' });
    } catch {
      notify('Error al registrar anotacion', 'danger');
    }
  };

  const disparaEvento = form.tipo === 'NEGATIVA' || form.tipo === 'GRAVE';

  return (
    <div>
      <SectionHeader title="Anotaciones de conducta" subtitle="Las anotaciones negativas o graves notifican automaticamente al apoderado" />

      {/* Explicacion del flujo */}
      <div style={{
        ...card, marginBottom: '24px', background: '#faf7f0',
        borderLeft: `3px solid ${C.accent}`, display: 'flex', gap: '14px', alignItems: 'center',
        flexWrap: 'wrap',
      }}>
        <span style={{ fontSize: '12px', fontWeight: 600, color: C.accentDark, textTransform: 'uppercase', letterSpacing: '0.04em' }}>Flujo asincrono:</span>
        <span style={{ fontSize: '13px', color: C.text }}>
          Asistencia (8082) <b>publica evento</b> → RabbitMQ → Comunicaciones (8083) <b>genera notificacion</b>
        </span>
        <span style={{ fontSize: '12px', color: C.muted }}>
          Solo se dispara con anotaciones NEGATIVA o GRAVE.
        </span>
      </div>

      <form onSubmit={registrar} style={{ ...card, marginBottom: '24px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(170px, 1fr))', gap: '16px', marginBottom: '18px' }}>
          <div>
            <label style={label}>Estudiante *</label>
            <input style={input} value={form.estudianteNombre}
              onChange={e => setForm({ ...form, estudianteNombre: e.target.value })}
              placeholder="Juan Perez" />
          </div>
          <div>
            <label style={label}>ID estudiante</label>
            <input style={input} type="number" value={form.estudianteId}
              onChange={e => setForm({ ...form, estudianteId: e.target.value })} placeholder="1" />
          </div>
          <div>
            <label style={label}>ID apoderado</label>
            <input style={input} type="number" value={form.apoderadoId}
              onChange={e => setForm({ ...form, apoderadoId: e.target.value })} placeholder="100" />
          </div>
          <div>
            <label style={label}>Docente</label>
            <input style={input} value={form.docente}
              onChange={e => setForm({ ...form, docente: e.target.value })} placeholder="Prof. Soto" />
          </div>
          <div>
            <label style={label}>Tipo</label>
            <select style={{ ...input, borderColor: disparaEvento ? C.danger : C.border }} value={form.tipo}
              onChange={e => setForm({ ...form, tipo: e.target.value })}>
              <option value="POSITIVA">Positiva</option>
              <option value="NEGATIVA">Negativa (notifica)</option>
              <option value="GRAVE">Grave (notifica)</option>
            </select>
          </div>
        </div>
        <div style={{ marginBottom: '18px' }}>
          <label style={label}>Descripcion *</label>
          <textarea style={{ ...input, minHeight: '80px', resize: 'vertical' }} value={form.descripcion}
            onChange={e => setForm({ ...form, descripcion: e.target.value })}
            placeholder="Detalle de la anotacion..." />
        </div>
        <button type="submit" style={btnPrimary(false)}>
          {disparaEvento ? '✓ Registrar y notificar al apoderado' : '✓ Registrar anotacion'}
        </button>
      </form>

      {ultimaEnviada && (
        <div style={{ ...card, borderLeft: `4px solid ${C.success}` }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
            <Badge tone="success">Registrada</Badge>
            <span style={{ fontWeight: 600 }}>Anotacion #{ultimaEnviada.id}</span>
          </div>
          <p style={{ margin: 0, color: C.muted, fontSize: '13px' }}>
            {ultimaEnviada.estudianteNombre} · {ultimaEnviada.tipo} · {ultimaEnviada.descripcion}
          </p>
          {(ultimaEnviada.tipo === 'NEGATIVA' || ultimaEnviada.tipo === 'GRAVE') && (
            <p style={{ margin: '8px 0 0', color: C.accent, fontSize: '13px', fontWeight: 600 }}>
              → Revisa la pestaña Notificaciones para ver el mensaje generado por RabbitMQ
            </p>
          )}
        </div>
      )}
    </div>
  );
}

// =====================================================
//  SECCION: COMUNICACIONES / NOTIFICACIONES (8083)
// =====================================================
function ComunicacionesView({ notify }) {
  const [mensajes, setMensajes] = useState([]);
  const [loading, setLoading] = useState(true);

  const cargar = useCallback(async () => {
    try {
      setLoading(true);
      setMensajes(await mensajesApi.listar() || []);
    } catch {
      notify('No se pudo conectar con comunicaciones (8083)', 'danger');
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => { cargar(); }, [cargar]);

const canalTone = (c) => ({ APP: 'neutral', EMAIL: 'success', SMS: 'warning' }[c] || 'neutral');
  const estadoTone = (e) => ({ PENDIENTE: 'warning', ENVIADO: 'success', LEIDO: 'neutral' }[e] || 'neutral');

  return (
    <div>
      <SectionHeader
        title="Notificaciones"
        subtitle="Mensajes generados automaticamente desde eventos de RabbitMQ"
        action={<button onClick={cargar} style={btnPrimary(false)}>↻ Actualizar</button>}
      />

      {loading ? <EmptyState icon="⏳" text="Cargando notificaciones..." />
        : mensajes.length === 0 ? <EmptyState icon="🔕" text="Sin notificaciones. Registra una anotacion negativa/grave para generar una." />
          : (
            <div style={{ display: 'grid', gap: '14px' }}>
              {mensajes.slice().reverse().map(m => (
                <div key={m.id} style={card}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '8px', gap: '12px', flexWrap: 'wrap' }}>
                    <h3 style={{ margin: 0, fontSize: '15px', color: C.primary }}>{m.asunto}</h3>
                    <div style={{ display: 'flex', gap: '6px' }}>
                      <Badge tone={canalTone(m.canal)}>{m.canal}</Badge>
                      <Badge tone={estadoTone(m.estado)}>{m.estado}</Badge>
                    </div>
                  </div>
                  <p style={{ margin: '0 0 8px', fontSize: '14px' }}>{m.contenido}</p>
                  <p style={{ margin: 0, fontSize: '12px', color: C.muted }}>
                    Para: {m.destinatarioNombre || `#${m.destinatarioId}`}
                    {m.fechaCreacion && ` · ${new Date(m.fechaCreacion).toLocaleString('es-CL')}`}
                  </p>
                </div>
              ))}
            </div>
          )}
    </div>
  );
}

// =====================================================
//  Componentes auxiliares
// =====================================================
function SectionHeader({ title, subtitle, action }) {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'end', marginBottom: '24px', gap: '16px', flexWrap: 'wrap', borderBottom: `1px solid ${C.border}`, paddingBottom: '14px' }}>
      <div>
        <h2 style={{
          margin: '0 0 5px', fontSize: '24px', color: C.primary,
          fontFamily: 'Georgia, "Times New Roman", serif', fontWeight: 600,
        }}>
          {title}
          <span style={{ display: 'block', width: '38px', height: '2px', background: C.accent, marginTop: '8px' }} />
        </h2>
        {subtitle && <p style={{ margin: '8px 0 0', color: C.muted, fontSize: '14px' }}>{subtitle}</p>}
      </div>
      {action}
    </div>
  );
}

function DataTable({ cols, rows }) {
  return (
    <div style={{ ...card, padding: 0, overflow: 'hidden' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px' }}>
        <thead>
          <tr style={{ background: '#f6f3ec' }}>
            {cols.map(c => (
              <th key={c} style={{ padding: '14px 16px', textAlign: 'left', fontWeight: 600, color: C.text, borderBottom: `1px solid ${C.border}` }}>{c}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((r, i) => (
            <tr key={i} style={{ background: i % 2 ? '#faf8f2' : '#fff' }}>
              {r.map((cell, j) => (
                <td key={j} style={{ padding: '13px 16px', color: j === 0 ? C.text : C.muted }}>{cell}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function LoginPage({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleLogin = async () => {
    if (!username.trim() || !password.trim()) {
      setError('Ingresa usuario y contraseña');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await authLogin(username, password);
      onLoginSuccess();
    } catch (err) {
      setError('Usuario o contraseña incorrectos');
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', background: C.bg, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ ...card, width: '100%', maxWidth: '380px' }}>
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <Crest />
          <h1 style={{ margin: '16px 0 4px', fontSize: '24px', color: C.primary, fontFamily: 'Georgia, serif', fontWeight: 600 }}>
            Libro de Clases
          </h1>
          <p style={{ margin: 0, fontSize: '13px', color: C.muted, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
            Colegio Bernardo O'Higgins
          </p>
        </div>

        <label style={label}>Usuario</label>
        <input
          type="text"
          placeholder="Ingresa tu usuario"
          value={username}
          onChange={(e) => { setUsername(e.target.value); setError(null); }}
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
          style={input}
        />

        <label style={{ ...label, marginTop: '16px' }}>Contraseña</label>
        <input
          type="password"
          placeholder="Ingresa tu contraseña"
          value={password}
          onChange={(e) => { setPassword(e.target.value); setError(null); }}
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
          style={input}
        />

        {error && <p style={{ color: C.danger, fontSize: '12px', marginTop: '8px', marginBottom: '0' }}>{error}</p>}

        <button
          onClick={handleLogin}
          disabled={loading}
          style={{ ...btnPrimary(loading), width: '100%', marginTop: '20px' }}
        >
          {loading ? 'Iniciando...' : 'Ingresar'}
        </button>
      </div>
    </div>
  );
}
