import { useEffect, useRef, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Select,
  MenuItem,
  CircularProgress,
  Alert,
  Chip,
  IconButton,
  Tooltip,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import LanguageIcon from '@mui/icons-material/Language';
import { decode } from 'blurhash';
import { useAuth } from '../hooks/useAuth';
import { getMovie } from '../api/movies';
import type { MovieDetail, MovieImage, MovieTranslation } from '../types/api.types';

// ── Blurhash → fade-in image ─────────────────────────────────────────────────

function BlurhashImage({
  image,
  alt,
  style,
}: {
  image: MovieImage;
  alt: string;
  style?: React.CSSProperties;
}) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [imgLoaded, setImgLoaded] = useState(false);

  const W = 32;
  const H = 32;

  useEffect(() => {
    setImgLoaded(false);
    if (!image.blurhash || !canvasRef.current) return;
    try {
      const pixels = decode(image.blurhash, W, H);
      const canvas = canvasRef.current;
      const ctx = canvas.getContext('2d');
      if (!ctx) return;
      const imageData = ctx.createImageData(W, H);
      imageData.data.set(pixels);
      ctx.putImageData(imageData, 0, 0);
    } catch {
      // ignore bad hashes
    }
  }, [image.blurhash]);

  return (
    <Box sx={{ position: 'relative', width: '100%', height: '100%', ...style }}>
      {image.blurhash && (
        <canvas
          ref={canvasRef}
          width={W}
          height={H}
          style={{
            position: 'absolute',
            inset: 0,
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            opacity: imgLoaded ? 0 : 1,
            transition: 'opacity 0.6s ease',
          }}
        />
      )}
      <img
        src={image.url}
        alt={alt}
        onLoad={() => setImgLoaded(true)}
        style={{
          position: 'absolute',
          inset: 0,
          width: '100%',
          height: '100%',
          objectFit: 'cover',
          opacity: imgLoaded ? 1 : 0,
          transition: 'opacity 0.6s ease',
        }}
      />
    </Box>
  );
}

// ── Helpers ───────────────────────────────────────────────────────────────────

function getImage(translation: MovieTranslation | null, type: 'POSTER' | 'BACKDROP') {
  return translation?.images.find((i) => i.type === type) ?? null;
}

function localeLabel(locale: string) {
  return locale.replace('_', ' (').replace(/(.{2})$/, '$1)');
}

// ── Main component ────────────────────────────────────────────────────────────

