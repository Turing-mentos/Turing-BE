package turing.turing.domain.homework;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.notebook.Notebook;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Homework extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Size(max = 100)
    @NotNull
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Size(max = 100)
    @NotNull
    @Column(name = "range_type", nullable = false, length = 100)
    private String rangeType;

    @NotNull
    @Column(name = "range_start", nullable = false)
    private Integer rangeStart;

    @NotNull
    @Column(name = "range_end", nullable = false)
    private Integer rangeEnd;

    @Size(max = 50)
    @NotNull
    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Size(max = 100)
    @Column(name = "memo", length = 100)
    private String memo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notebook_id", nullable = false)
    private Notebook notebook;

    @NotNull
    @Column(name = "is_done", nullable = false)
    @ColumnDefault("false")
    private Boolean isDone;

    @Builder
    public Homework(String category, String title, String rangeType, int rangeStart, int rangeEnd, String content, String memo, Notebook notebook) {
        super();
        this.category = category;
        this.title = title;
        this.rangeType = rangeType;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.content = content;
        this.memo = memo;
        this.notebook = notebook;
    }

    public Long update(DetailedHomeworkDto request) {
        this.category = request.getCategory();
        this.title = request.getTitle();
        this.rangeType = request.getRangeType();
        this.rangeStart = request.getRangeStart();
        this.rangeEnd = request.getRangeEnd();
        this.content = request.getContent();
        this.memo = request.getMemo();

        return request.getHomeworkId();
    }
}