ALTER TABLE categories ADD COLUMN transaction_type
    VARCHAR(20) NOT NULL default 'EXPENSE';