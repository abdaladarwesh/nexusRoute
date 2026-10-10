package com.abdullah.nexus_route.mappers;

import com.abdullah.nexus_route.dto.response.UserResponse;
import com.abdullah.nexus_route.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
