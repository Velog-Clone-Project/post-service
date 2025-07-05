package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListResponse {

    private List<PostSummaryDto> posts;
    private Long nextCursorId;
    private boolean hasNextPage;
}
