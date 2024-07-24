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
import turing.turing.domain.member.Member;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.member.dto.Profile;

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
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Size(max = 100)
    @Column(name = "university", length = 100)
    private String university;

    @Size(max = 30)
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

    public Teacher(String email, Role role, Provider provider, String firstName, String lastName) {
        super(role, email, provider, null);  //TODO fcmToken!!!
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updateProfile(Profile profile) {
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.university = profile.getUniversity();
        this.studentNumber = profile.getStudentNumber();
        this.department = profile.getDepartment();
    }
}