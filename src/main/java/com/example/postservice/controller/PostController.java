package com.example.postservice.controller;

import com.example.common.dto.ApiResponse;
import com.example.postservice.dto.*;
import com.example.postservice.exception.InvalidCursorIdException;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<PostListResponse>> getPosts(@RequestParam(required = false) Long cursorId) {

        // cursorId는 optional로 받게되는데, 이 상태에서 cursorId가 <= 0만 검사하면
        // cursorId = null인 경우 NullPointerException이 발생하기 때문에
        // curorId != null 인 경우에만 유효한 cursorId 인지 검사한다
        if (cursorId != null && cursorId <= 0) {
            throw new InvalidCursorIdException();
        }

        PostListResponse response = postService.getPosts(cursorId);

        return ResponseEntity
                .ok(new ApiResponse<>("Posts retrieved successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request,
            @RequestHeader("X-User-Id") String userId) {

        CreatePostResponse response = postService.createPost(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Post created successfully", response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long postId) {

        PostDetailResponse response = postService.getPost(postId);

        return ResponseEntity
                .ok(new ApiResponse<>("Post retrieved", response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request,
            @RequestHeader("X-User-Id") String userId) {

        UpdatePostResponse response = postService.updatePost(postId, request, userId);

        return ResponseEntity
                .ok(new ApiResponse<>("Post updated successfully", response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Map>> deletePost(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") String userId) {

        Map<String, Object> response = postService.deletePost(postId, userId);

        return ResponseEntity
                .ok(new ApiResponse<>("Post deleted successfully", response));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Map>> likePost(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") String userId) {

        Map<String, Object> response = postService.likePost(postId, userId);

        return ResponseEntity
                .ok(new ApiResponse<>("Post liked", response));
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Map>> unlikePost(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") String userId) {

        Map<String, Object> response = postService.unlikePost(postId, userId);

        return ResponseEntity
                .ok(new ApiResponse<>("Post unliked", response));
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<Map>> uploadImage(@RequestPart("image") MultipartFile image) {

        Map<String, String> response = postService.uploadImage(image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Image uploaded successfully", response));
    }

}
