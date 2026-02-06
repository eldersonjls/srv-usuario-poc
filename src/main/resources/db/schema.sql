-- =====================================================
-- VIAFLUVIAL - SCHEMA COMPLETO DE USUÁRIOS
-- PostgreSQL 12+
-- =====================================================

-- Habilitar extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- 1. TABELA PRINCIPAL DE USUÁRIOS
-- =====================================================
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  
  -- Tipo de usuário
  user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('PASSENGER', 'BOATMAN', 'AGENCY', 'ADMIN')),
  
  -- Dados de autenticação
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  
  -- Dados pessoais básicos
  full_name VARCHAR(255) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  
  -- Status e controle
  status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'ACTIVE', 'BLOCKED')),
  email_verified BOOLEAN DEFAULT false,
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW(),
  last_login TIMESTAMP
);

-- Índices para users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_created_at ON users(created_at);

-- =====================================================
-- 2. TABELA DE PASSAGEIROS
-- =====================================================
CREATE TABLE passengers (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Documentos
  cpf VARCHAR(14) UNIQUE NOT NULL,
  rg VARCHAR(20),
  birth_date DATE,
  
  -- Endereço
  address VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(2),
  zip_code VARCHAR(10),
  
  -- Preferências
  preferred_cabin_type VARCHAR(20) CHECK (preferred_cabin_type IN ('STANDARD', 'EXECUTIVE', 'VIP', 'SUITE')),
  
  -- Estatísticas (calculadas)
  total_trips INTEGER DEFAULT 0 CHECK (total_trips >= 0),
  total_spent DECIMAL(10,2) DEFAULT 0.00 CHECK (total_spent >= 0),
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para passengers
CREATE INDEX idx_passengers_user_id ON passengers(user_id);
CREATE INDEX idx_passengers_cpf ON passengers(cpf);
CREATE INDEX idx_passengers_total_trips ON passengers(total_trips);

-- =====================================================
-- 3. TABELA DE BARQUEIROS
-- =====================================================
CREATE TABLE boatmen (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Dados pessoais
  cpf VARCHAR(14) UNIQUE NOT NULL,
  rg VARCHAR(20),
  birth_date DATE,
  
  -- Dados da empresa
  company_name VARCHAR(255) NOT NULL,
  cnpj VARCHAR(18) UNIQUE NOT NULL,
  company_address VARCHAR(255),
  company_city VARCHAR(100),
  company_state VARCHAR(2),
  company_zip_code VARCHAR(10),
  
  -- Documentos enviados (URLs do storage)
  document_cpf_url VARCHAR(500),
  document_cnpj_url VARCHAR(500),
  document_address_proof_url VARCHAR(500),
  
  -- Avaliação
  rating DECIMAL(2,1) DEFAULT 0.0 CHECK (rating >= 0.0 AND rating <= 5.0),
  total_reviews INTEGER DEFAULT 0 CHECK (total_reviews >= 0),
  
  -- Estatísticas (calculadas)
  total_vessels INTEGER DEFAULT 0 CHECK (total_vessels >= 0),
  total_trips INTEGER DEFAULT 0 CHECK (total_trips >= 0),
  total_revenue DECIMAL(12,2) DEFAULT 0.00 CHECK (total_revenue >= 0),
  
  -- Observações administrativas
  admin_notes TEXT,
  
  -- Timestamps
  approved_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para boatmen
CREATE INDEX idx_boatmen_user_id ON boatmen(user_id);
CREATE INDEX idx_boatmen_cpf ON boatmen(cpf);
CREATE INDEX idx_boatmen_cnpj ON boatmen(cnpj);
CREATE INDEX idx_boatmen_rating ON boatmen(rating);
CREATE INDEX idx_boatmen_total_trips ON boatmen(total_trips);

-- =====================================================
-- 4. TABELA DE AGÊNCIAS
-- =====================================================
CREATE TABLE agencies (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Dados da agência
  company_name VARCHAR(255) NOT NULL,
  cnpj VARCHAR(18) UNIQUE NOT NULL,
  trade_name VARCHAR(255),
  
  -- Contato
  company_email VARCHAR(255),
  company_phone VARCHAR(20),
  whatsapp VARCHAR(20),
  
  -- Endereço
  address VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(2),
  zip_code VARCHAR(10),
  
  -- Comissionamento
  commission_percent DECIMAL(5,2) DEFAULT 10.00 CHECK (commission_percent >= 0 AND commission_percent <= 100),
  
  -- Documentos
  document_cnpj_url VARCHAR(500),
  document_contract_url VARCHAR(500),
  
  -- Estatísticas (calculadas)
  total_sales INTEGER DEFAULT 0 CHECK (total_sales >= 0),
  total_revenue DECIMAL(12,2) DEFAULT 0.00 CHECK (total_revenue >= 0),
  total_commission_paid DECIMAL(12,2) DEFAULT 0.00 CHECK (total_commission_paid >= 0),
  
  -- Dados bancários (para repasse)
  bank_name VARCHAR(100),
  bank_account VARCHAR(50),
  bank_agency VARCHAR(20),
  pix_key VARCHAR(100),
  
  -- Observações administrativas
  admin_notes TEXT,
  
  -- Timestamps
  approved_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para agencies
CREATE INDEX idx_agencies_user_id ON agencies(user_id);
CREATE INDEX idx_agencies_cnpj ON agencies(cnpj);
CREATE INDEX idx_agencies_total_sales ON agencies(total_sales);
CREATE INDEX idx_agencies_commission_percent ON agencies(commission_percent);

-- =====================================================
-- 5. TABELA DE ADMINISTRADORES
-- =====================================================
CREATE TABLE admins (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Nível de acesso
  role VARCHAR(50) NOT NULL CHECK (role IN ('SUPER_ADMIN', 'ADMIN', 'FINANCIAL', 'SUPPORT')),
  
  -- Permissões específicas (JSON)
  permissions JSONB DEFAULT '{}',
  
  -- Informações adicionais
  department VARCHAR(100),
  employee_id VARCHAR(50),
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para admins
CREATE INDEX idx_admins_user_id ON admins(user_id);
CREATE INDEX idx_admins_role ON admins(role);
CREATE INDEX idx_admins_permissions ON admins USING GIN (permissions);

-- =====================================================
-- 5.1 TABELA DE APROVAÇÕES
-- =====================================================
CREATE TABLE approvals (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  entity_type VARCHAR(30) NOT NULL CHECK (entity_type IN ('USER', 'BOATMAN', 'AGENCY')),
  entity_id UUID NOT NULL,
  type VARCHAR(50) NOT NULL,
  documents TEXT,
  status VARCHAR(30) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'MORE_INFO_REQUIRED', 'APPROVED', 'ACTIVE', 'BLOCKED')),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_approvals_entity ON approvals(entity_type, entity_id);
CREATE INDEX idx_approvals_status ON approvals(status);
CREATE INDEX idx_approvals_created_at ON approvals(created_at);

-- =====================================================
-- 6. TABELA DE MÉTODOS DE PAGAMENTO
-- =====================================================
CREATE TABLE payment_methods (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Tipo de pagamento
  payment_type VARCHAR(20) NOT NULL CHECK (payment_type IN ('CREDIT_CARD', 'DEBIT_CARD', 'PIX')),
  
  -- Dados do cartão (criptografados no backend)
  card_last_four VARCHAR(4),
  card_brand VARCHAR(20) CHECK (card_brand IN ('VISA', 'MASTERCARD', 'ELO', 'AMEX', 'HIPERCARD', 'DINERS')),
  card_expiry_month VARCHAR(2),
  card_expiry_year VARCHAR(4),
  card_holder_name VARCHAR(255),
  
  -- Token do gateway de pagamento
  payment_gateway_token VARCHAR(255),
  
  -- PIX
  pix_key VARCHAR(100),
  pix_key_type VARCHAR(20) CHECK (pix_key_type IN ('CPF', 'CNPJ', 'EMAIL', 'PHONE', 'RANDOM')),
  
  -- Status
  is_default BOOLEAN DEFAULT false,
  is_active BOOLEAN DEFAULT true,
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para payment_methods
CREATE INDEX idx_payment_methods_user_id ON payment_methods(user_id);
CREATE INDEX idx_payment_methods_is_default ON payment_methods(user_id, is_default) WHERE is_default = true;

-- =====================================================
-- 7. TABELA DE SESSÕES
-- =====================================================
CREATE TABLE user_sessions (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Dados da sessão
  token VARCHAR(500) UNIQUE NOT NULL,
  ip_address VARCHAR(50),
  user_agent TEXT,
  device_type VARCHAR(50) CHECK (device_type IN ('MOBILE', 'DESKTOP', 'TABLET')),
  
  -- Controle
  is_active BOOLEAN DEFAULT true,
  expires_at TIMESTAMP NOT NULL,
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  last_activity TIMESTAMP DEFAULT NOW()
);

-- Índices para user_sessions
CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_token ON user_sessions(token);
CREATE INDEX idx_user_sessions_expires_at ON user_sessions(expires_at);
CREATE INDEX idx_user_sessions_is_active ON user_sessions(is_active) WHERE is_active = true;

-- =====================================================
-- 8. TABELA DE RECUPERAÇÃO DE SENHA
-- =====================================================
CREATE TABLE password_resets (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Token de recuperação
  reset_token VARCHAR(255) UNIQUE NOT NULL,
  
  -- Status
  is_used BOOLEAN DEFAULT false,
  expires_at TIMESTAMP NOT NULL,
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  used_at TIMESTAMP
);

-- Índices para password_resets
CREATE INDEX idx_password_resets_user_id ON password_resets(user_id);
CREATE INDEX idx_password_resets_token ON password_resets(reset_token);
CREATE INDEX idx_password_resets_expires_at ON password_resets(expires_at);

-- =====================================================
-- 9. TABELA DE NOTIFICAÇÕES
-- =====================================================
CREATE TABLE user_notifications (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Conteúdo
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  notification_type VARCHAR(50) CHECK (notification_type IN ('INFO', 'SUCCESS', 'WARNING', 'ERROR')),
  
  -- Link/ação
  action_url VARCHAR(500),
  
  -- Status
  is_read BOOLEAN DEFAULT false,
  read_at TIMESTAMP,
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW()
);

-- Índices para user_notifications
CREATE INDEX idx_user_notifications_user_id ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_is_read ON user_notifications(user_id, is_read) WHERE is_read = false;
CREATE INDEX idx_user_notifications_created_at ON user_notifications(created_at DESC);

-- =====================================================
-- 10. TABELA DE PREFERÊNCIAS
-- =====================================================
CREATE TABLE user_preferences (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  
  -- Notificações
  email_notifications BOOLEAN DEFAULT true,
  sms_notifications BOOLEAN DEFAULT false,
  push_notifications BOOLEAN DEFAULT true,
  
  -- Preferências de email
  receive_promotions BOOLEAN DEFAULT true,
  receive_trip_updates BOOLEAN DEFAULT true,
  
  -- Idioma
  language VARCHAR(5) DEFAULT 'pt-BR',
  
  -- Tema
  theme VARCHAR(20) DEFAULT 'light' CHECK (theme IN ('light', 'dark', 'auto')),
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- Índices para user_preferences
CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);

-- =====================================================
-- TRIGGERS PARA ATUALIZAR updated_at
-- =====================================================

-- Função para atualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Aplicar trigger em todas as tabelas com updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_passengers_updated_at BEFORE UPDATE ON passengers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_boatmen_updated_at BEFORE UPDATE ON boatmen
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_agencies_updated_at BEFORE UPDATE ON agencies
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_admins_updated_at BEFORE UPDATE ON admins
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payment_methods_updated_at BEFORE UPDATE ON payment_methods
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_preferences_updated_at BEFORE UPDATE ON user_preferences
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================
