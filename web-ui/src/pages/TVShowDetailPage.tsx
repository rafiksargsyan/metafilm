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
import LanguageIcon from '@mui/icons-material/Language';
import { decode } from 'blurhash';
import { useAuth } from '../hooks/useAuth';
import { getTVShow, listSeasons } from '../api/tvshows';
import type { TVShowDetail, TVShowImage, TVShowTranslation, Season, Tag } from '../types/api.types';
import { localeLabel } from '../utils/localeLabel';

function BlurhashImage({
  image,
  alt,
  style,
}: {
  image: TVShowImage;
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
      {image.url && (
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
      )}
    </Box>
  );
}

function getImage(translation: TVShowTranslation | null, type: 'POSTER' | 'BACKDROP') {
  return translation?.images.find((i) => i.type === type) ?? null;
}

export function TVShowDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user, accountId } = useAuth();

  const [tvShow, setTVShow] = useState<TVShowDetail | null>(null);
  const [seasons, setSeasons] = useState<Season[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedLocale, setSelectedLocale] = useState<string>('');

  useEffect(() => {
    if (!user || !accountId || !id) return;
    setLoading(true);
    Promise.all([getTVShow(user, accountId, id), listSeasons(user, accountId, id)])
      .then(([show, seas]) => {
        setTVShow(show);
        setSeasons(seas);
        setSelectedLocale(show.originalLanguage);
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user, accountId, id]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !tvShow) {
    return <Alert severity="error">{error ?? 'TV Show not found.'}</Alert>;
  }

  const translation = tvShow.translations.find((t) => t.locale === selectedLocale) ?? null;
  const backdrop = getImage(translation, 'BACKDROP');
  const poster = getImage(translation, 'POSTER');

  const title = translation?.title || tvShow.originalTitle;
  const overview = translation?.overview ?? null;
  const tagline = translation?.tagline ?? null;

  const locales = tvShow.translations.map((t) => t.locale);

  return (
    <Box sx={{ mx: -3, mt: -3, minHeight: '100vh' }}>
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
        {backdrop ? (
          <Box sx={{ position: 'absolute', inset: 0 }}>
            <BlurhashImage image={backdrop} alt="backdrop" />
          </Box>
        ) : (
          <Box sx={{ position: 'absolute', inset: 0, bgcolor: 'grey.900' }} />
        )}

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

        <Box sx={{ position: 'absolute', top: 16, left: 16, zIndex: 10 }}>
          <Tooltip title="Back to TV shows">
            <IconButton
              onClick={() => navigate('/tvshows')}
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

            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5, mb: 2.5, alignItems: 'center' }}>
              {tvShow.firstAirDate && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <CalendarTodayIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{tvShow.firstAirDate}</Typography>
                </Box>
              )}
              {tvShow.originalLanguage && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'rgba(255,255,255,0.8)' }}>
                  <LanguageIcon sx={{ fontSize: 15 }} />
                  <Typography variant="body2">{localeLabel(tvShow.originalLanguage)}</Typography>
                </Box>
              )}
              {tvShow.imdbId && (
                <Chip
                  label={`IMDb ${tvShow.imdbId}`}
                  size="small"
                  sx={{ bgcolor: '#f5c518', color: 'black', fontWeight: 'bold', fontSize: 11, height: 22 }}
                />
              )}
              {tvShow.tmdbId && (
                <Chip
                  label={`TMDB ${tvShow.tmdbId}`}
                  size="small"
                  sx={{ bgcolor: '#01b4e4', color: 'white', fontWeight: 'bold', fontSize: 11, height: 22 }}
                />
              )}
            </Box>

            {/* Genre tags */}
            {tvShow.tags.length > 0 && (
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.75, mb: 2 }}>
                {tvShow.tags.map((tag: Tag) => (
                  <Chip
                    key={tag.id}
                    label={tag.localizations[selectedLocale] ?? tag.name}
                    size="small"
                    sx={{
                      bgcolor: 'rgba(255,255,255,0.15)',
                      color: 'rgba(255,255,255,0.9)',
                      backdropFilter: 'blur(4px)',
                      fontSize: 11,
                      height: 22,
                    }}
                  />
                ))}
              </Box>
            )}

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

      <Box sx={{ px: { xs: 3, md: 6 }, py: 4 }}>
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
                      No seasons yet.
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
