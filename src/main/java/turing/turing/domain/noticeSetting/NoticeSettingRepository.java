package turing.turing.domain.noticeSetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.member.Role;

import java.util.List;
import java.util.Optional;

public interface NoticeSettingRepository extends JpaRepository<NoticeSetting, Long> {

    @Query("SELECT ns FROM NoticeSetting ns WHERE ns.memberId = :memberId AND ns.role = :role AND ns.category = :category")
    NoticeSetting findByMemberIdAndRoleAndCategory(@Param("memberId") Long memberId,
                                                   @Param("role") Role role,
                                                   @Param("category") String category);
    List<NoticeSetting> findAllByMemberIdAndRole(Long memberId, Role role);

    @Query("SELECT ns FROM NoticeSetting ns WHERE ns.memberId = :memberId AND ns.role = :role AND ns.id = :notificationSettingId")
    NoticeSetting findByMemberIdAndRoleAndId(
            @Param("memberId") Long memberId,
            @Param("role") Role role,
            @Param("notificationSettingId") Long notificationSettingId
    );
}
