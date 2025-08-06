package com.example.postservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAuthorInfoEvent {
    private String userId;
    private String authorName;
    private String authorProfileImageUrl;
}