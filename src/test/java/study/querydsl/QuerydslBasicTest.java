package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
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

}

