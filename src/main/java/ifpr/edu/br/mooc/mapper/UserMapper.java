package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.user.CreateUserRequest;
import ifpr.edu.br.mooc.dto.user.UserResponse;
import ifpr.edu.br.mooc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true) // Password will be encoded in service
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);
}