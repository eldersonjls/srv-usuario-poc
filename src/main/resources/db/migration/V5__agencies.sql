CREATE TABLE agencies (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  company_name VARCHAR(255) NOT NULL,
  cnpj VARCHAR(18) UNIQUE NOT NULL,
  trade_name VARCHAR(255),
  company_email VARCHAR(255),
  company_phone VARCHAR(20),
  whatsapp VARCHAR(20),
  address VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(2),
  zip_code VARCHAR(10),
  commission_percent DECIMAL(5,2) DEFAULT 10.00 CHECK (commission_percent >= 0 AND commission_percent <= 100),
  document_cnpj_url VARCHAR(500),
  document_contract_url VARCHAR(500),
  total_sales INTEGER DEFAULT 0 CHECK (total_sales >= 0),
  total_revenue DECIMAL(12,2) DEFAULT 0.00 CHECK (total_revenue >= 0),
  total_commission_paid DECIMAL(12,2) DEFAULT 0.00 CHECK (total_commission_paid >= 0),
  bank_name VARCHAR(100),
  bank_account VARCHAR(50),
  bank_agency VARCHAR(20),
  pix_key VARCHAR(100),
  admin_notes TEXT,
  approved_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_agencies_user_id ON agencies(user_id);
CREATE INDEX idx_agencies_cnpj ON agencies(cnpj);
CREATE INDEX idx_agencies_total_sales ON agencies(total_sales);
CREATE INDEX idx_agencies_commission_percent ON agencies(commission_percent);
