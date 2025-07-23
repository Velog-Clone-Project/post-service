package com.example.postservice.service;

import com.example.postservice.domain.PostEntity;
import com.example.postservice.dto.PostListResponse;
import com.example.postservice.dto.PostSummaryDto;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalPostService {

    private final PostRepository postRepository;

    public PostListResponse getPostsByUserId(String userId, Long cursorId) {

        List<PostEntity> posts = postRepository.findNextPageByUserId(userId,cursorId, Pageable.ofSize(20));

        List<PostSummaryDto> dtoList = posts.stream().map(post -> PostSummaryDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .thumbnailUrl(post.getThumbnailUrl())
                .createdAt(post.getCreatedAt())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .build()
        ).toList();

        Long nextCursorId = !posts.isEmpty() ? posts.get(posts.size() - 1).getId() : null;
        boolean hasNext = nextCursorId != null && postRepository.existsById(nextCursorId);

        return PostListResponse.builder()
                .posts(dtoList)
                .nextCursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }
}
