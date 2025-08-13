package com.example.postservice.service;

import com.example.postservice.domain.PostEntity;
import com.example.postservice.dto.PostSummaryListDto;
import com.example.postservice.dto.PostSummaryDto;
import com.example.postservice.event.UpdateAuthorInfoEvent;
import com.example.postservice.repository.PostLikeRepository;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalPostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    private static final int PAGE_SIZE = 20;


    public PostSummaryListDto getPostsByUserId(String userId, Long cursorId) {

        List<PostEntity> entities = postRepository.findNextPageByUserId(userId, cursorId, Pageable.ofSize(PAGE_SIZE + 1));

        boolean hasNext = entities.size() > PAGE_SIZE;
        if (hasNext) {
            entities = entities.subList(0, PAGE_SIZE);
        }

        List<PostSummaryDto> dtoList = entities.stream()
                .map(post -> PostSummaryDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .thumbnailUrl(post.getThumbnailUrl())
                        .createdAt(post.getCreatedAt())
                        .commentCount(post.getCommentCount())
                        .likeCount(post.getLikeCount())
                        .build()
                ).toList();

        Long nextCursorId = hasNext ? entities.get(entities.size() - 1).getId() : null;

        return PostSummaryListDto.builder()
                .items(dtoList)
                .nextCursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Transactional
    public void updateAuthorInfo(UpdateAuthorInfoEvent request) {

        postRepository.updateAuthorInfoByUserId(
                request.getUserId(),
                request.getAuthorName(),
                request.getAuthorProfileImageUrl()
        );
    }

    public PostSummaryListDto getLikedPostsByUserId(String userId, Long cursorId) {

        List<PostEntity> entities = postRepository.findLikedPostsByUserId(userId, cursorId, Pageable.ofSize(PAGE_SIZE + 1));

        boolean hasNext = entities.size() > PAGE_SIZE;
        if (hasNext) {
            entities = entities.subList(0, PAGE_SIZE);
        }

        List<PostSummaryDto> dtoList = entities.stream()
                .map(post -> PostSummaryDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .thumbnailUrl(post.getThumbnailUrl())
                        .createdAt(post.getCreatedAt())
                        .commentCount(post.getCommentCount())
                        .likeCount(post.getLikeCount())
                        .build()
                ).toList();

        Long nextCursorId = hasNext ? entities.get(entities.size() - 1).getId() : null;

        return PostSummaryListDto.builder()
                .items(dtoList)
                .nextCursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Transactional
    public void deleteAllPostsByUserId(String userId) {
        postRepository.deleteByUserId(userId);
        postLikeRepository.deleteByUserId(userId);
    }

    public boolean postExists(Long postId) {
        return postRepository.existsById(postId);
    }

}
