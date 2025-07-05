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
public class CommentDto {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorProfileImageUrl;
    private List<CommentDto> replies;
}
