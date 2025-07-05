package com.example.postservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
// 복합 유니크 제약 조건
// post_id와 user_id의 조합이 유일해야 함
// 즉, 하나의 사용자는 같은 게시글에 한 번만 좋아요를 누를 수 있음
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime likedAt;

//    public PostLikeEntity(PostEntity post, String userId) {
//        this.post = post;
//        this.userId = userId;
//        this.likedAt = LocalDateTime.now();
//    }
}
