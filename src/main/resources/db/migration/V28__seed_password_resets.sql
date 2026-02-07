-- Seed password resets (800)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
)
INSERT INTO password_resets (
  id,
  user_id,
  reset_token,
  is_used,
  expires_at,
  created_at,
  used_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  format('reset-token-%s', b.rn),
  (b.rn % 4 = 0),
  NOW() + ((b.rn % 5) || ' days')::interval,
  NOW() - ((b.rn % 10) || ' days')::interval,
  CASE WHEN b.rn % 4 = 0 THEN NOW() - ((b.rn % 3) || ' days')::interval ELSE NULL END
FROM base b
WHERE b.rn <= 800
ON CONFLICT (reset_token) DO NOTHING;
