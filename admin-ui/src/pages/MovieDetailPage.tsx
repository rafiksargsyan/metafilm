import { useCallback, useEffect, useRef, useState } from 'react';
import { decode } from 'blurhash';
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Collapse,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SyncIcon from '@mui/icons-material/Sync';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { getMovie, setMovieTmdbId, syncMovie } from '../api/movies';
import type { MovieDetail, MovieTranslation, MovieImage } from '../types';
import { LOCALES } from '../types';

function localeLabel(locale: string): string {
  return LOCALES.find((l) => l.value === locale)?.label ?? locale;
}

function DetailRow({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <Box sx={{ display: 'flex', py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
      <Typography sx={{ width: 160, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>
        {label}
      </Typography>
      <Box sx={{ fontSize: 14, wordBreak: 'break-all' }}>{value ?? <Typography sx={{ fontSize: 14, color: 'text.disabled' }}>—</Typography>}</Box>
    </Box>
  );
}

function BlurhashCanvas({ hash, width, height }: { hash: string; width: number; height: number }) {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    try {
      const pixels = decode(hash, width, height);
      const ctx = canvas.getContext('2d');
      if (!ctx) return;
      const imageData = ctx.createImageData(width, height);
      imageData.data.set(pixels);
      ctx.putImageData(imageData, 0, 0);
    } catch {
      // invalid hash — leave canvas blank
    }
  }, [hash, width, height]);

  return (
    <canvas
      ref={canvasRef}
      width={width}
      height={height}
      style={{ borderRadius: 4, display: 'block' }}
    />
  );
}

function ImageCard({ image }: { image: MovieImage }) {
  const [expanded, setExpanded] = useState(false);
  return (
    <Box sx={{ border: '1px solid', borderColor: 'divider', borderRadius: 1, overflow: 'hidden', mb: 1 }}>
      <Box
        sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 1.5, py: 1, cursor: 'pointer', bgcolor: 'action.hover' }}
        onClick={() => setExpanded((v) => !v)}
      >
        <Chip label={image.type} size="small" variant="outlined" color={image.type === 'POSTER' ? 'primary' : 'secondary'} />
        {expanded ? <ExpandLessIcon fontSize="small" /> : <ExpandMoreIcon fontSize="small" />}
      </Box>
      <Collapse in={expanded}>
        <Box sx={{ p: 1.5 }}>
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', mb: 1.5 }}>
            {image.url && (
              <Box>
                <Typography variant="caption" color="text.secondary" display="block" mb={0.5}>Image</Typography>
                <Box
                  component="img"
                  src={image.url}
                  alt={image.type}
                  sx={{
                    width: image.type === 'POSTER' ? 120 : 213,
                    height: image.type === 'POSTER' ? 180 : 120,
                    objectFit: 'cover',
                    borderRadius: 1,
                    display: 'block',
                  }}
                />
              </Box>
            )}
            {image.blurhash && (
              <Box>
                <Typography variant="caption" color="text.secondary" display="block" mb={0.5}>Blurhash</Typography>
                <BlurhashCanvas
                  hash={image.blurhash}
                  width={image.type === 'POSTER' ? 120 : 213}
                  height={image.type === 'POSTER' ? 180 : 120}
                />
              </Box>
            )}
          </Box>
          {image.blurhash && (
            <Typography variant="body2" sx={{ fontFamily: 'monospace', fontSize: 11, wordBreak: 'break-all', color: 'text.secondary', mb: 1 }}>
              {image.blurhash}
            </Typography>
          )}
          {image.externalPath && (
            <Box>
              <Typography variant="caption" color="text.secondary">
                {image.externalSource} path
              </Typography>
              <Typography variant="body2" sx={{ fontFamily: 'monospace', fontSize: 11, wordBreak: 'break-all' }}>
                {image.externalPath}
              </Typography>
            </Box>
          )}
        </Box>
      </Collapse>
    </Box>
  );
}

