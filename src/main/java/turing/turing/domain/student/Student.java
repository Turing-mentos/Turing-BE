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
import turing.turing.domain.member.Member;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;

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
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Size(max = 100)
    @Column(name = "school", length = 100)
    private String school;

    @Size(max = 10)
    @Column(name = "year", length = 10)
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

    // 학생 가입용
    @Builder
    public Student(String email, Role role, Provider provider, String firstName, String lastName) {
        super(role, email, provider, null);  //TODO fcmToken!!!
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // 선생님 학생 등록용
    @Builder
    public Student(String firstName, String lastName, String school, String year, String phone, String parentPhone) {
        super(Role.STUDENT, null, null, null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.school = school;
        this.year = year;
        this.phone = phone;
        this.parentPhone = parentPhone;
    }

    // 학생 연결 시 기존 정보를 동기화하기 위해 사용
    public void updateSchoolAndYear(String school, String year) {
        this.school = school;
        this.year = year;
    }
}