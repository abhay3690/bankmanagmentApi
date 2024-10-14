package com.bank.service.impl;

import com.bank.dto.AccountInfo;
import com.bank.dto.BankResponse;
import com.bank.dto.EmailDetails;
import com.bank.dto.UserRequest;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.service.EmailSErvice;
import com.bank.service.UserService;
import com.bank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailSErvice emailSErvice;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CDOE) // Fixed typo here
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);// Corrected variable name

        //sender
        EmailDetails emailDetails =EmailDetails.builder()
                .recupient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation! Your account has been successfully created \nYour Account Details : \n"+ " Account Name : "+savedUser.getFirstName()+" "+ savedUser.getOtherName()+" "+savedUser.getLastName()+"\n Account Number : "+savedUser.getAccountNumber())
                .build();
        emailSErvice.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+ " " +savedUser.getLastName()+" " +savedUser.getOtherName())// Corrected here
                        .build())
                .build();
    }
}
