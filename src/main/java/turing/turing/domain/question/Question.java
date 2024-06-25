package turing.turing.domain.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Size(max = 300)
    @NotNull
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @Size(max = 200)
    @Column(name = "question_image", length = 200)
    private String questionImage;

    @NotNull
    @Column(name = "solve_status", nullable = false)
    private Boolean solveStatus = false;

    @NotNull
    @Column(name = "pin_status", nullable = false)
    private Boolean pinStatus = false;

}