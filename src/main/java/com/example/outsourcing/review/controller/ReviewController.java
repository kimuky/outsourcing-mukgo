package com.example.outsourcing.review.controller;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.review.dto.PostReviewRequestDto;
import com.example.outsourcing.review.dto.ReviewResponseDto;
import com.example.outsourcing.review.sercice.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReviewController {
    private ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> postReview(
            @PathVariable Long orderId,
            @RequestBody PostReviewRequestDto postReviewRequestDto,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("user");

        ReviewResponseDto reviewResponseDto = reviewService.postReview(
                user,
                orderId,
                postReviewRequestDto.getComment(),
                postReviewRequestDto.getRating()
        );

        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDto);
    }

    // 리뷰 조회
    @GetMapping("/store/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long storeId) {

        List<ReviewResponseDto> allReviews = reviewService.getAllReviews(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(allReviews);
    }
}
