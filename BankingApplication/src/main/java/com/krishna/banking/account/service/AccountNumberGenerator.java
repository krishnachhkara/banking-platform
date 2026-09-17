package com.krishna.banking.account.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate(){

        StringBuilder accountNumber = new StringBuilder(12);

        accountNumber.append(
                secureRandom.nextInt(9) + 1
        );

        for(int i = 1; i < 12 ; i++) {

            accountNumber.append(
                    secureRandom.nextInt(10)
            );

        }

        return  accountNumber.toString();

    }
}
