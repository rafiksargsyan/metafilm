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
import { getSeason } from '../api/tvshows';
import type { SeasonDetail, SeasonTranslation } from '../types';

function TranslationRow({ t }: { t: SeasonTranslation }) {
  const [open, setOpen] = useState(false);
  const poster = t.images.find((i) => i.type === 'POSTER');

  return (
    <>
      <TableRow hover sx={{ cursor: 'pointer' }} onClick={() => setOpen((o) => !o)}>
        <TableCell sx={{ width: 32 }}>
          {open ? <ExpandLessIcon fontSize="small" /> : <ExpandMoreIcon fontSize="small" />}
        </TableCell>
        <TableCell><Chip label={t.locale} size="small" /></TableCell>
        <TableCell>{t.title ?? <Typography color="text.secondary" variant="body2">—</Typography>}</TableCell>
        <TableCell>
          {poster?.url
            ? <img src={poster.url} alt="poster" style={{ height: 48, borderRadius: 4 }} />
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

export function SeasonDetailPage() {
  const { id: tvShowId, seasonId } = useParams<{ id: string; seasonId: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [season, setSeason] = useState<SeasonDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!user || !tvShowId || !seasonId) return;
    setLoading(true);
    getSeason(user, tvShowId, seasonId)
      .then(setSeason)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, tvShowId, seasonId]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !season) {
    return <Alert severity="error">{error ?? 'Season not found'}</Alert>;
  }

  const title = season.originalName ?? `Season ${season.seasonNumber}`;

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(`/tvshows/${tvShowId}`)}>
          TV Show
        </Button>
        <Typography color="text.secondary">/</Typography>
        <Typography fontWeight="medium">{title}</Typography>
      </Box>

      <Paper sx={{ p: 3, mb: 3, maxWidth: 500 }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          {title}
        </Typography>
        <Divider sx={{ mb: 1 }} />
        <Box sx={{ display: 'flex', py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
          <Typography sx={{ width: 140, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>Season #</Typography>
          <Typography sx={{ fontSize: 14 }}>{season.seasonNumber}</Typography>
        </Box>
        <Box sx={{ display: 'flex', py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
          <Typography sx={{ width: 140, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>Air Date</Typography>
          <Typography sx={{ fontSize: 14 }}>{season.airDate ?? '—'}</Typography>
        </Box>
        <Box sx={{ display: 'flex', py: 1.5 }}>
          <Typography sx={{ width: 140, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>Episodes</Typography>
          <Typography sx={{ fontSize: 14 }}>{season.episodes.length}</Typography>
        </Box>
      </Paper>

      <Typography variant="h6" fontWeight="bold" gutterBottom>
        Translations
      </Typography>
      <Paper sx={{ mb: 3 }}>
        {season.translations.length === 0 ? (
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
                  <TableCell>Poster</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {season.translations.map((t) => <TranslationRow key={t.id} t={t} />)}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      <Typography variant="h6" fontWeight="bold" gutterBottom>
        Episodes
      </Typography>
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Episode #</TableCell>
                <TableCell>Absolute #</TableCell>
                <TableCell>Air Date</TableCell>
                <TableCell>Runtime</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {season.episodes.map((e) => (
                <TableRow
                  key={e.id}
                  hover
                  sx={{ cursor: 'pointer' }}
                  onClick={() => navigate(`/tvshows/${tvShowId}/seasons/${seasonId}/episodes/${e.id}`)}
                >
                  <TableCell>{e.episodeNumber ?? '—'}</TableCell>
                  <TableCell>{e.absoluteNumber ?? '—'}</TableCell>
                  <TableCell>{e.airDate ?? '—'}</TableCell>
                  <TableCell>{e.runtime != null ? `${e.runtime} min` : '—'}</TableCell>
                </TableRow>
              ))}
              {season.episodes.length === 0 && (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ color: 'text.secondary' }}>
                    No episodes yet
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </>
  );
}
