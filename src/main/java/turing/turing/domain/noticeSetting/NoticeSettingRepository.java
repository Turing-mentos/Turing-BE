package turing.turing.domain.noticeSetting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeSettingRepository extends JpaRepository<NoticeSetting, Long> {
    NoticeSetting findByMemberIdAndRoleAndCategory(Long memberId, String role, String category);
}
