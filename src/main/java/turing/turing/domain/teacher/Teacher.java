package turing.turing.domain.teacher;

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
import turing.turing.domain.Member;
import turing.turing.domain.Provider;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher extends Member {

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
    @Column(name = "university", length = 100)
    private String university;

    @Size(max = 30)
    @NotNull
    @Column(name = "phone", length = 30)
    private String phone;

    @Size(max = 10)
    @Column(name = "main_subject", length = 10)
    private String mainSubject;

    @Size(max = 100)
    @Column(name = "department")
    private String department;

    @Size(max = 100)
    @Column(name = "student_number")
    private String studentNumber;


    public Teacher(String email, String name, Provider provider) {
        this.email=email;
        this.name = name;
        this.provider = provider;
    }

    @Size(max = 300)
    @Column(name = "fcm_token", nullable = false, length = 300)
    private String fcmToken;

}