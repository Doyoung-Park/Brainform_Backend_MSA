package kakao.answerservice.global.entity;

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

    @Column(name = "att_avg")
    private Float attAvg;

    @Column(name = "medit_avg")
    private Float meditAvg;
}
