package kakao.surveyservice.global.repository;//package kakao.memberservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kakao.surveyservice.global.dto.FilterDTO;
import kakao.surveyservice.global.entity.MemberSurvey;
import kakao.surveyservice.global.entity.QMemberSurvey;
import kakao.surveyservice.global.entity.QSurvey;
import kakao.surveyservice.global.entity.Survey;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MemberSurveyRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    private QMemberSurvey memberSurvey = QMemberSurvey.memberSurvey;

    private QSurvey survey = QSurvey.survey;


    public MemberSurveyRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<MemberSurvey> getMemberSurveyFilter(FilterDTO filterDTO) {

        BooleanExpression surveyExpression = surveyEq(filterDTO.getSurveyId());
        BooleanExpression genderExpression = genderEq(filterDTO.getGenders());
        BooleanExpression ageExpression = ageEq(filterDTO.getAges());
        BooleanExpression jobExpression = jobEq(filterDTO.getOccupations());

        return query
                .select(memberSurvey)
                .from(memberSurvey)
                .where(surveyExpression,genderExpression, ageExpression, jobExpression)
                .fetch();
    }

    public MemberSurvey save(MemberSurvey memberSurvey) {
        em.persist(memberSurvey);
        return memberSurvey;
    }


    private BooleanExpression surveyEq(Long surveyId) {
        if (surveyId == null) {
            return null;
        }
        return memberSurvey.survey.id.eq(surveyId);
    }
    private BooleanExpression genderEq(List<String> genders) {
        if (genders == null || genders.isEmpty()) {
            return null;
        }
        return memberSurvey.member.gender.in(genders);
    }

    private BooleanExpression ageEq(List<String> ages) {
        if (ages == null || ages.isEmpty()) {
            return null;
        }
        return memberSurvey.member.age.in(ages);
    }

    private BooleanExpression jobEq(List<String> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return null;
        }
        return memberSurvey.member.job.in(jobs);
    }

    public List<Survey> findMemberSurveyByMemberId(Long id) {
        return query
                .select(survey)
                .from(memberSurvey)
                .where(memberSurvey.member.id.eq(id))
                .fetch();
    }

    public void deleteMemberSurvey(MemberSurvey memberSurvey) {
        em.remove(memberSurvey);
    }
}
