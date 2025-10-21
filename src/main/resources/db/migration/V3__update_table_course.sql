ALTER TABLE curso
ALTER COLUMN miniatura DROP NOT NULL;

UPDATE curso
SET miniatura = NULL;