function TranslationRow({ translation }: { translation: MovieTranslation }) {
  const [expanded, setExpanded] = useState(false);
  return (
    <>
      <TableRow
        hover
        sx={{ cursor: 'pointer' }}
        onClick={() => setExpanded((v) => !v)}
      >
        <TableCell sx={{ width: 32 }}>
          {expanded ? <ExpandLessIcon fontSize="small" /> : <ExpandMoreIcon fontSize="small" />}
        </TableCell>
        <TableCell>
          <Tooltip title={translation.locale}>
            <span>{localeLabel(translation.locale)}</span>
          </Tooltip>
        </TableCell>
        <TableCell>{translation.title ?? <Typography color="text.disabled" fontSize={14}>—</Typography>}</TableCell>
        <TableCell sx={{ maxWidth: 240, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {translation.overview ?? <Typography color="text.disabled" fontSize={14}>—</Typography>}
        </TableCell>
        <TableCell>
          <Chip label={translation.images.length} size="small" color={translation.images.length > 0 ? 'success' : 'default'} variant="outlined" />
        </TableCell>
      </TableRow>
      {expanded && (
        <TableRow>
          <TableCell colSpan={5} sx={{ bgcolor: 'action.hover', px: 3, py: 2 }}>
            <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap' }}>
              {translation.tagline && (
                <Box sx={{ flexBasis: '100%' }}>
                  <Typography variant="caption" color="text.secondary">Tagline</Typography>
                  <Typography variant="body2" fontStyle="italic">{translation.tagline}</Typography>
                </Box>
              )}
              {translation.overview && (
                <Box sx={{ flexBasis: '100%' }}>
                  <Typography variant="caption" color="text.secondary">Overview</Typography>
                  <Typography variant="body2">{translation.overview}</Typography>
                </Box>
              )}
              {translation.images.length > 0 ? (
                <Box sx={{ minWidth: 200 }}>
                  <Typography variant="caption" color="text.secondary" display="block" mb={0.5}>Images</Typography>
                  {translation.images.map((img) => (
                    <ImageCard key={img.type} image={img} />
                  ))}
                </Box>
              ) : (
                <Typography variant="body2" color="text.disabled">No images</Typography>
              )}
            </Box>
          </TableCell>
        </TableRow>
      )}
    </>
  );
}

export function MovieDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [movie, setMovie] = useState<MovieDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [tmdbDialog, setTmdbDialog] = useState(false);
  const [tmdbInput, setTmdbInput] = useState('');
  const [saving, setSaving] = useState(false);
  const [syncing, setSyncing] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  const load = useCallback(() => {
    if (!user || !id) return;
    setLoading(true);
    getMovie(user, id)
      .then(setMovie)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, id]);

  useEffect(() => { load(); }, [load]);

  async function handleSetTmdbId() {
    if (!user || !id || !tmdbInput.trim()) return;
    setSaving(true);
    setActionError(null);
    try {
      const updated = await setMovieTmdbId(user, id, parseInt(tmdbInput.trim()));
      setMovie(updated);
      setTmdbDialog(false);
      setTmdbInput('');
    } catch (e: unknown) {
      setActionError(e instanceof Error ? e.message : 'Failed to set TMDB ID');
    } finally {
      setSaving(false);
    }
  }

  async function handleSync() {
    if (!user || !id) return;
    setSyncing(true);
    setActionError(null);
    try {
      await syncMovie(user, id);
      await load();
    } catch (e: unknown) {
      setActionError(e instanceof Error ? e.message : 'Sync failed');
    } finally {
      setSyncing(false);
    }
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !movie) {
    return <Alert severity="error">{error ?? 'Movie not found'}</Alert>;
  }

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/movies')}>
          Movies
        </Button>
        <Typography color="text.secondary">/</Typography>
        <Typography fontWeight="medium">{movie.originalTitle}</Typography>
      </Box>

      {actionError && <Alert severity="error" sx={{ mb: 2 }}>{actionError}</Alert>}

      <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap', mb: 3 }}>
        <Paper sx={{ flex: '1 1 400px', p: 3 }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            {movie.originalTitle}
          </Typography>
          <Divider sx={{ mb: 1 }} />
          <DetailRow label="ID" value={<Typography sx={{ fontSize: 13, fontFamily: 'monospace' }}>{movie.id}</Typography>} />
          <DetailRow label="Language" value={localeLabel(movie.originalLanguage)} />
          <DetailRow label="Release Date" value={movie.releaseDate} />
          <DetailRow label="Runtime" value={movie.runtime != null ? `${movie.runtime} min` : null} />
          <DetailRow label="TMDB ID" value={movie.tmdbId} />
          <DetailRow label="IMDB ID" value={movie.imdbId} />
        </Paper>

        <Box sx={{ flex: '0 0 260px', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
              Actions
            </Typography>
            <Divider sx={{ mb: 2 }} />
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">TMDB ID</Typography>
                {movie.tmdbId != null
                  ? <Chip label={movie.tmdbId} size="small" color="primary" variant="outlined" />
                  : <Typography variant="body2" color="text.disabled">Not set</Typography>}
              </Box>
              <Button size="small" onClick={() => setTmdbDialog(true)}>Set</Button>
            </Box>
            <Divider sx={{ mb: 2 }} />
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <Box>
                <Typography variant="body2" color="text.secondary">Sync from TMDB</Typography>
                {(syncing || movie.syncInProgress) && (
                  <Typography variant="caption" color="warning.main">In progress…</Typography>
                )}
              </Box>
              <Button
                size="small"
                variant="contained"
                startIcon={syncing || movie.syncInProgress ? <CircularProgress size={14} color="inherit" /> : <SyncIcon />}
                onClick={handleSync}
                disabled={syncing || movie.syncInProgress || movie.tmdbId == null}
              >
                Sync
              </Button>
            </Box>
          </Paper>
        </Box>
      </Box>

      <Paper sx={{ p: 3 }}>
        <Typography variant="subtitle1" fontWeight="bold" gutterBottom>
          Translations ({movie.translations.length})
        </Typography>
        <Divider sx={{ mb: 2 }} />
        {movie.translations.length === 0 ? (
          <Typography color="text.secondary">No translations yet. Run a sync to populate.</Typography>
        ) : (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: 32 }} />
                <TableCell>Locale</TableCell>
                <TableCell>Title</TableCell>
                <TableCell>Overview</TableCell>
                <TableCell>Images</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {movie.translations.map((t) => (
                <TranslationRow key={t.id} translation={t} />
              ))}
            </TableBody>
          </Table>
        )}
      </Paper>

      <Dialog open={tmdbDialog} onClose={() => setTmdbDialog(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Set TMDB ID</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          <TextField
            label="TMDB ID"
            type="number"
            value={tmdbInput}
            onChange={(e) => setTmdbInput(e.target.value)}
            fullWidth
            autoFocus
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setTmdbDialog(false)}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleSetTmdbId}
            disabled={saving || !tmdbInput.trim()}
          >
            {saving ? <CircularProgress size={20} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
