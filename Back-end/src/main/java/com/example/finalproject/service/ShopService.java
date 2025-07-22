package com.example.finalproject.service;

import com.example.finalproject.dto.ShopDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Shop;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final AgentRepository agentRepository;

    public ShopService(ShopRepository shopRepository, AgentRepository agentRepository) {
        this.shopRepository = shopRepository;
        this.agentRepository = agentRepository;
    }

    public List<ShopDTO> getShopsByAccountId(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));
        return shopRepository.findByAgentId(agent.getId())
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ShopDTO> getShopByIdAndAccount(Integer id, Integer accountId) {
        Optional<Agent> agent = agentRepository.findByAccount_AccountId(accountId);
        return agent.flatMap(a -> shopRepository.findByIdAndAgentId(id, a.getId())).map(this::convertToDTO);
    }
    public List<ShopDTO> getAllShops() {
        return shopRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public ShopDTO createShopWithAgent(ShopDTO shopDTO, Agent agent) {
        Shop shop = new Shop(agent, shopDTO.getLocation(), shopDTO.getPhoneNumber());
        return convertToDTO(shopRepository.save(shop));
    }

    public ShopDTO updateShopByAccount(Integer id, ShopDTO shopDTO, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));

        Shop shop = shopRepository.findByIdAndAgentId(id, agent.getId())
                .orElseThrow(() -> new RuntimeException("❌ Shop does not exist or is not accessible!"));

        shop.setLocation(shopDTO.getLocation());
        shop.setPhoneNumber(shopDTO.getPhoneNumber());
        return convertToDTO(shopRepository.save(shop));
    }

    public void deleteShopByAccount(Integer id, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌Agent not found from Account ID!"));

        boolean exists = shopRepository.existsByIdAndAgentId(id, agent.getId());
        if (!exists) throw new RuntimeException("❌ No permission to delete this Shop!");

        shopRepository.deleteById(id);
    }

    private ShopDTO convertToDTO(Shop shop) {
        return new ShopDTO(shop.getId(), shop.getAgent().getId(), shop.getLocation(), shop.getPhoneNumber());
    }
    public List<ShopDTO> getShopsByAgent(Integer agentId) {
        return shopRepository.findByAgentId(agentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
