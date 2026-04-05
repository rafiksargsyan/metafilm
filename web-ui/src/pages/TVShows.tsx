import { useEffect, useState } from 'react';
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
import { listTVShows } from '../api/tvshows';
import type { TVShow } from '../types/api.types';

export function TVShows() {
  const { user, accountId } = useAuth();
  const [shows, setShows] = useState<TVShow[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const rowsPerPage = 20;

  useEffect(() => {
    if (!user || !accountId) return;
    setLoading(true);
    setError(null);
    listTVShows(user, accountId, page, rowsPerPage)
      .then((data) => {
        setShows(data.content);
        setTotal(data.page.totalElements);
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, page]);

  return (
    <>
      <Typography variant="h5" fontWeight="bold" gutterBottom>
        TV Shows
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Title</TableCell>
                <TableCell>Language</TableCell>
                <TableCell>First Air Date</TableCell>
                <TableCell>Last Air Date</TableCell>
                <TableCell>IMDB</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                    <CircularProgress size={24} />
                  </TableCell>
                </TableRow>
              ) : shows.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ color: 'text.secondary' }}>
                    No TV shows found.
                  </TableCell>
                </TableRow>
              ) : (
                shows.map((s) => (
                  <TableRow key={s.id}>
                    <TableCell>{s.originalTitle}</TableCell>
                    <TableCell>{s.originalLanguage}</TableCell>
                    <TableCell>{s.firstAirDate ?? '—'}</TableCell>
                    <TableCell>{s.lastAirDate ?? '—'}</TableCell>
                    <TableCell>{s.imdbId ?? '—'}</TableCell>
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
