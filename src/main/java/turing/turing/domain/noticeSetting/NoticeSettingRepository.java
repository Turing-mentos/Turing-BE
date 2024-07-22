package turing.turing.domain.noticeSetting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeSettingRepository extends JpaRepository<NoticeSetting, Long> {
    NoticeSetting findByMemberIdAndRoleAndCategory(Long memberId, String role, String category);
    List<NoticeSetting> findAllByMemberIdAndRole(Long memberId, String role);
}
