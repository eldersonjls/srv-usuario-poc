-- Seed users (3800)
WITH data AS (
  SELECT
    gs,
    CASE
      WHEN gs <= 1500 THEN 'PASSENGER'
      WHEN gs <= 2500 THEN 'BOATMAN'
      WHEN gs <= 3200 THEN 'AGENCY'
      ELSE 'ADMIN'
    END AS user_type,
    (ARRAY['Ana','Bruno','Carla','Diego','Elaine','Fabio','Gisele','Henrique','Isabela','Joao','Karla','Luiz'])[((gs - 1) % 12) + 1] AS first_name,
    (ARRAY['Silva','Souza','Oliveira','Santos','Lima','Pereira','Costa','Rodrigues','Almeida','Gomes','Barbosa','Ribeiro'])[((gs - 1) % 12) + 1] AS last_name
  FROM generate_series(1, 3800) gs
)
INSERT INTO users (
  id,
  user_type,
  email,
  password_hash,
  full_name,
  phone,
  status,
  email_verified,
  created_at,
  updated_at,
  last_login
)
SELECT
  uuid_generate_v4(),
  user_type,
  format('user%04s@viafluvial.local', gs),
  '$2a$10$CwTycUXWue0Thq9StjUM0uJ8tQkQ8JwQn6k2uYQFQZlq6bK0vY0kS',
  format('%s %s', first_name, last_name),
  format('+55 92 9%08s', gs),
  CASE
    WHEN gs % 20 IN (0, 1) THEN 'BLOCKED'
    WHEN gs % 20 IN (2, 3, 4) THEN 'PENDING'
    WHEN gs % 20 IN (5, 6, 7, 8) THEN 'APPROVED'
    ELSE 'ACTIVE'
  END,
  (gs % 5 >= 2),
  NOW() - (gs || ' days')::interval,
  NOW() - (gs || ' days')::interval,
  CASE WHEN gs % 3 = 0 THEN NOW() - (gs || ' hours')::interval ELSE NULL END
FROM data
ON CONFLICT (email) DO NOTHING;
