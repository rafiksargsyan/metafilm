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
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import { useAuth } from '../hooks/useAuth';
import { listTags, createTag, updateTag, deleteTag } from '../api/tags';
import type { Tag } from '../types';

function LocalizationsPanel({ localizations }: { localizations: Record<string, string> }) {
  const entries = Object.entries(localizations).sort(([a], [b]) => a.localeCompare(b));
  if (entries.length === 0) return <Typography color="text.secondary" variant="body2">No localizations</Typography>;
  return (
    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.75 }}>
      {entries.map(([locale, name]) => (
        <Tooltip key={locale} title={locale}>
          <Chip label={`${locale}: ${name}`} size="small" variant="outlined" sx={{ fontSize: 11 }} />
        </Tooltip>
      ))}
    </Box>
  );
}

function TagRow({ tag, onEdit, onDelete }: { tag: Tag; onEdit: (t: Tag) => void; onDelete: (t: Tag) => void }) {
  const [expanded, setExpanded] = useState(false);
  const count = Object.keys(tag.localizations).length;
  return (
    <>
      <TableRow hover>
        <TableCell sx={{ width: 32, pr: 0 }}>
          <IconButton size="small" onClick={() => setExpanded((v) => !v)}>
            {expanded ? <ExpandLessIcon fontSize="small" /> : <ExpandMoreIcon fontSize="small" />}
          </IconButton>
        </TableCell>
        <TableCell sx={{ fontFamily: 'monospace', fontSize: 13 }}>{tag.key}</TableCell>
        <TableCell>{tag.name}</TableCell>
        <TableCell>
          <Chip label={count} size="small" color={count > 0 ? 'success' : 'default'} variant="outlined" />
        </TableCell>
        <TableCell align="right">
          <IconButton size="small" onClick={() => onEdit(tag)}><EditIcon fontSize="small" /></IconButton>
          <IconButton size="small" color="error" onClick={() => onDelete(tag)}><DeleteIcon fontSize="small" /></IconButton>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell colSpan={5} sx={{ p: 0, border: 0 }}>
          <Collapse in={expanded} unmountOnExit>
            <Box sx={{ px: 3, py: 1.5, bgcolor: 'action.hover' }}>
              <LocalizationsPanel localizations={tag.localizations} />
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}

export function TagsPage() {
  const { user } = useAuth();
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [createOpen, setCreateOpen] = useState(false);
  const [createKey, setCreateKey] = useState('');
  const [createName, setCreateName] = useState('');
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState<string | null>(null);

  const [editTag, setEditTag] = useState<Tag | null>(null);
  const [editName, setEditName] = useState('');
  const [saving, setSaving] = useState(false);
  const [editError, setEditError] = useState<string | null>(null);

  const [deleteTag_, setDeleteTag] = useState<Tag | null>(null);
  const [deleting, setDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  const load = useCallback(() => {
    if (!user) return;
    setLoading(true);
    listTags(user)
      .then((data) => setTags(data.sort((a, b) => a.key.localeCompare(b.key))))
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [user]);

  useEffect(() => { load(); }, [load]);

  async function handleCreate() {
    if (!user || !createKey.trim() || !createName.trim()) return;
    setCreating(true);
    setCreateError(null);
    try {
      await createTag(user, { key: createKey.trim(), name: createName.trim() });
      setCreateOpen(false);
      setCreateKey('');
      setCreateName('');
      load();
    } catch (e: unknown) {
      setCreateError(e instanceof Error ? e.message : 'Failed to create');
    } finally {
      setCreating(false);
    }
  }

  async function handleEdit() {
    if (!user || !editTag || !editName.trim()) return;
    setSaving(true);
    setEditError(null);
    try {
      await updateTag(user, editTag.id, { name: editName.trim() });
      setEditTag(null);
      load();
    } catch (e: unknown) {
      setEditError(e instanceof Error ? e.message : 'Failed to update');
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (!user || !deleteTag_) return;
    setDeleting(true);
    setDeleteError(null);
    try {
      await deleteTag(user, deleteTag_.id);
      setDeleteTag(null);
      load();
    } catch (e: unknown) {
      setDeleteError(e instanceof Error ? e.message : 'Failed to delete');
    } finally {
      setDeleting(false);
    }
  }

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h5" fontWeight="bold">Tags</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => { setCreateError(null); setCreateOpen(true); }}>
          New Tag
        </Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
            <CircularProgress />
          </Box>
        ) : (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: 32 }} />
                <TableCell>Key</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Localizations</TableCell>
                <TableCell />
              </TableRow>
            </TableHead>
            <TableBody>
              {tags.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ color: 'text.secondary', py: 4 }}>
                    No tags yet
                  </TableCell>
                </TableRow>
              ) : (
                tags.map((tag) => (
                  <TagRow
                    key={tag.id}
                    tag={tag}
                    onEdit={(t) => { setEditError(null); setEditName(t.name); setEditTag(t); }}
                    onDelete={(t) => { setDeleteError(null); setDeleteTag(t); }}
                  />
                ))
              )}
            </TableBody>
          </Table>
        )}
      </Paper>

      {/* Create dialog */}
      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>New Tag</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
          {createError && <Alert severity="error">{createError}</Alert>}
          <TextField
            label="Key"
            value={createKey}
            onChange={(e) => setCreateKey(e.target.value)}
            fullWidth
            autoFocus
            helperText="Lowercase letters, digits, hyphens (e.g. science-fiction)"
          />
          <TextField
            label="Name (English)"
            value={createName}
            onChange={(e) => setCreateName(e.target.value)}
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCreateOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleCreate} disabled={creating || !createKey.trim() || !createName.trim()}>
            {creating ? <CircularProgress size={20} /> : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit dialog */}
      <Dialog open={editTag !== null} onClose={() => setEditTag(null)} maxWidth="xs" fullWidth>
        <DialogTitle>Edit Tag</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
          {editError && <Alert severity="error">{editError}</Alert>}
          <Box>
            <Typography variant="caption" color="text.secondary">Key</Typography>
            <Typography fontFamily="monospace">{editTag?.key}</Typography>
          </Box>
          <Divider />
          <TextField
            label="Name (English)"
            value={editName}
            onChange={(e) => setEditName(e.target.value)}
            fullWidth
            autoFocus
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditTag(null)}>Cancel</Button>
          <Button variant="contained" onClick={handleEdit} disabled={saving || !editName.trim()}>
            {saving ? <CircularProgress size={20} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete dialog */}
      <Dialog open={deleteTag_ !== null} onClose={() => setDeleteTag(null)} maxWidth="xs" fullWidth>
        <DialogTitle>Delete Tag</DialogTitle>
        <DialogContent>
          {deleteError && <Alert severity="error" sx={{ mb: 2 }}>{deleteError}</Alert>}
          <Typography>
            Delete tag <strong>{deleteTag_?.key}</strong>? It will be removed from all movies and TV shows.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteTag(null)}>Cancel</Button>
          <Button variant="contained" color="error" onClick={handleDelete} disabled={deleting}>
            {deleting ? <CircularProgress size={20} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
