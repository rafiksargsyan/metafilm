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
  Tooltip,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import { decode } from 'blurhash';
import { useAuth } from '../hooks/useAuth';
import { getEpisode } from '../api/tvshows';
import type { EpisodeDetail, EpisodeImage, EpisodeTranslation } from '../types/api.types';
import { localeLabel } from '../utils/localeLabel';

function BlurhashImage({ image, alt }: { image: EpisodeImage; alt: string }) {
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

function getStill(translation: EpisodeTranslation | null) {
  return translation?.images.find((i) => i.type === 'STILL') ?? null;
}

export function EpisodeDetailPage() {
  const { id: tvShowId, seasonId, episodeId } = useParams<{ id: string; seasonId: string; episodeId: string }>();
  const navigate = useNavigate();
  const { user, accountId } = useAuth();

  const [episode, setEpisode] = useState<EpisodeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedLocale, setSelectedLocale] = useState<string>('');

  useEffect(() => {
    if (!user || !accountId || !tvShowId || !episodeId) return;
    setLoading(true);
    getEpisode(user, accountId, tvShowId, episodeId)
      .then((data) => {
        setEpisode(data);
        setSelectedLocale(data.translations[0]?.locale ?? '');
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, tvShowId, episodeId]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !episode) {
    return <Alert severity="error">{error ?? 'Episode not found.'}</Alert>;
  }

  const translation = episode.translations.find((t) => t.locale === selectedLocale) ?? null;
  const still = getStill(translation);
  const title = translation?.title || (episode.episodeNumber != null ? `Episode ${episode.episodeNumber}` : 'Episode');
  const overview = translation?.overview ?? null;
  const locales = episode.translations.map((t) => t.locale);

  return (
    <Box sx={{ mx: -3, mt: -3, minHeight: '100vh' }}>
      <Box
        sx={{
          position: 'relative',
          width: '100%',
          minHeight: { xs: 'auto', md: 400 },
          overflow: 'hidden',
          display: 'flex',
          alignItems: 'flex-end',
        }}
      >
        {still ? (
          <Box sx={{ position: 'absolute', inset: 0 }}>
            <BlurhashImage image={still} alt="episode still" />
          </Box>
        ) : (
          <Box sx={{ position: 'absolute', inset: 0, bgcolor: 'grey.900' }} />
        )}

        <Box sx={{
          position: 'absolute', inset: 0,
          background: 'linear-gradient(to right, rgba(0,0,0,0.85) 0%, rgba(0,0,0,0.4) 60%, rgba(0,0,0,0.1) 100%)',
        }} />
        <Box sx={{
          position: 'absolute', inset: 0,
          background: 'linear-gradient(to top, rgba(0,0,0,0.95) 0%, rgba(0,0,0,0.3) 40%, transparent 100%)',
        }} />

        <Box sx={{ position: 'absolute', top: 16, left: 16, zIndex: 10 }}>
          <Tooltip title="Back to season">
            <IconButton
              onClick={() => navigate(`/tvshows/${tvShowId}/seasons/${seasonId}`)}
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
        }}>
          <Typography variant="h4" fontWeight="bold" color="white" sx={{ textShadow: '0 2px 8px rgba(0,0,0,0.8)', mb: 1.5, fontSize: { xs: '1.5rem', md: '2rem' } }}>
            {title}
          </Typography>

          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5, mb: 2, alignItems: 'center' }}>
            {episode.airDate && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                <CalendarTodayIcon sx={{ fontSize: 15 }} />
                <Typography variant="body2">{episode.airDate}</Typography>
              </Box>
            )}
            {episode.runtime != null && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                <AccessTimeIcon sx={{ fontSize: 15 }} />
                <Typography variant="body2">{episode.runtime} min</Typography>
              </Box>
            )}
            {episode.episodeNumber != null && (
              <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.7)' }}>
                S{episode.seasonNumber ?? '?'} E{episode.episodeNumber}
              </Typography>
            )}
          </Box>

          {overview && (
            <Typography variant="body1" sx={{
              color: 'rgba(255,255,255,0.85)', lineHeight: 1.7, maxWidth: 680,
              textShadow: '0 1px 4px rgba(0,0,0,0.6)',
              display: '-webkit-box', WebkitLineClamp: 5, WebkitBoxOrient: 'vertical', overflow: 'hidden',
            }}>
              {overview}
            </Typography>
          )}
        </Box>
      </Box>
    </Box>
  );
}
