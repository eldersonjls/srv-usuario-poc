-- Seed payment methods (1500)
WITH base AS (
  SELECT id, row_number() OVER (ORDER BY created_at, id) AS rn
  FROM users
)
INSERT INTO payment_methods (
  id,
  user_id,
  payment_type,
  card_last_four,
  card_brand,
  card_expiry_month,
  card_expiry_year,
  card_holder_name,
  payment_gateway_token,
  pix_key,
  pix_key_type,
  is_default,
  is_active,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  b.id,
  CASE
    WHEN b.rn % 3 = 0 THEN 'PIX'
    WHEN b.rn % 3 = 1 THEN 'CREDIT_CARD'
    ELSE 'DEBIT_CARD'
  END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE lpad((b.rn % 10000)::text, 4, '0') END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE (ARRAY['VISA','MASTERCARD','ELO','AMEX'])[((b.rn - 1) % 4) + 1] END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE lpad(((b.rn % 12) + 1)::text, 2, '0') END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE (2026 + (b.rn % 4))::text END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE format('Titular %s', b.rn) END,
  CASE WHEN b.rn % 3 = 0 THEN NULL ELSE format('tok_%s', b.rn) END,
  CASE WHEN b.rn % 3 = 0 THEN format('user%04s@viafluvial.local', b.rn) ELSE NULL END,
  CASE WHEN b.rn % 3 = 0 THEN 'EMAIL' ELSE NULL END,
  (b.rn % 5 = 0),
  (b.rn % 11 <> 0),
  NOW() - (b.rn || ' days')::interval,
  NOW() - (b.rn || ' days')::interval
FROM base b
WHERE b.rn <= 1500
ON CONFLICT (id) DO NOTHING;
