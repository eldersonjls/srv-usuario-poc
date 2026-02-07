-- Seed user preferences (all users)
INSERT INTO user_preferences (
  id,
  user_id,
  email_notifications,
  sms_notifications,
  push_notifications,
  receive_promotions,
  receive_trip_updates,
  language,
  theme,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  u.id,
  (u.id IS NOT NULL),
  (row_number() OVER (ORDER BY u.created_at, u.id) % 4 = 0),
  (row_number() OVER (ORDER BY u.created_at, u.id) % 3 <> 0),
  (row_number() OVER (ORDER BY u.created_at, u.id) % 2 = 0),
  true,
  'pt-BR',
  CASE
    WHEN row_number() OVER (ORDER BY u.created_at, u.id) % 3 = 0 THEN 'dark'
    WHEN row_number() OVER (ORDER BY u.created_at, u.id) % 2 = 0 THEN 'auto'
    ELSE 'light'
  END,
  NOW() - (row_number() OVER (ORDER BY u.created_at, u.id) || ' days')::interval,
  NOW() - (row_number() OVER (ORDER BY u.created_at, u.id) || ' days')::interval
FROM users u
ON CONFLICT (user_id) DO NOTHING;
