package dev.cj.temple.converter;

import dev.cj.temple.domain.Role;
import dev.cj.temple.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleConverter {
    RoleVO toRoleVO(Role role);
}
