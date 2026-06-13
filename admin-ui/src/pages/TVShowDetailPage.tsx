import { useCallback, useEffect, useState } from 'react';
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
  FormControlLabel,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
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
import AddIcon from '@mui/icons-material/Add';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import {
  getTVShow,
  listSeasons,
  setTVShowTmdbId,
  setTVShowTvdbId,
  setTVShowUseTvdb,
  syncTVShow,
} from '../api/tvshows';
import { listTags, addTagToTVShow, removeTagFromTVShow } from '../api/tags';
import type { Season, Tag, TVShowDetail, TVShowTranslation } from '../types';

function DetailRow({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <Box sx={{ display: 'flex', py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
      <Typography sx={{ width: 160, flexShrink: 0, color: 'text.secondary', fontSize: 14 }}>
        {label}
      </Typography>
      <Typography sx={{ fontSize: 14, wordBreak: 'break-all' }}>{value ?? '—'}</Typography>
    </Box>
  );
}

function TranslationRow({ t }: { t: TVShowTranslation }) {
  const [open, setOpen] = useState(false);
  const poster = t.images.find((i) => i.type === 'POSTER');
  const backdrop = t.images.find((i) => i.type === 'BACKDROP');

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
        <TableCell>
          {backdrop?.url
            ? <img src={backdrop.url} alt="backdrop" style={{ height: 48, borderRadius: 4 }} />
            : <Typography color="text.secondary" variant="body2">—</Typography>}
        </TableCell>
        <TableCell>
          {t.trailer
            ? <Chip label={t.trailer.site} size="small" color="info" variant="outlined" />
            : <Typography color="text.secondary" variant="body2">—</Typography>}
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell colSpan={6} sx={{ p: 0, border: 0 }}>
          <Collapse in={open} unmountOnExit>
            <Box sx={{ p: 2, bgcolor: 'action.hover' }}>
              {t.tagline && (
                <Typography variant="body2" fontStyle="italic" sx={{ mb: 1 }}>{t.tagline}</Typography>
              )}
              <Typography variant="body2" color="text.secondary" sx={{ mb: t.trailer ? 2 : 0 }}>
                {t.overview ?? 'No overview.'}
              </Typography>
              {t.trailer && (
                <Box>
                  <Typography variant="caption" color="text.secondary" display="block" mb={0.5}>Trailer</Typography>
                  <Box
                    component="iframe"
                    src={
                      t.trailer.site === 'YouTube'
                        ? `https://www.youtube.com/embed/${t.trailer.key}`
                        : `https://player.vimeo.com/video/${t.trailer.key}`
                    }
                    width={480}
                    height={270}
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowFullScreen
                    sx={{ border: 'none', borderRadius: 1 }}
                  />
                </Box>
              )}
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}

type IdDialogType = 'tmdb' | 'tvdb' | null;

export function TVShowDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [tvShow, setTVShow] = useState<TVShowDetail | null>(null);
  const [seasons, setSeasons] = useState<Season[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [idDialog, setIdDialog] = useState<IdDialogType>(null);
  const [idInput, setIdInput] = useState('');
  const [saving, setSaving] = useState(false);
  const [syncing, setSyncing] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  const [allTags, setAllTags] = useState<Tag[]>([]);
  const [addTagOpen, setAddTagOpen] = useState(false);
  const [tagError, setTagError] = useState<string | null>(null);

  const load = useCallback(() => {
    if (!user || !id) return;
    setLoading(true);
    Promise.all([getTVShow(user, id), listSeasons(user, id), listTags(user)])
      .then(([show, seas, tags]) => {
        setTVShow(show);
        setSeasons(seas);
        setAllTags(tags.sort((a, b) => a.key.localeCompare(b.key)));
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, id]);

  useEffect(() => { load(); }, [load]);

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
      setTVShow((prev) => prev ? { ...prev, ...updated } : null);
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
      setTVShow((prev) => prev ? { ...prev, ...updated } : null);
    } catch (e: unknown) {
      setActionError(e instanceof Error ? e.message : 'Failed to update');
    }
  }

  async function handleAddTag(tagId: string) {
    if (!user || !id) return;
    setTagError(null);
    try {
      await addTagToTVShow(user, id, tagId);
      setAddTagOpen(false);
      load();
    } catch (e: unknown) {
      setTagError(e instanceof Error ? e.message : 'Failed to add tag');
    }
  }

  async function handleRemoveTag(tagId: string) {
    if (!user || !id) return;
    setTagError(null);
    try {
      await removeTagFromTVShow(user, id, tagId);
      load();
    } catch (e: unknown) {
      setTagError(e instanceof Error ? e.message : 'Failed to remove tag');
    }
  }

  async function handleSync() {
    if (!user || !id) return;
    setSyncing(true);
    setActionError(null);
    try {
      await syncTVShow(user, id);
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
          <DetailRow label="TMDB Score" value={tvShow.voteAverage != null ? tvShow.voteAverage.toFixed(1) : null} />
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
              <Button size="small" onClick={() => openIdDialog('tmdb')}>Set</Button>
            </Box>

            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">TVDB ID</Typography>
                {tvShow.tvdbId != null
                  ? <Chip label={tvShow.tvdbId} size="small" color="secondary" variant="outlined" />
                  : <Typography variant="body2">Not set</Typography>}
              </Box>
              <Button size="small" onClick={() => openIdDialog('tvdb')}>Set</Button>
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
            <Typography variant="caption" color="text.secondary" display="block" sx={{ mb: 2 }}>
              Sync source: {tvShow.useTvdb ? 'TVDB' : 'TMDB'}
            </Typography>

            <Divider sx={{ mb: 1.5 }} />
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <Box>
                <Typography variant="body2" color="text.secondary">Sync from {tvShow.useTvdb ? 'TVDB' : 'TMDB'}</Typography>
              </Box>
              <Button
                size="small"
                startIcon={syncing ? <CircularProgress size={14} color="inherit" /> : <SyncIcon />}
                onClick={handleSync}
                disabled={syncing || (tvShow.tmdbId == null && tvShow.tvdbId == null)}
              >
                Sync
              </Button>
            </Box>
          </Paper>
        </Box>
      </Box>

      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
          <Typography variant="subtitle1" fontWeight="bold">
            Tags ({tvShow.tags.length})
          </Typography>
          <Button size="small" startIcon={<AddIcon />} onClick={() => { setTagError(null); setAddTagOpen(true); }}>
            Add Tag
          </Button>
        </Box>
        <Divider sx={{ mb: 2 }} />
        {tagError && <Alert severity="error" sx={{ mb: 1 }}>{tagError}</Alert>}
        {tvShow.tags.length === 0 ? (
          <Typography color="text.secondary" variant="body2">No tags assigned.</Typography>
        ) : (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {tvShow.tags.map((tag) => (
              <Chip
                key={tag.id}
                label={tag.key}
                size="small"
                onDelete={() => handleRemoveTag(tag.id)}
                variant="outlined"
              />
            ))}
          </Box>
        )}
      </Paper>

      <Typography variant="h6" fontWeight="bold" gutterBottom>
        Translations
      </Typography>
      <Paper sx={{ mb: 3 }}>
        {tvShow.translations.length === 0 ? (
          <Box sx={{ p: 3 }}>
            <Typography color="text.secondary">No translations yet. Run a sync to populate.</Typography>
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
                  <TableCell>Backdrop</TableCell>
                  <TableCell>Trailer</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {tvShow.translations.map((t) => <TranslationRow key={t.id} t={t} />)}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

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

      <Dialog open={addTagOpen} onClose={() => setAddTagOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Add Tag</DialogTitle>
        <DialogContent sx={{ p: 0 }}>
          {tagError && <Alert severity="error" sx={{ mx: 2, mt: 1 }}>{tagError}</Alert>}
          <List dense>
            {allTags
              .filter((t) => !tvShow.tags.some((st) => st.id === t.id))
              .map((tag) => (
                <ListItem key={tag.id} disablePadding>
                  <ListItemButton onClick={() => handleAddTag(tag.id)}>
                    <ListItemText
                      primary={<Typography sx={{ fontFamily: 'monospace', fontSize: 13 }}>{tag.key}</Typography>}
                      secondary={tag.name}
                    />
                  </ListItemButton>
                </ListItem>
              ))}
            {allTags.filter((t) => !tvShow.tags.some((st) => st.id === t.id)).length === 0 && (
              <ListItem>
                <ListItemText primary={<Typography color="text.secondary">All tags already assigned</Typography>} />
              </ListItem>
            )}
          </List>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAddTagOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>

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
