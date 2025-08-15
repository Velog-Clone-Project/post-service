package com.example.postservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    private String thumbnailUrl;

    // 작성자 정보 스냅샷
    @Column(nullable = false)
    private String userId;

    /*
     * authorName과 authorProfileImageUrl은 user-service에서 관리되고,
     * 실제 운영 환경에서는 메시지 큐(RabbitMQ)를 통해 갱신할 예정
     */
    @Column(nullable = false)
    private String authorName;
    @Column(nullable = false)
    private String authorProfileImageUrl;

    // 카운트 필드 (denormalization)
    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;
    
    @Builder.Default
    @Column(nullable = false)
    private int commentCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
