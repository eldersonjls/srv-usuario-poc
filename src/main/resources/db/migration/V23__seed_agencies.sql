-- Seed agencies (700)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
  WHERE user_type = 'AGENCY'
)
INSERT INTO agencies (
  id,
  user_id,
  company_name,
  cnpj,
  trade_name,
  company_email,
  company_phone,
  whatsapp,
  address,
  city,
  state,
  zip_code,
  commission_percent,
  document_cnpj_url,
  document_contract_url,
  total_sales,
  total_revenue,
  total_commission_paid,
  bank_name,
  bank_account,
  bank_agency,
  pix_key,
  admin_notes,
  approved_at,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  format('Agencia Fluvial %s', b.rn),
  format('%s.%s.%s/%s-%s', lpad(b.rn::text,2,'0'), lpad((b.rn + 11)::text,3,'0'), lpad((b.rn + 21)::text,3,'0'), lpad((b.rn + 31)::text,4,'0'), lpad((b.rn + 41)::text,2,'0')),
  format('Fluvial %s', b.rn),
  format('contato%04s@agencia.viafluvial.local', b.rn),
  format('+55 92 3%03s-%04s', (b.rn % 1000), (b.rn % 10000)),
  format('+55 92 9%08s', (b.rn + 5000)),
  format('Rua %s, %s', (ARRAY['Porto','Centro','Navegantes','Mercado','Orla'])[((b.rn - 1) % 5) + 1], (50 + (b.rn % 900))),
  (ARRAY['Manaus','Belem','Santarem','Macapa','Porto Velho','Sao Luis'])[((b.rn - 1) % 6) + 1],
  (ARRAY['AM','PA','PA','AP','RO','MA'])[((b.rn - 1) % 6) + 1],
  format('69%03s-%03s', (b.rn % 1000), (b.rn % 1000)),
  ROUND((5 + (b.rn % 20))::numeric, 2),
  format('https://docs.viafluvial.local/agency/%s/cnpj.pdf', b.rn),
  format('https://docs.viafluvial.local/agency/%s/contract.pdf', b.rn),
  (b.rn % 800),
  ROUND(((b.rn % 900) * 210.75)::numeric, 2),
  ROUND(((b.rn % 900) * 21.50)::numeric, 2),
  (ARRAY['Banco do Brasil','Caixa','Itau','Bradesco','Santander'])[((b.rn - 1) % 5) + 1],
  format('%05s-%s', (b.rn % 100000), (b.rn % 10)),
  format('%04s', (b.rn % 10000)),
  format('pix%04s@agencia.viafluvial.local', b.rn),
  'Seed realista para testes',
  CASE WHEN b.rn % 3 = 0 THEN NOW() - (b.rn || ' days')::interval ELSE NULL END,
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM base b
WHERE b.rn <= 700
ON CONFLICT (user_id) DO NOTHING;
