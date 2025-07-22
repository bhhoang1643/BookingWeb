package com.example.finalproject.service;

import com.example.finalproject.dto.ProductDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Category;
import com.example.finalproject.entity.Product;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AgentRepository agentRepository;
    private final ImgurService imgurService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, AgentRepository agentRepository, ImgurService imgurService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.agentRepository = agentRepository;
        this.imgurService = imgurService;
    }

    private Agent getAgentByAccountId(Integer accountId) {
        return agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));
    }

    public List<ProductDTO> getProductsByAccountId(Integer accountId) {
        Agent agent = getAgentByAccountId(accountId);
        return productRepository.findByAgentId(agent.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductByIdAndAccount(Integer id, Integer accountId) {
        Agent agent = getAgentByAccountId(accountId);
        return productRepository.findByIdAndAgentId(id, agent.getId())
                .map(this::convertToDTO);
    }

    public Optional<ProductDTO> getProductById(Integer id) {
        return productRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ProductDTO> getProductsByAgentId(Integer agentId) {
        return productRepository.findByAgentId(agentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO createProduct(ProductDTO dto, MultipartFile file, Integer accountId) throws IOException {
        Agent agent = getAgentByAccountId(accountId);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("❌ Category not found!"));

        String imageUrl = (file != null && !file.isEmpty()) ? imgurService.uploadToImgur(file) : null;

        Product product = new Product(imageUrl, dto.getName(), dto.getPrice(), category, agent);
        product.setDescription(dto.getDescription());

        return convertToDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Integer id, ProductDTO dto, MultipartFile file, Integer accountId) throws IOException {
        Agent agent = getAgentByAccountId(accountId);

        Product product = productRepository.findByIdAndAgentId(id, agent.getId())
                .orElseThrow(() -> new RuntimeException("❌ Product not found or not authorized by Agent!"));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("❌ Category not found!"));
            product.setCategory(category);
        }
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (file != null && !file.isEmpty()) {
            product.setImage(imgurService.uploadToImgur(file));
        }

        return convertToDTO(productRepository.save(product));
    }

    public void deleteProductByAccount(Integer id, Integer accountId) {
        Agent agent = getAgentByAccountId(accountId);
        Product product = productRepository.findByIdAndAgentId(id, agent.getId())
                .orElseThrow(() -> new RuntimeException("❌ Product not found or not under Agent's management!"));
        productRepository.deleteById(product.getId());
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getImage(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getAgent().getId(),
                product.getDescription()
        );
    }

    public Map<String, List<ProductDTO>> getProductsGroupedByCategory(Integer agentId) {
        return productRepository.findByAgentId(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.groupingBy(dto -> {
                    Optional<Category> categoryOpt = categoryRepository.findById(dto.getCategoryId());
                    return categoryOpt.map(Category::getName).orElse("Unknown");
                }));
    }
}