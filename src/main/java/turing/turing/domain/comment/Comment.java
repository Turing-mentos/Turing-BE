package turing.turing.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.question.Question;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "role", length = 10, nullable = false)
    private String role;  // 추후 Enum으로 변경

    @NotNull
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Size(max = 200)
    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @NotNull
    @Size(max = 300)
    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Builder
    public Comment(String role, Long memberId, String content, String imageUrl, Question question) {
        this.role = role;
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.content = content;
        this.question = question;
    }
}