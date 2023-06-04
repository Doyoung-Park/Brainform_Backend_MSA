package kakao.memberservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kakao.memberservice.dto.MemberRegisterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private String username;

    @Column
    private String nickname;

    @Column
    private String gender;

    @Column
    private String age;

    @Column
    private String email;

    @Column
    private String job;

    @Column
    private String provider;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;


    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Survey> surveys = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY) //roles 컬렉션
    @JsonIgnore
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member updateOAuth(String email, String username) {
        this.email = email;
        this.username = username;

        return this;
    }

    public Member register(MemberRegisterDTO dto) {
        this.nickname = dto.getNickname();
        this.age = dto.getAge();
        this.gender = dto.getGender();
        this.job = dto.getJob();
        this.roles = Collections.singletonList("ROLE_USER");

        return this;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;

    }
    public void setGender(String gender) {
        this.gender=gender;
    }
    public void setAge(String age) {
        this.age=age;
    }

    public void setJob(String job) {
        this.job=job;
    }
}


