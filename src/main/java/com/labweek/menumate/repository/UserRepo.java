package com.labweek.menumate.repository;

import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);
    //List<NewProductEntity> findByNtId(String ntId);

}
