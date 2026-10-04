SET @col_exists = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'usuarios'
      AND column_name = 'device_token'
);

SET @drop_col_sql = IF(@col_exists > 0, 'ALTER TABLE usuarios DROP COLUMN device_token;', 'SELECT 1;');

PREPARE stmt FROM @drop_col_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
