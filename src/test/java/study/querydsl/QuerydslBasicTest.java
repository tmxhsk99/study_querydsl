package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;

    JPQLQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    //Querydsl va JPQL

    @Test
    public void startJPQL() {
        //member1을 찾아라
        String qlString =
                "select m from Member m " +
                        "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        //member1 을 찾아라
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void startQuerydsl2() {
        //Member1 을 찾아라
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl3(){

        //기본 인스턴스를 static import 함께 사용한 경우
        //import static study.querydsl.entity.QMember.*;

        //member1 을 찾아라
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search () {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void provideJpqlSearch(){

        member.username.eq("member1"); // username = 'member1'
        member.username.ne("member1"); // username != 'member1'
        member.username.eq("member1").not(); //username != 'member1'

        member.username.isNotNull(); //이름이 is not null

        member.age.in(10,20); //age in (10,20);
        member.age.notIn(10, 20); // age not in ( 10 , 20 )
        member.age.between(10, 30); // age between (10 , 30 )

        member.age.goe(30); //age >= 30
        member.age.gt(30); // age > 30
        member.age.loe(30); // age <= 30
        member.age.lt(30); // age < 30

        member.username.like("member%"); //like 검색
        member.username.contains("member"); //like '%like%' 검색
        member.username.startsWith("member"); //like '%like' 검색

    }

    @Test
    public void searchAndParam(){
        List<Member> resultList = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),
                        member.age.eq(10))
                .fetch();

        assertThat(resultList.size()).isEqualTo(1);
    }

    @Test
    public void resultFetch() {
        //List
        List<Member> memberList = queryFactory
                .selectFrom(member)
                .fetch();

        //단 건 조회 (없으면 null , 단건이 아닌경우 NonUnitqueResultException 이 발생한다.)
        Member findMember = queryFactory
                .selectFrom(member)
                .fetchOne();

        //처음 한 건 조회
        Member findMemberFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        //페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        //count 쿼리로 변경하기
        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();

    }


    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순 (desc)
     * 2. 회원 이름 올림차순 (asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() {
        //테스트를 위한 추가 데이터 삽입
        em.persist(new Member(null, 100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    //페이징
    //조회 건수 제한
    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)  //0 부터 시작 (zero index)
                .limit(2)  //최대 2건 조회
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);

    }
    /**
     * JPQL
     * select
     *     COUNT(m), //회원수
     *     SUM(m.age), //나이 합
     *     AVG(m.age). //평균 나이
     *     MAX(m.age). //최대 나이
     *     MIN(m.age). //최소 나이
     * from Member m
     */
    @Test
    public void aggregation() throws Exception {

        List<Tuple> result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라...
     */
    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("TeamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("TeamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);

    }

    /**
     * 팀 A에 소속된 모든 회원
     */

    @Test
    public void join() throws Exception {
        QMember member = QMember.member;
        QTeam qTeam = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("TeamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인 (연관관계가 없는 필드로 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조절
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("TeamA"));
        em.persist(new Member("TeamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("TeamA","TeamB");

    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 TeamA인 팀만 조인, 회원은 모두 조회
     *  JPQL : SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'TeamA'
     *  SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name = 'TeamA'
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("TeamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 2. 연관관계가 없는 엔티티 외부 조인
     * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     *  JPQL : SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name
     *  SQL  : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("TeamA"));
        em.persist(new Member("TeamB"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);

        }

    }
}

