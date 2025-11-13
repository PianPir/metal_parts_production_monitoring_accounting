package com.metal_parts_production_monitoring_accounting.mapper;



import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import com.metal_parts_production_monitoring_accounting.payload.response.MachineMonitoringResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MachineStatusLogMapper {

    MachineMonitoringResponse toMachineStatusLogResponse(MachineStatusLog log);

}