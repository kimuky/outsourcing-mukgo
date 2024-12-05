package com.example.outsourcing.review.sercice;

import com.example.outsourcing.entity.Orders;
import com.example.outsourcing.entity.Review;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.review.dto.ReviewResponseDto;
import com.example.outsourcing.review.repository.ReviewRepository;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;
    private OrderRepository orderRepository;
    private StoreRepository storeRepository;

    // 리뷰 생성
    public ReviewResponseDto postReview(User user, Long orderId, String comment, int rating) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Review review = new Review(user, order, comment, rating);

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .id(savedReview.getId())
                .comment(comment)
                .rating(rating)
                .build();
    }

    // 리뷰 조회
    public List<ReviewResponseDto> getAllReviews(Long storeId) {

        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        List<Review> findReviews = reviewRepository.findByStore(findStore);
        List<ReviewResponseDto> reviews = new ArrayList<>();
        for (Review review : findReviews) {
            reviews.add(
                    ReviewResponseDto.builder()
                        .id(review.getId())
                        .comment(review.getComment())
                        .rating(review.getRating())
                        .build()
            );
        }

        return reviews;
    }
}
