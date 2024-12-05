package com.example.outsourcing.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewRequestDto {

    private String comment;

    private Integer rating;
}
