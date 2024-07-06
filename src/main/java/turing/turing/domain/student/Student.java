package turing.turing.domain.student;

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
public class Student extends BaseEntity {

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
    @NotNull
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Size(max = 30)
    @Column(name = "parent_phone", length = 30)
    private String parentPhone;

    @Size(max = 300)
    @Column(name = "fcm_token", nullable = false, length = 300)
    private String fcmToken;

}