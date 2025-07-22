package com.example.finalproject.service;

import com.example.finalproject.dto.HairstylistDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Hairstylist;
import com.example.finalproject.entity.Shop;
import com.example.finalproject.repository.HairstylistRepository;
import com.example.finalproject.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.finalproject.repository.AgentRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HairstylistService {

    private final HairstylistRepository hairstylistRepository;
    private final ShopRepository shopRepository;
    private final AgentRepository agentRepository;
    private final ImgurService imgurService;

    public HairstylistService(HairstylistRepository hairstylistRepository,
                              ShopRepository shopRepository,
                              AgentRepository agentRepository,
                              ImgurService imgurService) {
        this.hairstylistRepository = hairstylistRepository;
        this.shopRepository = shopRepository;
        this.agentRepository = agentRepository;
        this.imgurService = imgurService;
    }

    public List<HairstylistDTO> getMyHairstylists(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));
        return shopRepository.findByAgentId(agent.getId()).stream()
                .flatMap(shop -> hairstylistRepository.findByShopId(shop.getId()).stream())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<HairstylistDTO> getByShopId(Integer shopId) {
        return hairstylistRepository.findByShopId(shopId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public HairstylistDTO createHairstylistSecure(HairstylistDTO dto, MultipartFile file, Integer accountId) throws IOException {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        Shop shop = shopRepository.findById(dto.getShopId())
                .filter(s -> s.getAgent().getId().equals(agent.getId()))
                .orElseThrow(() -> new RuntimeException("❌ Shop not belong to this agent!"));

        String imageUrl = (file != null && !file.isEmpty()) ? imgurService.uploadToImgur(file) : null;

        Hairstylist hairstylist = new Hairstylist();
        hairstylist.setShop(shop);
        hairstylist.setName(dto.getName());
        hairstylist.setExperience(dto.getExperience());
        hairstylist.setSpecialty(dto.getSpecialty());
        hairstylist.setImage(imageUrl);

        return convertToDTO(hairstylistRepository.save(hairstylist));
    }

    public HairstylistDTO updateHairstylistSecure(Integer id, HairstylistDTO dto, MultipartFile file, Integer accountId) throws IOException {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        Hairstylist hairstylist = hairstylistRepository.findById(id)
                .filter(h -> h.getShop().getAgent().getId().equals(agent.getId()))
                .orElseThrow(() -> new RuntimeException("❌ No permission to edit this hairstylist!"));

        if (dto.getName() != null) hairstylist.setName(dto.getName());
        if (dto.getExperience() != null) hairstylist.setExperience(dto.getExperience());
        if (dto.getSpecialty() != null) hairstylist.setSpecialty(dto.getSpecialty());
        if (file != null && !file.isEmpty()) {
            hairstylist.setImage(imgurService.uploadToImgur(file));
        }

        return convertToDTO(hairstylistRepository.save(hairstylist));
    }
    @Transactional
    public void deleteHairstylistSecure(Integer id, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        Hairstylist hairstylist = hairstylistRepository.findById(id)
                .filter(h -> h.getShop().getAgent().getId().equals(agent.getId()))
                .orElseThrow(() -> new RuntimeException("❌ No permission to edit this hairstylist!"));

        hairstylistRepository.deleteById(id);
    }

    private HairstylistDTO convertToDTO(Hairstylist h) {
        return new HairstylistDTO(
                h.getId(), h.getShop().getId(), h.getName(),
                h.getExperience(), h.getSpecialty(), h.getImage(),  h.getShop().getLocation(),
                h.getShop().getAgent().getAgentName());
    }
    public List<HairstylistDTO> getByShopAndAccount(Integer shopId, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("❌ Shop does not exist!"));

        if (!shop.getAgent().getId().equals(agent.getId())) {
            throw new RuntimeException("❌ Shop does not belong to this agent!");
        }

        List<Hairstylist> hairstylists = hairstylistRepository.findByShopId(shopId);
        return hairstylists.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
