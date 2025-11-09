package com.metal_parts_production_monitoring_accounting.mapper;

import com.metal_parts_production_monitoring_accounting.model.WorkOrder;
import com.metal_parts_production_monitoring_accounting.payload.request.WorkOrderRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.WorkOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MachineMapper.class, MaterialBatchMapper.class})
public interface WorkOrderMapper {

    @Mapping(source = "materialBatch.alloyType", target = "alloyType")
    @Mapping(source = "machine.name", target = "machineName")
    @Mapping(target = "status", expression = "java(workOrder.getStatus().name())")
    WorkOrderResponse toResponse(WorkOrder workOrder);

    // Для создания из запроса — будем использовать отдельный метод в сервисе,
    // потому что нужно подгрузить связанные сущности
}
