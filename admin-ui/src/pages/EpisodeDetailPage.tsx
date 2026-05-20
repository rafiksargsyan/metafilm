import { useEffect, useState } from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Collapse,
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { getEpisode } from '../api/tvshows';
import type { EpisodeDetail, EpisodeTranslation } from '../types';

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

function TranslationRow({ t }: { t: EpisodeTranslation }) {
  const [open, setOpen] = useState(false);
  const still = t.images.find((i) => i.type === 'STILL');

  return (
    <>
      <TableRow hover sx={{ cursor: 'pointer' }} onClick={() => setOpen((o) => !o)}>
        <TableCell sx={{ width: 32 }}>
          {open ? <ExpandLessIcon fontSize="small" /> : <ExpandMoreIcon fontSize="small" />}
        </TableCell>
        <TableCell><Chip label={t.locale} size="small" /></TableCell>
        <TableCell>{t.title ?? <Typography color="text.secondary" variant="body2">—</Typography>}</TableCell>
        <TableCell>
          {still?.url
            ? <img src={still.url} alt="still" style={{ height: 48, borderRadius: 4 }} />
            : <Typography color="text.secondary" variant="body2">—</Typography>}
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell colSpan={4} sx={{ p: 0, border: 0 }}>
          <Collapse in={open} unmountOnExit>
            <Box sx={{ p: 2, bgcolor: 'action.hover' }}>
              <Typography variant="body2" color="text.secondary">
                {t.overview ?? 'No overview.'}
              </Typography>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}

export function EpisodeDetailPage() {
  const { id: tvShowId, seasonId, episodeId } = useParams<{
    id: string;
    seasonId: string;
    episodeId: string;
  }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [episode, setEpisode] = useState<EpisodeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!user || !tvShowId || !episodeId) return;
    setLoading(true);
    getEpisode(user, tvShowId, episodeId)
      .then(setEpisode)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, tvShowId, episodeId]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !episode) {
    return <Alert severity="error">{error ?? 'Episode not found'}</Alert>;
  }

  const episodeLabel = episode.episodeNumber != null
    ? `Episode ${episode.episodeNumber}`
    : 'Episode';

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(`/tvshows/${tvShowId}/seasons/${seasonId}`)}>
          Season
        </Button>
        <Typography color="text.secondary">/</Typography>
        <Typography fontWeight="medium">{episodeLabel}</Typography>
      </Box>

      <Paper sx={{ p: 3, mb: 3, maxWidth: 500 }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          {episodeLabel}
        </Typography>
        <Divider sx={{ mb: 1 }} />
        <DetailRow label="Season #" value={episode.seasonNumber} />
        <DetailRow label="Episode #" value={episode.episodeNumber} />
        <DetailRow label="Absolute #" value={episode.absoluteNumber} />
        <DetailRow label="Air Date" value={episode.airDate} />
        <DetailRow
          label="Runtime"
          value={episode.runtime != null ? `${episode.runtime} min` : null}
        />
      </Paper>

      <Typography variant="h6" fontWeight="bold" gutterBottom>
        Translations
      </Typography>
      <Paper>
        {episode.translations.length === 0 ? (
          <Box sx={{ p: 3 }}>
            <Typography color="text.secondary">No translations yet.</Typography>
          </Box>
        ) : (
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ width: 32 }} />
                  <TableCell>Locale</TableCell>
                  <TableCell>Title</TableCell>
                  <TableCell>Still</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {episode.translations.map((t) => <TranslationRow key={t.id} t={t} />)}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>
    </>
  );
}
