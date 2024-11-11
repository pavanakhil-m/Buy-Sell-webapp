package com.labweek.menumate.mappers;

import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(UserEntity appuser);

    @Mapping(target = "password", ignore = true)
    UserEntity signUpToUser(SignUpDto userDto);
}
