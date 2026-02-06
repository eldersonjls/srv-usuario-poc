-- =====================================================
-- VIAFLUVIAL - Flyway V2 (Seed fictício para dev)
-- Observação: este seed é intencionalmente "ON CONFLICT DO NOTHING"
-- para permitir reexecução sem quebrar ambientes.
-- =====================================================

-- Usuários base
INSERT INTO users (id, user_type, email, password_hash, full_name, phone, status, email_verified, created_at, updated_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'PASSENGER', 'passenger.teste@viafluvial.com', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8tQkQ8JwQn6k2uYQFQZlq6bK0vY0kS', 'Passageiro Teste', '+55 92 99999-0001', 'ACTIVE', true, NOW(), NOW()),
  ('33333333-3333-3333-3333-333333333333', 'BOATMAN',    'boatman.teste@viafluvial.com',    '$2a$10$CwTycUXWue0Thq9StjUM0uJ8tQkQ8JwQn6k2uYQFQZlq6bK0vY0kS', 'Barqueiro Teste',  '+55 92 99999-0002', 'APPROVED', true, NOW(), NOW()),
  ('55555555-5555-5555-5555-555555555555', 'AGENCY',     'agencia.teste@viafluvial.com',     '$2a$10$CwTycUXWue0Thq9StjUM0uJ8tQkQ8JwQn6k2uYQFQZlq6bK0vY0kS', 'Agência Teste',    '+55 92 99999-0003', 'PENDING',  true, NOW(), NOW()),
  ('77777777-7777-7777-7777-777777777777', 'ADMIN',      'admin.teste@viafluvial.com',       '$2a$10$CwTycUXWue0Thq9StjUM0uJ8tQkQ8JwQn6k2uYQFQZlq6bK0vY0kS', 'Admin Teste',      '+55 92 99999-0004', 'ACTIVE',   true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Preferences
INSERT INTO user_preferences (id, user_id, email_notifications, sms_notifications, push_notifications, receive_promotions, receive_trip_updates, language, theme, created_at, updated_at)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', true,  false, true,  true,  true,  'pt-BR', 'light', NOW(), NOW()),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '33333333-3333-3333-3333-333333333333', true,  false, true,  false, true,  'pt-BR', 'auto',  NOW(), NOW()),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '55555555-5555-5555-5555-555555555555', true,  true,  true,  true,  true,  'pt-BR', 'dark',  NOW(), NOW()),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '77777777-7777-7777-7777-777777777777', true,  false, true,  false, true,  'pt-BR', 'light', NOW(), NOW())
ON CONFLICT (user_id) DO NOTHING;

-- Passenger
INSERT INTO passengers (id, user_id, cpf, rg, birth_date, address, city, state, zip_code, preferred_cabin_type, total_trips, total_spent, created_at, updated_at)
VALUES
  ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '123.456.789-01', 'MG-12.345.678', '1995-01-20', 'Rua Teste, 100', 'Manaus', 'AM', '69000-000', 'STANDARD', 3, 450.00, NOW(), NOW())
ON CONFLICT (cpf) DO NOTHING;

-- Boatman
INSERT INTO boatmen (
  id, user_id, cpf, rg, birth_date,
  company_name, cnpj, company_address, company_city, company_state, company_zip_code,
  document_cpf_url, document_cnpj_url, document_address_proof_url,
  rating, total_reviews, total_vessels, total_trips, total_revenue,
  admin_notes, approved_at, created_at, updated_at
)
VALUES
  (
    '44444444-4444-4444-4444-444444444444', '33333333-3333-3333-3333-333333333333',
    '987.654.321-00', 'AM-98.765.432', '1988-09-15',
    'Barcos do Rio LTDA', '12.345.678/0001-90', 'Av. Fluvial, 200', 'Manaus', 'AM', '69000-001',
    'https://storage.viafluvial.local/docs/cpf.pdf',
    'https://storage.viafluvial.local/docs/cnpj.pdf',
    'https://storage.viafluvial.local/docs/comp_residencia.pdf',
    4.6, 12, 2, 20, 12500.00,
    'Seed fictício', NOW(), NOW(), NOW()
  )
ON CONFLICT (cnpj) DO NOTHING;

-- Agency
INSERT INTO agencies (
  id, user_id, company_name, cnpj, trade_name,
  company_email, company_phone, whatsapp,
  address, city, state, zip_code,
  commission_percent,
  document_cnpj_url, document_contract_url,
  total_sales, total_revenue, total_commission_paid,
  bank_name, bank_account, bank_agency, pix_key,
  admin_notes, approved_at, created_at, updated_at
)
VALUES
  (
    '66666666-6666-6666-6666-666666666666', '55555555-5555-5555-5555-555555555555',
    'Agência Amazônia Viagens', '98.765.432/0001-10', 'Amazônia Viagens',
    'contato@amazoniaviagens.teste', '+55 92 3000-0000', '+55 92 99999-7777',
    'Rua do Porto, 10', 'Manaus', 'AM', '69000-002',
    10.00,
    'https://storage.viafluvial.local/docs/agencia_cnpj.pdf',
    'https://storage.viafluvial.local/docs/contrato.pdf',
    5, 9800.00, 980.00,
    'Banco Teste', '000123-4', '0001', 'contato@amazoniaviagens.teste',
    'Seed fictício', NULL, NOW(), NOW()
  )
ON CONFLICT (cnpj) DO NOTHING;

-- Admin
INSERT INTO admins (id, user_id, role, permissions, department, employee_id, created_at, updated_at)
VALUES
  ('88888888-8888-8888-8888-888888888888', '77777777-7777-7777-7777-777777777777', 'SUPER_ADMIN', '{}'::jsonb, 'Plataforma', 'EMP-0001', NOW(), NOW())
ON CONFLICT (user_id) DO NOTHING;

-- Approvals (referenciam IDs das entidades, não user_id)
INSERT INTO approvals (id, entity_type, entity_id, type, documents, status, created_at, updated_at)
VALUES
  ('99999999-9999-9999-9999-999999999991', 'BOATMAN', '44444444-4444-4444-4444-444444444444', 'KYC', '{"cpf":"ok","cnpj":"ok","addressProof":"ok"}', 'PENDING', NOW(), NOW()),
  ('99999999-9999-9999-9999-999999999992', 'AGENCY',  '66666666-6666-6666-6666-666666666666', 'KYC', '{"cnpj":"pending","contract":"pending"}', 'MORE_INFO_REQUIRED', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Payment methods
INSERT INTO payment_methods (id, user_id, payment_type, pix_key, pix_key_type, is_default, is_active, created_at, updated_at)
VALUES
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '11111111-1111-1111-1111-111111111111', 'PIX', 'passenger.teste@viafluvial.com', 'EMAIL', true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Sessions
INSERT INTO user_sessions (id, user_id, token, ip_address, user_agent, device_type, is_active, expires_at, created_at, last_activity)
VALUES
  ('dddddddd-dddd-dddd-dddd-ddddddddddd1', '11111111-1111-1111-1111-111111111111', 'seed-token-passenger-1', '127.0.0.1', 'Seed UA', 'DESKTOP', true, NOW() + INTERVAL '7 days', NOW(), NOW())
ON CONFLICT (token) DO NOTHING;

-- Password resets
INSERT INTO password_resets (id, user_id, reset_token, is_used, expires_at, created_at)
VALUES
  ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1', '11111111-1111-1111-1111-111111111111', 'seed-reset-token-1', false, NOW() + INTERVAL '1 day', NOW())
ON CONFLICT (reset_token) DO NOTHING;

-- Notifications
INSERT INTO user_notifications (id, user_id, title, message, notification_type, action_url, is_read, created_at)
VALUES
  ('cccccccc-cccc-cccc-cccc-ccccccccccc1', '11111111-1111-1111-1111-111111111111', 'Bem-vindo', 'Conta criada com sucesso (seed fictício).', 'SUCCESS', NULL, false, NOW()),
  ('cccccccc-cccc-cccc-cccc-ccccccccccc2', '33333333-3333-3333-3333-333333333333', 'Aprovação', 'Seu cadastro está em análise (seed fictício).', 'INFO', '/approvals', false, NOW())
ON CONFLICT (id) DO NOTHING;

-- Sessions
INSERT INTO user_sessions (id, user_id, token, ip_address, user_agent, device_type, is_active, expires_at, created_at, last_activity)
VALUES
  ('dddddddd-dddd-dddd-dddd-dddddddddd01', '11111111-1111-1111-1111-111111111111', 'seed-token-passenger', '127.0.0.1', 'seed-agent', 'MOBILE', true, NOW() + INTERVAL '30 days', NOW(), NOW()),
  ('dddddddd-dddd-dddd-dddd-dddddddddd02', '33333333-3333-3333-3333-333333333333', 'seed-token-boatman',   '127.0.0.1', 'seed-agent', 'DESKTOP', true, NOW() + INTERVAL '30 days', NOW(), NOW())
ON CONFLICT (token) DO NOTHING;

-- Password resets
INSERT INTO password_resets (id, user_id, reset_token, is_used, expires_at, created_at, used_at)
VALUES
  ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01', '11111111-1111-1111-1111-111111111111', 'seed-reset-token-1', false, NOW() + INTERVAL '1 day', NOW(), NULL)
ON CONFLICT (reset_token) DO NOTHING;
