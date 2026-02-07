CREATE TABLE passengers (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  cpf VARCHAR(14) UNIQUE NOT NULL,
  rg VARCHAR(20),
  birth_date DATE,
  address VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(2),
  zip_code VARCHAR(10),
  preferred_cabin_type VARCHAR(20) CHECK (preferred_cabin_type IN ('STANDARD', 'EXECUTIVE', 'VIP', 'SUITE')),
  total_trips INTEGER DEFAULT 0 CHECK (total_trips >= 0),
  total_spent DECIMAL(10,2) DEFAULT 0.00 CHECK (total_spent >= 0),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_passengers_user_id ON passengers(user_id);
CREATE INDEX idx_passengers_cpf ON passengers(cpf);
CREATE INDEX idx_passengers_total_trips ON passengers(total_trips);
