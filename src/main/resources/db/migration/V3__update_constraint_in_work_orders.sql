ALTER TABLE work_orders DROP CONSTRAINT chk_actual_dates;
ALTER TABLE work_orders ADD CONSTRAINT chk_actual_dates CHECK (
    (actual_start IS NULL AND actual_end IS NULL) OR
    (actual_start IS NOT NULL AND actual_end IS NULL) OR
    (actual_start IS NOT NULL AND actual_end IS NOT NULL AND actual_end >= actual_start)
    );