-- Seed passengers (1200)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
  WHERE user_type = 'PASSENGER'
)
INSERT INTO passengers (
  id,
  user_id,
  cpf,
  rg,
  birth_date,
  address,
  city,
  state,
  zip_code,
  preferred_cabin_type,
  total_trips,
  total_spent,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  format(
    '%s.%s.%s-%s',
    substr(cpf_raw, 1, 3),
    substr(cpf_raw, 4, 3),
    substr(cpf_raw, 7, 3),
    substr(cpf_raw, 10, 2)
  ),
  format('RG-%s', lpad(b.rn::text,8,'0')),
  (date '1975-01-01' + ((b.rn % 12000) * interval '1 day')),
  format('Rua %s, %s', (ARRAY['Rio','Sol','Floresta','Porto','Amazonas','Navegantes'])[((b.rn - 1) % 6) + 1], (100 + (b.rn % 900))),
  (ARRAY['Manaus','Belem','Santarem','Parintins','Macapa','Porto Velho','Sao Luis','Belterra'])[((b.rn - 1) % 8) + 1],
  (ARRAY['AM','PA','PA','AM','AP','RO','MA','PA'])[((b.rn - 1) % 8) + 1],
  format('69%03s-%03s', (b.rn % 1000), (b.rn % 1000)),
  CASE
    WHEN b.rn % 4 = 0 THEN 'VIP'
    WHEN b.rn % 3 = 0 THEN 'EXECUTIVE'
    WHEN b.rn % 2 = 0 THEN 'SUITE'
    ELSE 'STANDARD'
  END,
  (b.rn % 60),
  ROUND(((b.rn % 300) * 53.75)::numeric, 2),
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM (
  SELECT b.*, lpad(b.rn::text, 11, '0') AS cpf_raw
  FROM base b
) b
WHERE b.rn <= 1200
ON CONFLICT (user_id) DO NOTHING;
