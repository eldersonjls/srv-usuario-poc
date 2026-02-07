-- Seed user notifications (2000)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
)
INSERT INTO user_notifications (
  id,
  user_id,
  title,
  message,
  notification_type,
  action_url,
  is_read,
  read_at,
  created_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  format('Aviso %s', b.rn),
  format('Notificacao automatica numero %s para testes.', b.rn),
  CASE
    WHEN b.rn % 4 = 0 THEN 'SUCCESS'
    WHEN b.rn % 3 = 0 THEN 'WARNING'
    WHEN b.rn % 2 = 0 THEN 'ERROR'
    ELSE 'INFO'
  END,
  CASE WHEN b.rn % 5 = 0 THEN '/app/notifications' ELSE NULL END,
  (b.rn % 3 = 0),
  CASE WHEN b.rn % 3 = 0 THEN NOW() - (b.rn || ' hours')::interval ELSE NULL END,
  NOW() - (b.rn || ' hours')::interval
FROM base b
WHERE b.rn <= 2000
ON CONFLICT (id) DO NOTHING;
