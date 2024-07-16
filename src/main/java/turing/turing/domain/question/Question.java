package turing.turing.domain.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.comment.Comment;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.ArrayList;
import java.util.List;

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

    @Size(max = 10)
    @NotNull
    @Column(name = "category", nullable = false, length = 10)
    private String category;

    @Size(max = 300)
    @NotNull
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @NotNull
    @Size(max = 100)
    @Column(name = "importance", nullable = false, length = 100)
    private String importance;

    @Size(max = 200)
    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @NotNull
    @Column(name = "solve_status", nullable = false)
    private Boolean solveStatus = false;

    @NotNull
    @Column(name = "pin_status", nullable = false)
    private Boolean pinStatus = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;
  
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Question(String title, String category, String content, String importance, String imageUrl, StudyRoom studyRoom) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.importance = importance;
        this.imageUrl = imageUrl;
        this.studyRoom = studyRoom;
    }

    public void updateQuestion(String title, String category, String content, String importance, String imageUrl){
        this.title = title;
        this.category = category;
        this.content = content;
        this.importance = importance;
        this.imageUrl = imageUrl;
    }

    public void switchPinStatus(){
        this.pinStatus = !this.pinStatus;
    }

    public void switchSolveStatus(){
        this.solveStatus= !this.solveStatus;
    }

}