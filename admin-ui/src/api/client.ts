import type { User } from 'firebase/auth';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export async function apiRequest<T>(
  path: string,
  user: User,
  options: RequestInit = {},
): Promise<T> {
  const token = await user.getIdToken();
  const { headers: extraHeaders, ...fetchOptions } = options;

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
    ...(extraHeaders as Record<string, string> | undefined),
  };

  const response = await fetch(`${BASE_URL}/admin${path}`, { ...fetchOptions, headers });

  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.message ?? `Request failed: ${response.status}`);
  }

  const contentLength = response.headers.get('content-length');
  const contentType = response.headers.get('content-type') ?? '';
  if (!contentType.includes('application/json') || contentLength === '0') return undefined as T;
  return response.json() as Promise<T>;
}
