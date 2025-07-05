package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSummaryDto {

    private Long postId;
    private String title;
    private String thumbnailUrl;
    private String authorName;
    private String authorProfileImageUrl;
    private LocalDateTime createdAt;
    private int commentCount;
    private int likeCount;
}