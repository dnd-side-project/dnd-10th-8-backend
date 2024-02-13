package ac.dnd.mour.server.group.domain.repository;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.exception.GroupException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static ac.dnd.mour.server.group.exception.GroupExceptionCode.GROUP_NOT_FOUND;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new GroupException(GROUP_NOT_FOUND));
    }

    @MourWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Group g WHERE g.id = :id")
    void deleteGroup(@Param("id") final long id);

    @MourWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Group g WHERE g.memberId = :memberId")
    void deleteMemberGroups(@Param("memberId") final Long memberId);

    boolean existsByMemberIdAndName(final long memberId, String name);

    List<Group> findByMemberId(final long memberId);

    Optional<Group> findByIdAndMemberId(final long id, final long memberId);

    default Group getMemberGroup(final long id, final long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new GroupException(GROUP_NOT_FOUND));
    }
}
