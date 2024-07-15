package turing.turing.domain.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "school", nullable = false, length = 100)
    private String school;

    @Size(max = 10)
    @NotNull
    @Column(name = "year", nullable = false, length = 10)
    private String year;

    @Size(max = 30)
    @Column(name = "phone", length = 30)
    private String phone;

    @Size(max = 30)
    @Column(name = "parent_phone", length = 30)
    private String parentPhone;

    @Size(max = 300)
    @Column(name = "fcm_token", length = 300)
    private String fcmToken;

    @Builder
    public Student(String name, String school, String year, String phone, String parentPhone) {
        this.name = name;
        this.school = school;
        this.year = year;
        this.phone = phone;
        this.parentPhone = parentPhone;
    }
}