-- Seed user sessions (2000)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
)
INSERT INTO user_sessions (
  id,
  user_id,
  token,
  ip_address,
  user_agent,
  device_type,
  is_active,
  expires_at,
  created_at,
  last_activity
)
SELECT
  uuid_generate_v4(),
  b.id,
  format('seed-token-%s', b.rn),
  format('192.168.%s.%s', (b.rn % 255), ((b.rn + 50) % 255)),
  format('Mozilla/5.0 SeedAgent/%s', b.rn),
  CASE
    WHEN b.rn % 3 = 0 THEN 'MOBILE'
    WHEN b.rn % 3 = 1 THEN 'DESKTOP'
    ELSE 'TABLET'
  END,
  (b.rn % 7 <> 0),
  NOW() + ((b.rn % 30) || ' days')::interval,
  NOW() - (b.rn || ' hours')::interval,
  NOW() - (b.rn || ' minutes')::interval
FROM base b
WHERE b.rn <= 2000
ON CONFLICT (token) DO NOTHING;
