package ac.dnd.mur.server.group.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.exception.GroupException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_NOT_FOUND;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new GroupException(GROUP_NOT_FOUND));
    }

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Group g WHERE g.memberId = :memberId")
    void deleteMemberGroups(@Param("memberId") final Long memberId);

    boolean existsByMemberIdAndName(final long memberId, String name);

    List<Group> findByMemberId(final long memberId);
}
