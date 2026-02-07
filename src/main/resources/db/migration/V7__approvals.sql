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
