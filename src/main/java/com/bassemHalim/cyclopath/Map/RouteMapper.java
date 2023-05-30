package com.bassemHalim.cyclopath.Map;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteMapper {
    RouteMapper MAPPER = Mappers.getMapper(RouteMapper.class);

    RouteDTO toDTO(Route route);

}
