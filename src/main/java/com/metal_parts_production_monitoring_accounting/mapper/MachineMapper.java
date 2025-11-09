package com.metal_parts_production_monitoring_accounting.mapper;

import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.payload.request.MachineRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MachineMapper {
    MachineRequest toMachineRequest(Machine machine);
    Machine toEntity(MachineRequest dto);
}
