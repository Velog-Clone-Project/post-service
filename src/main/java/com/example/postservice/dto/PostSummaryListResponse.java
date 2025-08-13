package com.example.postservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSummaryListResponse {
    private PostSummaryListDto posts;
}