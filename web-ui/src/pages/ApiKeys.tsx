import { useEffect, useState } from 'react';
import {
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  TableContainer,
  CircularProgress,
  Alert,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  Tooltip,
  Box,
  Snackbar,
  Chip,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import BlockIcon from '@mui/icons-material/Block';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { useAuth } from '../hooks/useAuth';
import { listApiKeys, createApiKey, disableApiKey, enableApiKey, deleteApiKey } from '../api/apikeys';
import type { ApiKey } from '../types/api.types';

export function ApiKeys() {
  const { user, accountId, userId } = useAuth();
  const [keys, setKeys] = useState<ApiKey[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [createOpen, setCreateOpen] = useState(false);
  const [description, setDescription] = useState('');
  const [creating, setCreating] = useState(false);

  const [newKey, setNewKey] = useState<string | null>(null);
  const [copiedSnack, setCopiedSnack] = useState(false);

  const [confirmDelete, setConfirmDelete] = useState<ApiKey | null>(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    if (!user || !accountId || !userId) return;
    fetchKeys();
  }, [user, accountId, userId]);

  function fetchKeys() {
    if (!user || !accountId || !userId) return;
    setLoading(true);
    setError(null);
    listApiKeys(user, accountId, userId)
      .then(setKeys)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }

  function handleCreate() {
    if (!user || !accountId || !userId || !description.trim()) return;
    setCreating(true);
    createApiKey(user, accountId, userId, description.trim())
      .then((created) => {
        setNewKey(created.key);
        setCreateOpen(false);
        setDescription('');
        fetchKeys();
      })
      .catch((e) => setError(e.message))
      .finally(() => setCreating(false));
  }

  function handleToggle(key: ApiKey) {
    if (!user || !accountId || !userId) return;
    const action = key.disabled
      ? enableApiKey(user, accountId, userId, key.id)
      : disableApiKey(user, accountId, userId, key.id);
    action
      .then(() => fetchKeys())
      .catch((e) => setError(e.message));
  }

  function handleDelete() {
    if (!user || !accountId || !userId || !confirmDelete) return;
    setDeleting(true);
    deleteApiKey(user, accountId, userId, confirmDelete.id)
      .then(() => {
        setConfirmDelete(null);
        fetchKeys();
      })
      .catch((e) => setError(e.message))
      .finally(() => setDeleting(false));
  }

  function copyKey(key: string) {
    navigator.clipboard.writeText(key).then(() => setCopiedSnack(true));
  }

  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h5" fontWeight="bold">
          API Keys
        </Typography>
        <Button variant="contained" onClick={() => setCreateOpen(true)} disabled={keys.length >= 2}>
          New API Key
        </Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>{error}</Alert>}

      {newKey && (
        <Alert
          severity="success"
          sx={{ mb: 2, wordBreak: 'break-all' }}
          action={
            <IconButton size="small" color="inherit" onClick={() => copyKey(newKey)}>
              <ContentCopyIcon fontSize="small" />
            </IconButton>
          }
          onClose={() => setNewKey(null)}
        >
          <Typography variant="body2" fontWeight="bold" gutterBottom>
            Save this key — it won't be shown again.
          </Typography>
          <Typography variant="body2" sx={{ fontFamily: 'monospace' }}>
            {newKey}
          </Typography>
        </Alert>
      )}

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Description</TableCell>
                <TableCell>Key ID</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Last Used</TableCell>
                <TableCell />
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                    <CircularProgress size={24} />
                  </TableCell>
                </TableRow>
              ) : keys.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ color: 'text.secondary' }}>
                    No API keys yet.
                  </TableCell>
                </TableRow>
              ) : (
                keys.map((k) => (
                  <TableRow key={k.id}>
                    <TableCell>{k.description}</TableCell>
                    <TableCell sx={{ fontFamily: 'monospace', fontSize: 12 }}>{k.id}</TableCell>
                    <TableCell>
                      <Chip
                        label={k.disabled ? 'Disabled' : 'Active'}
                        color={k.disabled ? 'default' : 'success'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      {k.lastAccessTime
                        ? new Date(k.lastAccessTime).toLocaleString()
                        : '—'}
                    </TableCell>
                    <TableCell align="right">
                      <Tooltip title={k.disabled ? 'Enable' : 'Disable'}>
                        <IconButton size="small" onClick={() => handleToggle(k)}>
                          {k.disabled ? (
                            <CheckCircleIcon fontSize="small" color="success" />
                          ) : (
                            <BlockIcon fontSize="small" />
                          )}
                        </IconButton>
                      </Tooltip>
                      <Tooltip title={k.disabled ? 'Delete' : 'Disable first to delete'}>
                        <span>
                          <IconButton
                            size="small"
                            color="error"
                            disabled={!k.disabled}
                            onClick={() => setConfirmDelete(k)}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </span>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {/* Create dialog */}
      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} fullWidth maxWidth="xs">
        <DialogTitle>New API Key</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            label="Description"
            fullWidth
            margin="dense"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleCreate()}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCreateOpen(false)}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleCreate}
            disabled={creating || !description.trim()}
          >
            {creating ? <CircularProgress size={18} /> : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete confirm dialog */}
      <Dialog open={Boolean(confirmDelete)} onClose={() => setConfirmDelete(null)}>
        <DialogTitle>Delete API Key</DialogTitle>
        <DialogContent>
          <Typography>
            "{confirmDelete?.description}" will be permanently deleted.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDelete(null)}>Cancel</Button>
          <Button variant="contained" color="error" onClick={handleDelete} disabled={deleting}>
            {deleting ? <CircularProgress size={18} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={copiedSnack}
        autoHideDuration={2000}
        onClose={() => setCopiedSnack(false)}
        message="Key copied to clipboard"
      />
    </>
  );
}
