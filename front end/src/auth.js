// Funciones de autenticación y manejo de JWT

export async function login(username, password) {
  try {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    
    if (!response.ok) {
      throw new Error('Login fallido');
    }
    
    const data = await response.json();
    localStorage.setItem('token', data.token);
    window.location.reload();
  } catch (error) {
    console.error('Error en login:', error);
    throw error;
  }
}

export function logout() {
  localStorage.removeItem('token');
  window.location.reload();
}

export function getToken() {
  return localStorage.getItem('token');
}

export function isLoggedIn() {
  return !!getToken();
}
