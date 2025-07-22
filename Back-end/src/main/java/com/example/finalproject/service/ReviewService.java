package com.example.finalproject.service;

import com.example.finalproject.dto.ReviewDTO;
import com.example.finalproject.entity.*;
import com.example.finalproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final ShopRepository shopRepository;
    private final HairstylistRepository hairstylistRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         CustomerRepository customerRepository,
                         AgentRepository agentRepository,
                         ShopRepository shopRepository,
                         HairstylistRepository hairstylistRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
        this.shopRepository = shopRepository;
        this.hairstylistRepository = hairstylistRepository;
    }

    public List<ReviewDTO> getAllReviews() {
        List<Review> all = reviewRepository.findAll();
        System.out.println("📌 Reviews in DB: " + all.size());
        return all.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByShopId(Integer shopId) {
        return reviewRepository.findByShop_Id(shopId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByAgentId(Integer agentId) {
        return reviewRepository.findByAgent_Id(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByHairstylistId(Integer hairstylistId) {
        return reviewRepository.findByHairstylist_Id(hairstylistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Review does not exist!"));
        return convertToDTO(review);
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Customer customer = customerRepository.findById(reviewDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("❌ Customer does not exist!"));

        Agent agent = agentRepository.findById(reviewDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        Shop shop = shopRepository.findById(reviewDTO.getShopId())
                .orElseThrow(() -> new RuntimeException("❌ Shop does not exist!"));

        Hairstylist stylist = null;
        if (reviewDTO.getHairstylistId() != null) {
            stylist = hairstylistRepository.findById(reviewDTO.getHairstylistId())
                    .orElseThrow(() -> new RuntimeException("❌ Stylist does not exist!"));
        }

        Review review = new Review(customer, agent, shop, stylist,
                reviewDTO.getRating(), reviewDTO.getComment(), LocalDateTime.now());

        return convertToDTO(reviewRepository.save(review));
    }

    @Transactional
    public ReviewDTO updateReview(Integer id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Review does not exist!"));

        if (reviewDTO.getRating() != null) {
            review.setRating(reviewDTO.getRating());
        }
        if (reviewDTO.getComment() != null) {
            review.setComment(reviewDTO.getComment());
        }

        return convertToDTO(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("❌ Review does not exist!");
        }
        reviewRepository.deleteById(id);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO(
                review.getReviewId(),
                review.getCustomer().getId(),
                review.getAgent().getId(),
                review.getShop().getId(),
                review.getHairstylist() != null ? review.getHairstylist().getId() : null,
                review.getRating(),
                review.getComment(),
                review.getTimestamp()
        );

        // Thêm thông tin hiển thị
        dto.setAgentName(review.getAgent().getAgentName());
        dto.setShopName(review.getShop().getLocation());
        dto.setHairstylistName(review.getHairstylist() != null ? review.getHairstylist().getName() : "unknown");

        // 🆕 Thêm thông tin customer
        dto.setCustomerUsername(review.getCustomer().getAccount().getUsername());
        dto.setCustomerImage(review.getCustomer().getImageFile());

        return dto;
    }
}
