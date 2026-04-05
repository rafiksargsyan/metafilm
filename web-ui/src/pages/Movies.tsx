import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  TablePagination,
  Paper,
  TableContainer,
  CircularProgress,
  Alert,
} from '@mui/material';
import { useAuth } from '../hooks/useAuth';
import { listMovies } from '../api/movies';
import type { Movie } from '../types/api.types';

export function Movies() {
  const { user, accountId } = useAuth();
  const navigate = useNavigate();
  const [movies, setMovies] = useState<Movie[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const rowsPerPage = 20;

  useEffect(() => {
    if (!user || !accountId) return;
    setLoading(true);
    setError(null);
    listMovies(user, accountId, page, rowsPerPage)
      .then((data) => {
        setMovies(data.content);
        setTotal(data.page.totalElements);
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, page]);

  return (
    <>
      <Typography variant="h5" fontWeight="bold" gutterBottom>
        Movies
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Title</TableCell>
                <TableCell>Language</TableCell>
                <TableCell>Release Date</TableCell>
                <TableCell>Runtime</TableCell>
                <TableCell>TMDB</TableCell>
                <TableCell>IMDB</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={7} align="center" sx={{ py: 4 }}>
                    <CircularProgress size={24} />
                  </TableCell>
                </TableRow>
              ) : movies.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} align="center" sx={{ color: 'text.secondary' }}>
                    No movies found.
                  </TableCell>
                </TableRow>
              ) : (
                movies.map((m) => (
                  <TableRow
                    key={m.id}
                    hover
                    sx={{ cursor: 'pointer' }}
                    onClick={() => navigate(`/movies/${m.id}`)}
                  >
                    <TableCell sx={{ fontFamily: 'monospace', fontSize: 12 }}>{m.id}</TableCell>
                    <TableCell>{m.originalTitle}</TableCell>
                    <TableCell>{m.originalLanguage}</TableCell>
                    <TableCell>{m.releaseDate ?? '—'}</TableCell>
                    <TableCell>{m.runtime != null ? `${m.runtime} min` : '—'}</TableCell>
                    <TableCell>{m.tmdbId ?? '—'}</TableCell>
                    <TableCell>{m.imdbId ?? '—'}</TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={total}
          page={page}
          rowsPerPage={rowsPerPage}
          rowsPerPageOptions={[rowsPerPage]}
          onPageChange={(_, newPage) => setPage(newPage)}
        />
      </Paper>
    </>
  );
}
