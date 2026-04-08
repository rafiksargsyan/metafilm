import { useEffect, useState } from 'react';
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { createMovie, listMovies } from '../api/movies';
import type { Movie } from '../types';
import { LOCALES } from '../types';

export function MoviesPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [movies, setMovies] = useState<Movie[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(20);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [dialogOpen, setDialogOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [form, setForm] = useState({ originalTitle: '', originalLocale: 'EN_US', releaseDate: '' });

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    listMovies(user, page, rowsPerPage)
      .then((p) => {
        setMovies(p.content);
        setTotalElements(p.page.totalElements);
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, page, rowsPerPage]);

  async function handleCreate() {
    if (!user || !form.originalTitle.trim()) return;
    setSaving(true);
    setFormError(null);
    try {
      await createMovie(user, {
        originalTitle: form.originalTitle.trim(),
        originalLocale: form.originalLocale,
        releaseDate: form.releaseDate || null,
      });
      setDialogOpen(false);
      setForm({ originalTitle: '', originalLocale: 'EN_US', releaseDate: '' });
      setPage(0);
      // reload
      const p = await listMovies(user, 0, rowsPerPage);
      setMovies(p.content);
      setTotalElements(p.page.totalElements);
    } catch (e: unknown) {
      setFormError(e instanceof Error ? e.message : 'Failed to create movie');
    } finally {
      setSaving(false);
    }
  }

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5" fontWeight="bold" sx={{ flexGrow: 1 }}>
          Movies
        </Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => setDialogOpen(true)}>
          Add Movie
        </Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
          <CircularProgress />
        </Box>
      ) : (
        <Paper>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Title</TableCell>
                  <TableCell>Language</TableCell>
                  <TableCell>Release Date</TableCell>
                  <TableCell>TMDB ID</TableCell>
                  <TableCell>IMDB ID</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {movies.map((m) => (
                  <TableRow
                    key={m.id}
                    hover
                    sx={{ cursor: 'pointer' }}
                    onClick={() => navigate(`/movies/${m.id}`)}
                  >
                    <TableCell>{m.originalTitle}</TableCell>
                    <TableCell>{m.originalLanguage}</TableCell>
                    <TableCell>{m.releaseDate ?? '—'}</TableCell>
                    <TableCell>{m.tmdbId ?? '—'}</TableCell>
                    <TableCell>{m.imdbId ?? '—'}</TableCell>
                  </TableRow>
                ))}
                {movies.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={5} align="center" sx={{ color: 'text.secondary' }}>
                      No movies yet
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={totalElements}
            page={page}
            rowsPerPage={rowsPerPage}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={(_, p) => setPage(p)}
            onRowsPerPageChange={(e) => { setRowsPerPage(parseInt(e.target.value)); setPage(0); }}
          />
        </Paper>
      )}

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Add Movie</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
          {formError && <Alert severity="error">{formError}</Alert>}
          <TextField
            label="Original Title"
            value={form.originalTitle}
            onChange={(e) => setForm({ ...form, originalTitle: e.target.value })}
            required
            autoFocus
          />
          <TextField
            select
            label="Original Language"
            value={form.originalLocale}
            onChange={(e) => setForm({ ...form, originalLocale: e.target.value })}
          >
            {LOCALES.map((l) => (
              <MenuItem key={l.value} value={l.value}>{l.label}</MenuItem>
            ))}
          </TextField>
          <TextField
            label="Release Date"
            type="date"
            value={form.releaseDate}
            onChange={(e) => setForm({ ...form, releaseDate: e.target.value })}
            slotProps={{ inputLabel: { shrink: true } }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleCreate}
            disabled={saving || !form.originalTitle.trim()}
          >
            {saving ? <CircularProgress size={20} /> : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
