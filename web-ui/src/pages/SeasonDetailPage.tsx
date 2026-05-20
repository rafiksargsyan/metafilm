import { useEffect, useRef, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Select,
  MenuItem,
  CircularProgress,
  Alert,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Tooltip,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import { decode } from 'blurhash';
import { useAuth } from '../hooks/useAuth';
import { getSeason } from '../api/tvshows';
import type { SeasonDetail, SeasonImage, SeasonTranslation } from '../types/api.types';
import { localeLabel } from '../utils/localeLabel';

function BlurhashImage({ image, alt }: { image: SeasonImage; alt: string }) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [imgLoaded, setImgLoaded] = useState(false);
  const W = 32, H = 32;

  useEffect(() => {
    setImgLoaded(false);
    if (!image.blurhash || !canvasRef.current) return;
    try {
      const pixels = decode(image.blurhash, W, H);
      const ctx = canvasRef.current.getContext('2d');
      if (!ctx) return;
      const imageData = ctx.createImageData(W, H);
      imageData.data.set(pixels);
      ctx.putImageData(imageData, 0, 0);
    } catch { /* ignore */ }
  }, [image.blurhash]);

  return (
    <Box sx={{ position: 'relative', width: '100%', height: '100%' }}>
      {image.blurhash && (
        <canvas ref={canvasRef} width={W} height={H} style={{
          position: 'absolute', inset: 0, width: '100%', height: '100%',
          objectFit: 'cover', opacity: imgLoaded ? 0 : 1, transition: 'opacity 0.6s ease',
        }} />
      )}
      {image.url && (
        <img src={image.url} alt={alt} onLoad={() => setImgLoaded(true)} style={{
          position: 'absolute', inset: 0, width: '100%', height: '100%',
          objectFit: 'cover', opacity: imgLoaded ? 1 : 0, transition: 'opacity 0.6s ease',
        }} />
      )}
    </Box>
  );
}

function getPoster(translation: SeasonTranslation | null) {
  return translation?.images.find((i) => i.type === 'POSTER') ?? null;
}

export function SeasonDetailPage() {
  const { id: tvShowId, seasonId } = useParams<{ id: string; seasonId: string }>();
  const navigate = useNavigate();
  const { user, accountId } = useAuth();

  const [season, setSeason] = useState<SeasonDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedLocale, setSelectedLocale] = useState<string>('');

  useEffect(() => {
    if (!user || !accountId || !tvShowId || !seasonId) return;
    setLoading(true);
    getSeason(user, accountId, tvShowId, seasonId)
      .then((data) => {
        setSeason(data);
        setSelectedLocale(data.translations[0]?.locale ?? '');
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, tvShowId, seasonId]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !season) {
    return <Alert severity="error">{error ?? 'Season not found.'}</Alert>;
  }

  const translation = season.translations.find((t) => t.locale === selectedLocale) ?? null;
  const poster = getPoster(translation);
  const title = translation?.title || season.originalName || `Season ${season.seasonNumber}`;
  const overview = translation?.overview ?? null;
  const locales = season.translations.map((t) => t.locale);

  return (
    <Box sx={{ mx: -3, mt: -3, minHeight: '100vh' }}>
      <Box
        sx={{
          position: 'relative',
          width: '100%',
          minHeight: { xs: 'auto', md: 320 },
          overflow: 'hidden',
          display: 'flex',
          alignItems: 'flex-end',
          bgcolor: 'grey.900',
        }}
      >
        <Box sx={{
          position: 'absolute', inset: 0,
          background: 'linear-gradient(to top, rgba(0,0,0,0.95) 0%, rgba(0,0,0,0.3) 60%, transparent 100%)',
        }} />

        <Box sx={{ position: 'absolute', top: 16, left: 16, zIndex: 10 }}>
          <Tooltip title="Back to TV show">
            <IconButton
              onClick={() => navigate(`/tvshows/${tvShowId}`)}
              sx={{ color: 'white', bgcolor: 'rgba(0,0,0,0.4)', '&:hover': { bgcolor: 'rgba(0,0,0,0.6)' } }}
            >
              <ArrowBackIcon />
            </IconButton>
          </Tooltip>
        </Box>

        {locales.length > 0 && (
          <Box sx={{ position: 'absolute', top: 16, right: 16, zIndex: 10 }}>
            <Select
              value={selectedLocale}
              onChange={(e) => setSelectedLocale(e.target.value)}
              size="small"
              sx={{
                color: 'white', bgcolor: 'rgba(0,0,0,0.5)', backdropFilter: 'blur(8px)',
                '& .MuiOutlinedInput-notchedOutline': { borderColor: 'rgba(255,255,255,0.3)' },
                '&:hover .MuiOutlinedInput-notchedOutline': { borderColor: 'rgba(255,255,255,0.6)' },
                '& .MuiSvgIcon-root': { color: 'white' }, minWidth: 140,
              }}
            >
              {locales.map((l) => (
                <MenuItem key={l} value={l}>{localeLabel(l)}</MenuItem>
              ))}
            </Select>
          </Box>
        )}

        <Box sx={{
          position: 'relative', zIndex: 1, width: '100%',
          px: { xs: 3, md: 6 }, pb: { xs: 4, md: 6 }, pt: { xs: 10, md: 6 },
          display: 'flex', flexDirection: { xs: 'column', md: 'row' },
          alignItems: { xs: 'center', md: 'flex-end' }, gap: 4,
        }}>
          <Box sx={{
            flexShrink: 0,
            width: { xs: 120, md: 160 }, height: { xs: 180, md: 240 },
            borderRadius: 2, overflow: 'hidden',
            boxShadow: '0 8px 40px rgba(0,0,0,0.8)', bgcolor: 'grey.800',
          }}>
            {poster ? (
              <BlurhashImage image={poster} alt={`${title} poster`} />
            ) : (
              <Box sx={{ width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: 'grey.800' }}>
                <Typography variant="caption" color="grey.500">No poster</Typography>
              </Box>
            )}
          </Box>

          <Box sx={{ flex: 1, color: 'white', minWidth: 0 }}>
            <Typography variant="h4" fontWeight="bold" sx={{ textShadow: '0 2px 8px rgba(0,0,0,0.8)', mb: 1.5, fontSize: { xs: '1.5rem', md: '2rem' } }}>
              {title}
            </Typography>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5, mb: 2, alignItems: 'center' }}>
              {season.airDate && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <CalendarTodayIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{season.airDate}</Typography>
                </Box>
              )}
              <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.7)' }}>
                {season.episodes.length} episode{season.episodes.length !== 1 ? 's' : ''}
              </Typography>
            </Box>
            {overview && (
              <Typography variant="body1" sx={{
                color: 'rgba(255,255,255,0.85)', lineHeight: 1.7, maxWidth: 680,
                textShadow: '0 1px 4px rgba(0,0,0,0.6)',
                display: '-webkit-box', WebkitLineClamp: 4, WebkitBoxOrient: 'vertical', overflow: 'hidden',
              }}>
                {overview}
              </Typography>
            )}
          </Box>
        </Box>
      </Box>

      <Box sx={{ px: { xs: 3, md: 6 }, py: 4 }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          Episodes
        </Typography>
        <Paper>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>#</TableCell>
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
                      No episodes yet.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>
      </Box>
    </Box>
  );
}