export function MovieDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user, accountId } = useAuth();

  const [movie, setMovie] = useState<MovieDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedLocale, setSelectedLocale] = useState<string>('');

  useEffect(() => {
    if (!user || !accountId || !id) return;
    setLoading(true);
    getMovie(user, accountId, id)
      .then((data) => {
        setMovie(data);
        setSelectedLocale(data.originalLanguage);
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, id]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !movie) {
    return <Alert severity="error">{error ?? 'Movie not found.'}</Alert>;
  }

  const translation = movie.translations.find((t) => t.locale === selectedLocale) ?? null;
  const backdrop = getImage(translation, 'BACKDROP');
  const poster = getImage(translation, 'POSTER');

  const title = translation?.title || movie.originalTitle;
  const overview = translation?.overview ?? null;
  const tagline = translation?.tagline ?? null;

  const locales = movie.translations.map((t) => t.locale);

  return (
    <Box sx={{ mx: -3, mt: -3, minHeight: '100vh' }}>
      {/* ── Backdrop hero ── */}
      <Box
        sx={{
          position: 'relative',
          width: '100%',
          minHeight: { xs: 'auto', md: '90vh' },
          overflow: 'hidden',
          display: 'flex',
          alignItems: 'flex-end',
        }}
      >
        {/* Backdrop image */}
        {backdrop ? (
          <Box sx={{ position: 'absolute', inset: 0 }}>
            <BlurhashImage image={backdrop} alt="backdrop" />
          </Box>
        ) : (
          <Box sx={{ position: 'absolute', inset: 0, bgcolor: 'grey.900' }} />
        )}

        {/* Gradient overlays */}
        <Box
          sx={{
            position: 'absolute',
            inset: 0,
            background:
              'linear-gradient(to right, rgba(0,0,0,0.85) 0%, rgba(0,0,0,0.4) 60%, rgba(0,0,0,0.1) 100%)',
          }}
        />
        <Box
          sx={{
            position: 'absolute',
            inset: 0,
            background:
              'linear-gradient(to top, rgba(0,0,0,0.95) 0%, rgba(0,0,0,0.3) 40%, transparent 100%)',
          }}
        />

        {/* Back button */}
        <Box sx={{ position: 'absolute', top: 16, left: 16, zIndex: 10 }}>
          <Tooltip title="Back to movies">
            <IconButton onClick={() => navigate('/movies')} sx={{ color: 'white', bgcolor: 'rgba(0,0,0,0.4)', '&:hover': { bgcolor: 'rgba(0,0,0,0.6)' } }}>
              <ArrowBackIcon />
            </IconButton>
          </Tooltip>
        </Box>

        {/* Locale selector */}
        {locales.length > 0 && (
          <Box sx={{ position: 'absolute', top: 16, right: 16, zIndex: 10 }}>
            <Select
              value={selectedLocale}
              onChange={(e) => setSelectedLocale(e.target.value)}
              size="small"
              sx={{
                color: 'white',
                bgcolor: 'rgba(0,0,0,0.5)',
                backdropFilter: 'blur(8px)',
                '& .MuiOutlinedInput-notchedOutline': { borderColor: 'rgba(255,255,255,0.3)' },
                '&:hover .MuiOutlinedInput-notchedOutline': { borderColor: 'rgba(255,255,255,0.6)' },
                '& .MuiSvgIcon-root': { color: 'white' },
                minWidth: 140,
              }}
            >
              {locales.map((locale) => (
                <MenuItem key={locale} value={locale}>
                  {localeLabel(locale)}
                </MenuItem>
              ))}
            </Select>
          </Box>
        )}

        {/* Content */}
        <Box
          sx={{
            position: 'relative',
            zIndex: 1,
            width: '100%',
            px: { xs: 3, md: 6 },
            pb: { xs: 4, md: 6 },
            pt: { xs: 10, md: 6 },
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            alignItems: { xs: 'center', md: 'flex-end' },
            gap: 4,
          }}
        >
          {/* Poster */}
          <Box
            sx={{
              flexShrink: 0,
              width: { xs: 140, sm: 180, md: 220 },
              height: { xs: 210, sm: 270, md: 330 },
              borderRadius: 2,
              overflow: 'hidden',
              boxShadow: '0 8px 40px rgba(0,0,0,0.8)',
              bgcolor: 'grey.800',
            }}
          >
            {poster ? (
              <BlurhashImage image={poster} alt={`${title} poster`} />
            ) : (
              <Box
                sx={{
                  width: '100%',
                  height: '100%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  bgcolor: 'grey.800',
                }}
              >
                <Typography variant="caption" color="grey.500">
                  No poster
                </Typography>
              </Box>
            )}
          </Box>

          {/* Text info */}
          <Box sx={{ flex: 1, color: 'white', minWidth: 0 }}>
            <Typography
              variant="h3"
              fontWeight="bold"
              sx={{
                textShadow: '0 2px 8px rgba(0,0,0,0.8)',
                lineHeight: 1.15,
                mb: tagline ? 0.5 : 1.5,
                fontSize: { xs: '1.8rem', md: '2.5rem' },
              }}
            >
              {title}
            </Typography>

            {tagline && (
              <Typography
                variant="h6"
                sx={{
                  fontStyle: 'italic',
                  color: 'rgba(255,255,255,0.7)',
                  mb: 2,
                  fontWeight: 400,
                  fontSize: { xs: '0.95rem', md: '1.1rem' },
                }}
              >
                "{tagline}"
              </Typography>
            )}

            {/* Metadata chips */}
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5, mb: 2.5, alignItems: 'center' }}>
              {movie.releaseDate && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <CalendarTodayIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{movie.releaseDate}</Typography>
                </Box>
              )}
              {movie.runtime && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <AccessTimeIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{movie.runtime} min</Typography>
                </Box>
              )}
              {movie.originalLanguage && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <LanguageIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{localeLabel(movie.originalLanguage)}</Typography>
                </Box>
              )}
              {movie.imdbId && (
                <Chip
                  label={`IMDb ${movie.imdbId}`}
                  size="small"
                  sx={{
                    bgcolor: '#f5c518',
                    color: 'black',
                    fontWeight: 'bold',
                    fontSize: 11,
                    height: 22,
                  }}
                />
              )}
              {movie.tmdbId && (
                <Chip
                  label={`TMDB ${movie.tmdbId}`}
                  size="small"
                  sx={{
                    bgcolor: '#01b4e4',
                    color: 'white',
                    fontWeight: 'bold',
                    fontSize: 11,
                    height: 22,
                  }}
                />
              )}
            </Box>

            {/* Overview */}
            {overview && (
              <Typography
                variant="body1"
                sx={{
                  color: 'rgba(255,255,255,0.85)',
                  lineHeight: 1.7,
                  maxWidth: 680,
                  textShadow: '0 1px 4px rgba(0,0,0,0.6)',
                  display: '-webkit-box',
                  WebkitLineClamp: 5,
                  WebkitBoxOrient: 'vertical',
                  overflow: 'hidden',
                }}
              >
                {overview}
              </Typography>
            )}
          </Box>
        </Box>
      </Box>
    </Box>
  );
}
