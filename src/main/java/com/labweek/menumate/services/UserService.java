package com.labweek.menumate.services;

import com.labweek.menumate.dto.CredentialsDto;
import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.entity.UserEntity;
import com.labweek.menumate.exceptions.AppException;
import com.labweek.menumate.mappers.UserMapper;
import com.labweek.menumate.repository.ProductRepository;
import com.labweek.menumate.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service


@Transactional
public class UserService {

    @Autowired
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;


    @Transactional
    public UserDto findByEmail(String email){

        UserEntity appuser = userRepo.findByEmail(email)
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(appuser);

    }

    @Transactional
    public UserDto login(CredentialsDto credentialsDto){

        UserEntity appuser = userRepo.findByEmail(credentialsDto.getEmail())
                .orElseThrow(()-> new AppException("unknown user",HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(credentialsDto.getPassword(),appuser.getPassword())){
            return userMapper.toUserDto(appuser);
        }
        throw new AppException("INCORRECT PASSWORD!",HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public UserDto register(SignUpDto signUpUserDto){

        Optional<UserEntity> optionalUser = userRepo.findByEmail(signUpUserDto.getEmail());

        if(optionalUser.isPresent()){
            throw new AppException("User already exists!", HttpStatus.BAD_REQUEST);
        }

        UserEntity appUser = userMapper.signUpToUser(signUpUserDto);
        appUser.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));;
        UserEntity savedUser = userRepo.save(appUser);

        return userMapper.toUserDto(appUser);
    }

    // Fetch products for a specific user (based on the username)
    @Transactional
    public List<NewProductDto> getProductsByUser(String ntId) {
        // Fetch products from the repository using the user's username
        List<NewProductEntity> products = productRepository.findByNtId(ntId);
        System.out.println(products);
        // Convert the entity list to DTOs and return
        return products.stream()
                .map(product -> NewProductDto.builder()
                        .ntId(product.getNtId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .purchaseDate(product.getPurchaseDate())
                        .dateListed(String.valueOf(product.getDateListed()))
                        .price(product.getPrice())
                        .category(product.getCategory())
                        .image(product.getImage()) // Assuming the image is stored as bytes or base64
                        .build())
                .collect(Collectors.toList());
    }



}
