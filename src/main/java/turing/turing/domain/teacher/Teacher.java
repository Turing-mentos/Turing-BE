package turing.turing.domain.teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "university", nullable = false, length = 100)
    private String university;

    @Size(max = 30)
    @NotNull
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Size(max = 10)
    @NotNull
    @Column(name = "main_subject", nullable = false, length = 10)
    private String mainSubject;

    @Size(max = 255)
    @Column(name = "department")
    private String department;

    @Size(max = 255)
    @Column(name = "student_number")
    private String studentNumber;

    @Size(max = 255)
    @Column(name = "nickname")
    private String nickname;

}