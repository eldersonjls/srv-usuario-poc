-- Seed admins (500)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
  WHERE user_type = 'ADMIN'
)
INSERT INTO admins (
  id,
  user_id,
  role,
  permissions,
  department,
  employee_id,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  CASE
    WHEN b.rn % 10 = 0 THEN 'SUPER_ADMIN'
    WHEN b.rn % 4 = 0 THEN 'FINANCIAL'
    WHEN b.rn % 3 = 0 THEN 'SUPPORT'
    ELSE 'ADMIN'
  END,
  '{}'::jsonb,
  (ARRAY['Operacoes','Plataforma','Financeiro','Suporte'])[((b.rn - 1) % 4) + 1],
  format('EMP-%04s', b.rn),
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM base b
WHERE b.rn <= 500
ON CONFLICT (user_id) DO NOTHING;
