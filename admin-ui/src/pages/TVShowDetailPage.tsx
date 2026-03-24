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
  FormControlLabel,
  Paper,
  Switch,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SyncIcon from '@mui/icons-material/Sync';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import {
  getTVShow,
  listSeasons,
  setTVShowTmdbId,
  setTVShowTvdbId,
  setTVShowUseTvdb,
} from '../api/tvshows';
import type { Season, TVShow } from '../types';

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

type IdDialogType = 'tmdb' | 'tvdb' | null;

export function TVShowDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [tvShow, setTVShow] = useState<TVShow | null>(null);
  const [seasons, setSeasons] = useState<Season[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [idDialog, setIdDialog] = useState<IdDialogType>(null);
  const [idInput, setIdInput] = useState('');
  const [saving, setSaving] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  useEffect(() => {
    if (!user || !id) return;
    setLoading(true);
    Promise.all([getTVShow(user, id), listSeasons(user, id)])
      .then(([show, seas]) => {
        setTVShow(show);
        setSeasons(seas);
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, id]);

  function openIdDialog(type: IdDialogType) {
    setIdInput('');
    setActionError(null);
    setIdDialog(type);
  }

  async function handleSetId() {
    if (!user || !id || !idInput.trim() || !idDialog) return;
    setSaving(true);
    setActionError(null);
    try {
      const val = parseInt(idInput.trim());
      const updated =
        idDialog === 'tmdb'
          ? await setTVShowTmdbId(user, id, val)
          : await setTVShowTvdbId(user, id, val);
      setTVShow(updated);
      setIdDialog(null);
    } catch (e: unknown) {
      setActionError(e instanceof Error ? e.message : 'Failed to save');
    } finally {
      setSaving(false);
    }
  }

  async function handleToggleUseTvdb(checked: boolean) {
    if (!user || !id) return;
    setActionError(null);
    try {
      const updated = await setTVShowUseTvdb(user, id, checked);
      setTVShow(updated);
    } catch (e: unknown) {
      setActionError(e instanceof Error ? e.message : 'Failed to update');
    }
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !tvShow) {
    return <Alert severity="error">{error ?? 'TV Show not found'}</Alert>;
  }

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/tvshows')}>
          TV Shows
        </Button>
        <Typography color="text.secondary">/</Typography>
        <Typography fontWeight="medium">{tvShow.originalTitle}</Typography>
      </Box>

      <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap', mb: 3 }}>
        <Paper sx={{ flex: '1 1 400px', p: 3 }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            {tvShow.originalTitle}
          </Typography>
          <Divider sx={{ mb: 1 }} />
          <DetailRow label="ID" value={<Typography sx={{ fontSize: 13, fontFamily: 'monospace' }}>{tvShow.id}</Typography>} />
          <DetailRow label="Language" value={tvShow.originalLanguage} />
          <DetailRow label="First Air Date" value={tvShow.firstAirDate} />
          <DetailRow label="Last Air Date" value={tvShow.lastAirDate} />
          <DetailRow label="TMDB ID" value={tvShow.tmdbId} />
          <DetailRow label="TVDB ID" value={tvShow.tvdbId} />
          <DetailRow label="IMDB ID" value={tvShow.imdbId} />
        </Paper>

        <Box sx={{ flex: '0 0 260px', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
              External IDs & Sync
            </Typography>
            <Divider sx={{ mb: 2 }} />
            {actionError && <Alert severity="error" sx={{ mb: 2 }}>{actionError}</Alert>}

            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">TMDB ID</Typography>
                {tvShow.tmdbId != null
                  ? <Chip label={tvShow.tmdbId} size="small" color="primary" variant="outlined" />
                  : <Typography variant="body2">Not set</Typography>}
              </Box>
              <Button size="small" startIcon={<SyncIcon />} onClick={() => openIdDialog('tmdb')}>
                Set
              </Button>
            </Box>

            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">TVDB ID</Typography>
                {tvShow.tvdbId != null
                  ? <Chip label={tvShow.tvdbId} size="small" color="secondary" variant="outlined" />
                  : <Typography variant="body2">Not set</Typography>}
              </Box>
              <Button size="small" startIcon={<SyncIcon />} onClick={() => openIdDialog('tvdb')}>
                Set
              </Button>
            </Box>

            <Divider sx={{ my: 1.5 }} />
            <FormControlLabel
              control={
                <Switch
                  checked={tvShow.useTvdb}
                  onChange={(e) => handleToggleUseTvdb(e.target.checked)}
                />
              }
              label="Use TVDB"
            />
            <Typography variant="caption" color="text.secondary" display="block">
              Sync source: {tvShow.useTvdb ? 'TVDB' : 'TMDB'}
            </Typography>
          </Paper>
        </Box>
      </Box>

      <Typography variant="h6" fontWeight="bold" gutterBottom>
        Seasons
      </Typography>
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>#</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Air Date</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {seasons.map((s) => (
                <TableRow
                  key={s.id}
                  hover
                  sx={{ cursor: 'pointer' }}
                  onClick={() => navigate(`/tvshows/${tvShow.id}/seasons/${s.id}`)}
                >
                  <TableCell>{s.seasonNumber}</TableCell>
                  <TableCell>{s.originalName ?? `Season ${s.seasonNumber}`}</TableCell>
                  <TableCell>{s.airDate ?? '—'}</TableCell>
                </TableRow>
              ))}
              {seasons.length === 0 && (
                <TableRow>
                  <TableCell colSpan={3} align="center" sx={{ color: 'text.secondary' }}>
                    No seasons yet
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      <Dialog open={idDialog !== null} onClose={() => setIdDialog(null)} maxWidth="xs" fullWidth>
        <DialogTitle>Set {idDialog === 'tmdb' ? 'TMDB' : 'TVDB'} ID</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          <TextField
            label={`${idDialog === 'tmdb' ? 'TMDB' : 'TVDB'} ID`}
            type="number"
            value={idInput}
            onChange={(e) => setIdInput(e.target.value)}
            fullWidth
            autoFocus
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIdDialog(null)}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleSetId}
            disabled={saving || !idInput.trim()}
          >
            {saving ? <CircularProgress size={20} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
