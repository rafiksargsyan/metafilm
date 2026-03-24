import { useEffect, useState } from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  Paper,
  TextField,
  Typography,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SyncIcon from '@mui/icons-material/Sync';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { getMovie, setMovieTmdbId } from '../api/movies';
import type { Movie } from '../types';

function DetailRow({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <Box sx={{ display: 'flex', py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
      <Typography sx={{ width: 160, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>
        {label}
      </Typography>
      <Typography sx={{ fontSize: 14 }}>{value ?? '—'}</Typography>
    </Box>
  );
}

export function MovieDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [movie, setMovie] = useState<Movie | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [tmdbDialog, setTmdbDialog] = useState(false);
  const [tmdbInput, setTmdbInput] = useState('');
  const [saving, setSaving] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  useEffect(() => {
    if (!user || !id) return;
    setLoading(true);
    getMovie(user, id)
      .then(setMovie)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, id]);

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

      <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap' }}>
        <Paper sx={{ flex: '1 1 400px', p: 3 }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            {movie.originalTitle}
          </Typography>
          <Divider sx={{ mb: 1 }} />
          <DetailRow label="ID" value={<Typography sx={{ fontSize: 13, fontFamily: 'monospace' }}>{movie.id}</Typography>} />
          <DetailRow label="Language" value={movie.originalLanguage} />
          <DetailRow label="Release Date" value={movie.releaseDate} />
          <DetailRow label="Runtime" value={movie.runtime != null ? `${movie.runtime} min` : null} />
          <DetailRow label="TMDB ID" value={movie.tmdbId} />
          <DetailRow label="IMDB ID" value={movie.imdbId} />
        </Paper>

        <Box sx={{ flex: '0 0 260px', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
              External IDs
            </Typography>
            <Divider sx={{ mb: 2 }} />
            {actionError && <Alert severity="error" sx={{ mb: 2 }}>{actionError}</Alert>}
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">TMDB ID</Typography>
                {movie.tmdbId != null
                  ? <Chip label={movie.tmdbId} size="small" color="primary" variant="outlined" />
                  : <Typography variant="body2">Not set</Typography>}
              </Box>
              <Button size="small" startIcon={<SyncIcon />} onClick={() => setTmdbDialog(true)}>
                Set
              </Button>
            </Box>
          </Paper>
        </Box>
      </Box>

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
