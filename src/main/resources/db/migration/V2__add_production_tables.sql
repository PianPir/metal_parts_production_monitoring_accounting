-- Типы enum как CHECK-ограничения (поскольку в V1 уже используется VARCHAR + CHECK)

-- 1. WorkOrder — производственный заказ
CREATE TABLE work_orders (
                             id BIGSERIAL PRIMARY KEY,
                             order_number VARCHAR(100) NOT NULL UNIQUE,
                             material_batch_id BIGINT NOT NULL REFERENCES material_batches(id) ON DELETE RESTRICT,
                             machine_id BIGINT NOT NULL REFERENCES machines(id) ON DELETE RESTRICT,
                             planned_start TIMESTAMP(6) NOT NULL,
                             planned_end TIMESTAMP(6) NOT NULL,
                             actual_start TIMESTAMP(6),
                             actual_end TIMESTAMP(6),
                             status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'RUNNING', 'COMPLETED', 'CANCELLED')),
                             CONSTRAINT chk_planned_dates CHECK (planned_end > planned_start),
                             CONSTRAINT chk_actual_dates CHECK (
                                 (actual_start IS NULL AND actual_end IS NULL) OR
                                 (actual_start IS NOT NULL AND actual_end IS NOT NULL AND actual_end >= actual_start)
                                 )
);

-- 2. ProcessedPart — обработанная деталь
CREATE TABLE processed_parts (
                                 id BIGSERIAL PRIMARY KEY,
                                 part_number VARCHAR(100) NOT NULL,
                                 weight_after_processing_kg NUMERIC(19,4) NOT NULL CHECK (weight_after_processing_kg >= 0),
                                 defect_reason TEXT,
                                 work_order_id BIGINT NOT NULL REFERENCES work_orders(id) ON DELETE CASCADE,
                                 created_at TIMESTAMP(6) NOT NULL DEFAULT NOW()
);

-- 3. MachineStatusLog — лог состояния станка
CREATE TABLE machine_status_logs (
                                     id BIGSERIAL PRIMARY KEY,
                                     machine_id BIGINT NOT NULL REFERENCES machines(id) ON DELETE CASCADE,
                                     timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     status VARCHAR(50) NOT NULL CHECK (status IN ('IDLE', 'RUNNING', 'ERROR', 'MAINTENANCE')),
                                     temperature_celsius DOUBLE PRECISION,
                                     vibration_level DOUBLE PRECISION
);

-- Индексы для производительности
CREATE INDEX idx_work_orders_order_number ON work_orders(order_number);
CREATE INDEX idx_work_orders_status ON work_orders(status);
CREATE INDEX idx_work_orders_machine_id ON work_orders(machine_id);
CREATE INDEX idx_processed_parts_work_order_id ON processed_parts(work_order_id);
CREATE INDEX idx_machine_status_logs_machine_id ON machine_status_logs(machine_id);
CREATE INDEX idx_machine_status_logs_timestamp ON machine_status_logs(timestamp DESC);