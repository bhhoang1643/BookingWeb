package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.ProductDTO;
import com.example.finalproject.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;
    private final JwtUtils jwtUtils;

    public ProductController(ProductService productService, JwtUtils jwtUtils) {
        this.productService = productService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProductDTO>> getMyProducts(@RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(productService.getProductsByAccountId(accountId));
    }

    @GetMapping("/by-agent/{agentId}")
    public ResponseEntity<List<ProductDTO>> getProductsByAgent(@PathVariable Integer agentId) {
        return ResponseEntity.ok(productService.getProductsByAgentId(agentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Integer id,
                                              @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return productService.getProductByIdAndAccount(id, accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ProductDTO> getProductPublic(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        ProductDTO dto = new ProductDTO();
        dto.setName(name);
        dto.setPrice(price);
        dto.setCategoryId(categoryId);
        dto.setDescription(description);
        return ResponseEntity.status(201).body(productService.createProduct(dto, file, accountId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("description") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        ProductDTO dto = new ProductDTO();
        dto.setName(name);
        dto.setPrice(price);
        dto.setCategoryId(categoryId);
        dto.setDescription(description);
        return ResponseEntity.ok(productService.updateProduct(id, dto, file, accountId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id,
                                                @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        productService.deleteProductByAccount(id, accountId);
        return ResponseEntity.ok("✅ Đã xóa sản phẩm thành công!");
    }

    @GetMapping("/by-agent/{agentId}/grouped")
    public ResponseEntity<Map<String, List<ProductDTO>>> getProductsGroupedByCategory(@PathVariable Integer agentId) {
        Map<String, List<ProductDTO>> grouped = productService.getProductsGroupedByCategory(agentId);
        return ResponseEntity.ok(grouped);
    }
}
