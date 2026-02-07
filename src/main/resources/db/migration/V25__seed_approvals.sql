-- Seed approvals (1200)
WITH boatmen_base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM boatmen
),
agency_base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM agencies
),
user_base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
)
INSERT INTO approvals (
  id,
  entity_type,
  entity_id,
  type,
  documents,
  status,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  'BOATMAN',
  b.id,
  'KYC',
  '{"cpf":"ok","cnpj":"ok","addressProof":"ok"}',
  CASE
    WHEN b.rn % 6 = 0 THEN 'ACTIVE'
    WHEN b.rn % 5 = 0 THEN 'MORE_INFO_REQUIRED'
    WHEN b.rn % 4 = 0 THEN 'BLOCKED'
    WHEN b.rn % 3 = 0 THEN 'APPROVED'
    ELSE 'PENDING'
  END,
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM boatmen_base b
WHERE b.rn <= 400

UNION ALL

SELECT
  uuid_generate_v4(),
  'AGENCY',
  a.id,
  'KYC',
  '{"cnpj":"ok","contract":"ok"}',
  CASE
    WHEN a.rn % 6 = 0 THEN 'ACTIVE'
    WHEN a.rn % 5 = 0 THEN 'MORE_INFO_REQUIRED'
    WHEN a.rn % 4 = 0 THEN 'BLOCKED'
    WHEN a.rn % 3 = 0 THEN 'APPROVED'
    ELSE 'PENDING'
  END,
  NOW() - (a.rn || ' days')::interval,
  NOW() - (a.rn || ' days')::interval
FROM agency_base a
WHERE a.rn <= 300

UNION ALL

SELECT
  uuid_generate_v4(),
  'USER',
  u.id,
  'ONBOARDING',
  '{"email":"ok"}',
  CASE
    WHEN u.rn % 7 = 0 THEN 'BLOCKED'
    WHEN u.rn % 6 = 0 THEN 'ACTIVE'
    WHEN u.rn % 5 = 0 THEN 'MORE_INFO_REQUIRED'
    WHEN u.rn % 4 = 0 THEN 'APPROVED'
    ELSE 'PENDING'
  END,
  NOW() - (u.rn || ' days')::interval,
  NOW() - (u.rn || ' days')::interval
FROM user_base u
WHERE u.rn <= 500
ON CONFLICT (id) DO NOTHING;
