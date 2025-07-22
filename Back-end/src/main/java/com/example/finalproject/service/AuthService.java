package com.example.finalproject.service;

import com.example.finalproject.dto.LoginRequest;
import com.example.finalproject.dto.RegisterRequest;
import com.example.finalproject.entity.Account;
import com.example.finalproject.entity.Account.Role;
import com.example.finalproject.repository.AccountRepository;
import com.example.finalproject.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtils jwtUtils;


    public String register(RegisterRequest request) {
        // Kiểm tra username đã tồn tại
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            return "❌ Username already exists!";
        }


        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "❌ Invalid role! Must be CUSTOMER, AGENT, or ADMIN.";
        }


        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setRole(role);
        account.setSubmittedAt(LocalDateTime.now());


        account.setStatus(role == Role.AGENT ? "INACTIVE" : "ACTIVE");

        Account savedAccount = accountRepository.save(account);


        return jwtUtils.generateToken(
                savedAccount.getAccountId(),
                savedAccount.getUsername(),
                savedAccount.getRole().name(),
                savedAccount.getStatus()
        );
    }


    public String login(LoginRequest request) {
        Optional<Account> userOptional = accountRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            System.out.println("❌ User not found: " + request.getUsername());
            throw new RuntimeException("❌ User not found!");
        }

        Account account = userOptional.get();

        if (!request.getPassword().equals(account.getPassword())) {
            System.out.println("❌ Password does not match!");
            throw new RuntimeException("❌ Invalid credentials!");
        }

        System.out.println("✅ Login successful for user: " + account.getUsername());

        return jwtUtils.generateToken(
                account.getAccountId(),
                account.getUsername(),
                account.getRole().name(),
                account.getStatus()
        );
    }


    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    public String disableAccount(Integer accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            return "❌ Account not found!";
        }

        Account account = optionalAccount.get();
        account.setStatus("INACTIVE");
        accountRepository.save(account);
        return "✅ Account disabled successfully!";
    }


    public String deleteAccount(Integer accountId) {
        if (!accountRepository.existsById(accountId)) {
            return "❌ Account not found!";
        }

        accountRepository.deleteById(accountId);
        return "✅ Account deleted successfully!";
    }
}
