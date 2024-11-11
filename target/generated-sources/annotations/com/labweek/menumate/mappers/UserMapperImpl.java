package com.labweek.menumate.mappers;

import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-11T00:32:32+0530",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(UserEntity appuser) {
        if ( appuser == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( appuser.getId() );
        userDto.ntId( appuser.getNtId() );
        userDto.email( appuser.getEmail() );
        userDto.phoneNo( appuser.getPhoneNo() );
        userDto.userName( appuser.getUserName() );

        return userDto.build();
    }

    @Override
    public UserEntity signUpToUser(SignUpDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( userDto.getId() );
        userEntity.ntId( userDto.getNtId() );
        userEntity.userName( userDto.getUserName() );
        userEntity.email( userDto.getEmail() );
        userEntity.phoneNo( userDto.getPhoneNo() );

        return userEntity.build();
    }
}
