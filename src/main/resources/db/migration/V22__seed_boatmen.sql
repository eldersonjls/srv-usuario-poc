-- Seed boatmen (900)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
  WHERE user_type = 'BOATMAN'
)
INSERT INTO boatmen (
  id,
  user_id,
  cpf,
  rg,
  birth_date,
  company_name,
  cnpj,
  company_address,
  company_city,
  company_state,
  company_zip_code,
  document_cpf_url,
  document_cnpj_url,
  document_address_proof_url,
  rating,
  total_reviews,
  total_vessels,
  total_trips,
  total_revenue,
  admin_notes,
  approved_at,
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
  format('RG-%s', lpad((b.rn + 1000)::text,8,'0')),
  (date '1970-01-01' + ((b.rn % 15000) * interval '1 day')),
  format('Transporte Fluvial %s', b.rn),
  format('%s.%s.%s/%s-%s', lpad(b.rn::text,2,'0'), lpad((b.rn + 10)::text,3,'0'), lpad((b.rn + 20)::text,3,'0'), lpad((b.rn + 30)::text,4,'0'), lpad((b.rn + 40)::text,2,'0')),
  format('Av. %s, %s', (ARRAY['Sol','Rio','Navegantes','Floresta','Porto'])[((b.rn - 1) % 5) + 1], (200 + (b.rn % 800))),
  (ARRAY['Manaus','Itacoatiara','Parintins','Belem','Santarem','Macapa'])[((b.rn - 1) % 6) + 1],
  (ARRAY['AM','AM','AM','PA','PA','AP'])[((b.rn - 1) % 6) + 1],
  format('69%03s-%03s', (b.rn % 1000), (b.rn % 1000)),
  format('https://docs.viafluvial.local/boatman/%s/cpf.pdf', b.rn),
  format('https://docs.viafluvial.local/boatman/%s/cnpj.pdf', b.rn),
  format('https://docs.viafluvial.local/boatman/%s/address.pdf', b.rn),
  ROUND(((b.rn % 50)::numeric / 10), 1),
  (b.rn % 200),
  (1 + (b.rn % 6)),
  (b.rn % 300),
  ROUND(((b.rn % 900) * 120.50)::numeric, 2),
  'Seed realista para testes',
  CASE WHEN b.rn % 4 = 0 THEN NOW() - (b.rn || ' days')::interval ELSE NULL END,
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM (
  SELECT b.*, lpad((b.rn + 5000)::text, 11, '0') AS cpf_raw
  FROM base b
) b
WHERE b.rn <= 900
ON CONFLICT (user_id) DO NOTHING;
