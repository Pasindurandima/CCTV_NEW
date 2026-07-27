const API_BASE_URL = import.meta.env.VITE_API_URL;

if (!API_BASE_URL) {
  throw new Error('VITE_API_URL is not defined. Set it in .env.development or .env.production.');
}

const cleanBaseUrl = API_BASE_URL.replace(/\/+$/, '');

export async function apiFetch(path, init) {
  const url = `${cleanBaseUrl}${path}`;
  return fetch(url, init);
}

export function buildApiUrl(path) {
  return `${cleanBaseUrl}${path}`;
}
