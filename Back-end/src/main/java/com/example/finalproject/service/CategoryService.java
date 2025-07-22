package com.example.finalproject.service;

import com.example.finalproject.dto.CategoryDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Category;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AgentRepository agentRepository;

    public CategoryService(CategoryRepository categoryRepository, AgentRepository agentRepository) {
        this.categoryRepository = categoryRepository;
        this.agentRepository = agentRepository;
    }

    public List<CategoryDTO> getCategoriesByAccountId(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌Agent not found from Account ID!"));
        return categoryRepository.findByAgentId(agent.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CategoryDTO createCategory(CategoryDTO dto, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist from Account ID!"));

        boolean exists = categoryRepository.existsByNameAndAgentId(dto.getName(), agent.getId());
        if (exists) {
            throw new RuntimeException("❌ Category already exists for this Agent!");
        }

        Category category = new Category(dto.getName(), agent);
        return toDTO(categoryRepository.save(category));
    }


    public CategoryDTO updateCategoryByAccount(Integer id, CategoryDTO dto, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌Agent not found from Account ID!"));

        Category category = categoryRepository.findById(id)
                .filter(c -> c.getAgent().getId().equals(agent.getId()))
                .orElseThrow(() -> new RuntimeException("❌  Category does not exist or does not belong to this Agent!"));

        if (dto.getName() != null) category.setName(dto.getName());
        return toDTO(categoryRepository.save(category));
    }

    public void deleteCategoryByAccount(Integer id, Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));

        Category category = categoryRepository.findById(id)
                .filter(c -> c.getAgent().getId().equals(agent.getId()))
                .orElseThrow(() -> new RuntimeException("❌ No permission to delete this Category!"));

        categoryRepository.deleteById(id);
    }

    private CategoryDTO toDTO(Category c) {
        return new CategoryDTO(c.getId(), c.getName(), c.getAgent().getId());
    }
}
