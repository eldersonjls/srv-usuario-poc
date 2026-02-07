CREATE TABLE boatmen (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  cpf VARCHAR(14) UNIQUE NOT NULL,
  rg VARCHAR(20),
  birth_date DATE,
  company_name VARCHAR(255) NOT NULL,
  cnpj VARCHAR(18) UNIQUE NOT NULL,
  company_address VARCHAR(255),
  company_city VARCHAR(100),
  company_state VARCHAR(2),
  company_zip_code VARCHAR(10),
  document_cpf_url VARCHAR(500),
  document_cnpj_url VARCHAR(500),
  document_address_proof_url VARCHAR(500),
  rating DECIMAL(2,1) DEFAULT 0.0 CHECK (rating >= 0.0 AND rating <= 5.0),
  total_reviews INTEGER DEFAULT 0 CHECK (total_reviews >= 0),
  total_vessels INTEGER DEFAULT 0 CHECK (total_vessels >= 0),
  total_trips INTEGER DEFAULT 0 CHECK (total_trips >= 0),
  total_revenue DECIMAL(12,2) DEFAULT 0.00 CHECK (total_revenue >= 0),
  admin_notes TEXT,
  approved_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_boatmen_user_id ON boatmen(user_id);
CREATE INDEX idx_boatmen_cpf ON boatmen(cpf);
CREATE INDEX idx_boatmen_cnpj ON boatmen(cnpj);
CREATE INDEX idx_boatmen_rating ON boatmen(rating);
CREATE INDEX idx_boatmen_total_trips ON boatmen(total_trips);
