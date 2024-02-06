package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum HeartFixture {
    결혼_축의금을_보냈다(
            true, 500_000,
            LocalDate.of(2024, 1, 24),
            "결혼", "메모...",
            new ArrayList<>(List.of("참석"))
    ),
    결혼_축의금을_받았다(
            false, 300_000,
            LocalDate.of(2024, 1, 24),
            "결혼", "메모...",
            new ArrayList<>(List.of("참석"))
    ),

    생일_선물을_보냈다(
            true, 500_000,
            LocalDate.of(2024, 1, 24),
            "생일", "메모...",
            new ArrayList<>(List.of("상품권"))
    ),
    생일_선물을_받았다(
            false, 1_000_000,
            LocalDate.of(2024, 2, 24),
            "생일", "메모...",
            new ArrayList<>(List.of("상품권"))
    ),

    출산_선물을_보냈다(
            true, 500_000,
            LocalDate.of(2024, 1, 24),
            "출산", "메모...",
            new ArrayList<>(List.of("출산"))
    ),
    출산_선물을_받았다(
            false, 1_000_000,
            LocalDate.of(2024, 1, 24),
            "출산", "메모...",
            new ArrayList<>(List.of("출산"))
    ),

    돌잔치_선물을_보냈다(
            true, 500_000,
            LocalDate.of(2024, 1, 24),
            "돌잔치", "메모...",
            new ArrayList<>(List.of("돌잔치"))
    ),
    돌잔치_선물을_받았다(
            false, 1_000_000,
            LocalDate.of(2024, 1, 24),
            "돌잔치", "메모...",
            new ArrayList<>(List.of("돌잔치"))
    ),

    개업_선물을_보냈다(
            true, 500_000,
            LocalDate.of(2024, 1, 24),
            "개업", "메모...",
            new ArrayList<>(List.of("개업"))
    ),
    개업_선물을_받았다(
            false, 1_000_000,
            LocalDate.of(2024, 1, 24),
            "개업", "메모...",
            new ArrayList<>(List.of("개업"))
    ),

    승진_선물을_보냈다(
            true, 1_000_000,
            LocalDate.of(2024, 1, 24),
            "승진", "메모...",
            new ArrayList<>(List.of("참석", "상품권"))
    ),
    승진_선물을_받았다(
            false, 2_000_000,
            LocalDate.of(2024, 1, 24),
            "승진", "메모...",
            new ArrayList<>(List.of("참석", "상품권"))
    ),
    ;

    private final boolean give;
    private final long money;
    private final LocalDate day;
    private final String event;
    private final String memo;
    private final List<String> tags;

    public Heart toDomain(final Member member, final Relation relation) {
        return new Heart(member, relation, give, money, day, event, memo, tags);
    }

    public Heart toDomain(final Member member, final Relation relation, final List<String> tags) {
        return new Heart(member, relation, give, money, day, event, memo, tags);
    }

    public Heart toDomain(final Member member, final Relation relation, final long money) {
        return new Heart(member, relation, give, money, day, event, memo, tags);
    }

    public Heart toDomain(final Member member, final Relation relation, final long money, final LocalDate day) {
        return new Heart(member, relation, give, money, day, event, memo, tags);
    }
}
