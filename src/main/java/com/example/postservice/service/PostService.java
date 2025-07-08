package com.example.postservice.service;

import com.example.postservice.domain.PostEntity;
import com.example.postservice.domain.PostLikeEntity;
import com.example.postservice.dto.*;
import com.example.postservice.exception.*;
import com.example.postservice.repository.PostLikeRepository;
import com.example.postservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MinioService minioService;

    public PostListResponse getPosts(Long cursorId) {

        List<PostEntity> posts = postRepository.findNextPage(cursorId, Pageable.ofSize(20));

        List<PostSummaryDto> dtoList = posts.stream().map(post -> PostSummaryDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .thumbnailUrl(post.getThumbnailUrl())
                .authorName(post.getAuthorName())
                .authorProfileImageUrl(post.getAuthorProfileImageUrl())
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

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request, String authorId) {
        // 썸네일 URL 추출 (간단한 정규표현식)
        String thumbnailUrl = extractFirstImageUrl(request.getContent());

        PostEntity post = PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnailUrl(thumbnailUrl)
                .authorId(authorId)
                .authorName("지현") // TODO: user-service → 메시지 큐로부터 사용자 정보 수신 예정
                .authorProfileImageUrl("default-image-url") // TODO: user-service → 메시지 큐로부터 사용자 정보 수신 예정
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .commentCount(0)
                .build();

        postRepository.save(post);

        return CreatePostResponse.builder()
                .postId(post.getId())
                .build();
    }

    public PostDetailResponse getPost(Long postId) {

        if (postId == null || postId <= 0) {
            throw new InvalidPostIdFormatException();
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        // TODO: comment-service에 postId를 이용하여 댓글 목록 조회 (RabbitMQ 처리)

        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthorName())
                .authorProfileImageUrl(post.getAuthorProfileImageUrl())
                .createdAt(post.getCreatedAt())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .comments(List.of()) // TODO: 댓글 목록 조회 후 설정
                .build();
    }

    @Transactional
    public UpdatePostResponse updatePost(Long postId, UpdatePostRequest request, String authorId) {

        if (postId == null || postId <= 0) {
            throw new InvalidPostIdFormatException();
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthorId().equals(authorId)) {
            throw new PostAccessDeniedException();
        }

        boolean modified = false;

        if (request.getTitle() != null) {
            if (request.getTitle().length() > 100) {
                throw new TitleTooLongException();
            }
            post.setTitle(request.getTitle());
            modified = true;
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
            post.setThumbnailUrl(extractFirstImageUrl(request.getContent()));
            modified = true;
        }

        if (!modified) {
            throw new NoChangesProvidedException();
        }

        return UpdatePostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional
    public Map<String, Object> deletePost(Long postId, String authorId) {

        if (postId == null || postId <= 0) {
            throw new InvalidPostIdFormatException();
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthorId().equals(authorId)) {
            throw new PostAccessDeniedException();
        }

        postLikeRepository.deleteByPostId(postId);

        postRepository.delete(post);

        return Map.of("postId", postId);
    }

    @Transactional
    public Map<String, Object> likePost(Long postId, String userId) {

        if (postId == null || postId <= 0) {
            throw new InvalidPostIdFormatException();
        }

        if(postRepository.existsById(postId)) {
            throw new PostNotFoundException();
        }

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new AlreadyLikedException();
        }

        PostLikeEntity like = PostLikeEntity.builder()
                .postId(postId)
                .userId(userId)
                .likedAt(LocalDateTime.now())
                .build();

        postLikeRepository.save(like);

        postRepository.incrementLikeCount(postId);

        int likeCount = postRepository.findById(postId).map(PostEntity::getLikeCount).orElse(0);

        return Map.of("postId", postId, "likeCount", likeCount);
    }

    @Transactional
    public Map<String, Object> unlikePost(Long postId, String userId) {

        if (postId == null || postId <= 0) {
            throw new InvalidPostIdFormatException();
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthorId().equals(userId)) {
            throw new PostAccessDeniedException();
        }

        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new LikeNotFoundException();
        }

        postLikeRepository.deleteByPostIdAndUserId(postId, userId);

        postRepository.decrementLikeCount(postId);

        int likeCount = postRepository.findById(postId).map(PostEntity::getLikeCount).orElse(0);

        return Map.of("postId", postId, "likeCount", likeCount);
    }

    @Transactional
    public Map<String, String> uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new NoImageProvidedException();
        }

        String contentType = file.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/jpg").contains(contentType)) {
            throw new InvalidFileTypeException();
        }

        String originalFilename = file.getOriginalFilename();
        String objectName = UUID.randomUUID() + "-" + originalFilename;

        String imageUrl = minioService.upload(file, objectName);

        return Map.of("imageUrl", imageUrl);
    }

    // 마크다운 이미지 URL 추출
    private String extractFirstImageUrl(String content) {
        if (content == null) return null;

        // 마크다운 이미지 형식: ![alt text](image_url)
        // !\\[     -> 마크다운 이미지 구문의 시작인 ![ 문자
        // [^\\]]*  -> ]가 나오기 전까지의 모든 문자 (alt 텍스트)
        // \\]      -> ] 문자
        // \\(      -> 여는 괄호 (
        // (.*?)    -> 캡처 그룹: URL을 비탐욕적으로 추출 (가장 첫 번째 항목만)
        // \\)      -> 닫는 괄호 )
        String regex = "!\\[[^\\]]*\\]\\((.*?)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(content);

        return matcher.find() ? matcher.group(1) : null;
    }

}
