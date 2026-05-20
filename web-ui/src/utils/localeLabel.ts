export function localeLabel(locale: string): string {
  const idx = locale.indexOf('_');
  if (idx === -1) return locale;
  return locale.substring(0, idx) + ' (' + locale.substring(idx + 1) + ')';
}
