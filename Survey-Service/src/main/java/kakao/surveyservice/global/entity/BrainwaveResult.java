package kakao.surveyservice.global.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "brainwave_result")
public class BrainwaveResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @Column(name = "brainwave_img")
    private String img;

    private String temp_1;
    private String temp_2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participate_id")
    private MemberSurvey memberSurvey;
}
