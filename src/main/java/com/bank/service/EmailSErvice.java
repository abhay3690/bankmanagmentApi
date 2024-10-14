package com.bank.service;

import com.bank.dto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailSErvice {
    void sendEmailAlert(EmailDetails emailDetails);
}
