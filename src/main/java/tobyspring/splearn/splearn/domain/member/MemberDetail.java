package tobyspring.splearn.splearn.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyspring.splearn.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
    @Embedded
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    // 어그리거트 안에서만 생성이 되게끔 default로 생성
    static MemberDetail create() {
        MemberDetail detail = new MemberDetail();
        detail.registeredAt = LocalDateTime.now();
        return detail;
    }

    void activate() {
        Assert.isTrue(this.activatedAt == null, "이미 ActivatedAt은 설정되었습니다.");
        this.activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        Assert.isTrue(this.deactivatedAt == null, "이미 DeactivatedAt은 설정되었습니다");
        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest request) {
        this.profile = new Profile(request.profileAddress());
        this.introduction = Objects.requireNonNull(request.introduction());
    }
}
