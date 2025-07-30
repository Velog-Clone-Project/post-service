package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAuthorInfoRequest {
    private String userId;
    private String authorName;
    private String authorProfileImageUrl;
}