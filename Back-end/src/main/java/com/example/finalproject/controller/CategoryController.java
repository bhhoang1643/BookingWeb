package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.CategoryDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    private final JwtUtils jwtUtils;
    private final AgentRepository agentRepository;

    public CategoryController(CategoryService categoryService, JwtUtils jwtUtils, AgentRepository agentRepository) {
        this.categoryService = categoryService;
        this.jwtUtils = jwtUtils;
        this.agentRepository = agentRepository;
    }


    @GetMapping("/my")
    public ResponseEntity<List<CategoryDTO>> getMyCategories(@RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(categoryService.getCategoriesByAccountId(accountId));
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDTO dto,
                                    @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.status(201).body(categoryService.createCategory(dto, accountId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody CategoryDTO dto,
                                    @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(categoryService.updateCategoryByAccount(id, dto, accountId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id,
                                         @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        categoryService.deleteCategoryByAccount(id, accountId);
        return ResponseEntity.ok("✅ Category deleted");
    }
}
