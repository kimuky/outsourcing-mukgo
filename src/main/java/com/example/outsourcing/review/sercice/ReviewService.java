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
import com.example.outsourcing.status.OrderStep;
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

    /**
     * 리뷰 생성
     * @param user - 로그인 유저
     * @param orderId - 주문 id
     * @param comment - 코멘트
     * @param rating - 별점
     * @return 리뷰 아이디, 코멘트, 별점
     */
    public ReviewResponseDto postReview(User user, Long orderId, String comment, int rating) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 본인이 주문한 게 아닐때 예외
        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ORDER);
        }

        // 배달완료가 아닐때 예외
        if (!order.getStep().equals(OrderStep.DELIVERING_COMPLETED)) {
            throw new CustomException(ErrorCode.NOT_DELIVERING_COMPLETED);
        }

        Review review = new Review(user, order, comment, rating);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .id(savedReview.getId())
                .comment(comment)
                .rating(rating)
                .build();
    }

    // 리뷰 조회
    public List<ReviewResponseDto> getAllReviews(User user, Long storeId, Integer minRating, Integer maxRating) {

        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        List<Review> findReviews = reviewRepository.findByStore(findStore, user, minRating, maxRating);;

        // 여기부터 리뷰 response dto 만드는 부분
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
