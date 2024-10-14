package com.bank.service;

import com.bank.dto.BankResponse;
import com.bank.dto.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
