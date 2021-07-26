package ${package}.service.mapstruct;

import com.wzx.digitalalweb.base.BaseMapper;
import ${package}.entry.${className};
import ${package}.service.dto.${className}Dto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ${author}
* @date ${date}
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Mapper extends BaseMapper<${className}Dto, ${className}> {

}
