package com.example.finalproject.service;

import com.example.finalproject.dto.AgentPackageDTO;
import com.example.finalproject.entity.Account;
import com.example.finalproject.entity.AgentPackageSubscription;
import com.example.finalproject.repository.AccountRepository;
import com.example.finalproject.repository.AgentPackageSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgentPackageService {

    private final AgentPackageSubscriptionRepository subscriptionRepository;
    private final AccountRepository accountRepository;

    public AgentPackageService(
            AgentPackageSubscriptionRepository subscriptionRepository,
            AccountRepository accountRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.accountRepository = accountRepository;
    }

    public AgentPackageDTO createSubscription(Integer accountId, String name, BigDecimal price, int duration) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Account does not exist"));

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusMonths(duration);

        AgentPackageSubscription sub = new AgentPackageSubscription(
                name, price, "unpaid", now, endDate, accountId
        );

        AgentPackageSubscription saved = subscriptionRepository.save(sub);
        return convertToDTO(saved);
    }

    public void activateAgentPackage(Integer subscriptionId) {
        AgentPackageSubscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("❌ Package not found"));

        sub.setPaymentStatus("paid");
        subscriptionRepository.save(sub);

        Account account = accountRepository.findById(sub.getAccountId())
                .orElseThrow(() -> new RuntimeException("❌ Account does not exist"));
        account.setStatus("ACTIVE");
        accountRepository.save(account);
    }

    public Map<String, Object> getRemainingTime(Integer accountId) {
        AgentPackageSubscription latestPackage = subscriptionRepository
                .findTopByAccountIdOrderByIdDesc(accountId)
                .orElseThrow(() -> new RuntimeException("❌ No subscription packages found"));

        if (!"paid".equals(latestPackage.getPaymentStatus())) {
            throw new RuntimeException("❌No subscription packages found");
        }

        LocalDate today = LocalDate.now();
        LocalDate end = latestPackage.getEndDate();

        if (end.isBefore(today)) {
            return Map.of(
                    "months", 0,
                    "days", 0,
                    "hours", 0,
                    "minutes", 0
            );
        }

        Duration duration = Duration.between(today.atStartOfDay(), end.atStartOfDay());
        long totalMinutes = duration.toMinutes();
        long months = totalMinutes / (30L * 24 * 60);
        long days = (totalMinutes % (30L * 24 * 60)) / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        Map<String, Object> result = new HashMap<>();
        result.put("months", months);
        result.put("days", days);
        result.put("hours", hours);
        result.put("minutes", minutes);

        return result;
    }

    public List<AgentPackageDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AgentPackageDTO convertToDTO(AgentPackageSubscription sub) {
        return new AgentPackageDTO(
                sub.getId(),
                sub.getName(),
                sub.getPrice(),
                sub.getPaymentStatus(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getAccountId()
        );
    }
}
