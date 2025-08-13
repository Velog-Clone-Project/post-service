package com.example.postservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSummaryListDto {

    private List<PostSummaryDto> items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long nextCursorId;
    private boolean hasNext;
}
