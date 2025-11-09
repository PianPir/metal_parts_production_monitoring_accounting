package com.metal_parts_production_monitoring_accounting.mapper;

import com.metal_parts_production_monitoring_accounting.model.MaterialBatch;
import com.metal_parts_production_monitoring_accounting.payload.request.MaterialBatchRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialBatchMapper {
    MaterialBatch toMaterialBatch(MaterialBatchRequest materialBatchRequest);
    MaterialBatchRequest toMaterialBatchRequest(MaterialBatch materialBatch);
}
