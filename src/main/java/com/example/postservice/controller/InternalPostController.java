package com.example.postservice.controller;

import com.example.common.dto.ApiResponse;
import com.example.postservice.dto.PostListResponse;
import com.example.postservice.exception.InvalidCursorIdException;
import com.example.postservice.service.InternalPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/posts/")
public class InternalPostController {

    private final InternalPostService internalPostService;

    @GetMapping
    public ResponseEntity<PostListResponse> getPostsByUserId(
            @RequestParam String userId,
            @RequestParam(required = false) Long cursorId) {

        if (cursorId != null && cursorId <= 0) {
            throw new InvalidCursorIdException();
        }

        PostListResponse response = internalPostService.getPostsByUserId(userId, cursorId);

        return ResponseEntity.ok(response);
    }
}
