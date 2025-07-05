package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private String authorProfileImageUrl;
    private LocalDateTime createdAt;
    private int commentCount;
    private int likeCount;
    private List<CommentDto> comments; // TODO: comment-service로부터 댓글 트리 구조를 조회하는 로직 필요
}
