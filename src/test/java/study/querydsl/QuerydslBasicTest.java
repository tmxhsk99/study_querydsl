package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;
import static com.querydsl.jpa.JPAExpressions.select;
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    EntityManagerFactory emf;

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
        //member1??? ?????????
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
        //member1 ??? ?????????
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
        //Member1 ??? ?????????
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

        //?????? ??????????????? static import ?????? ????????? ??????
        //import static study.querydsl.entity.QMember.*;

        //member1 ??? ?????????
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

        member.username.isNotNull(); //????????? is not null

        member.age.in(10,20); //age in (10,20);
        member.age.notIn(10, 20); // age not in ( 10 , 20 )
        member.age.between(10, 30); // age between (10 , 30 )

        member.age.goe(30); //age >= 30
        member.age.gt(30); // age > 30
        member.age.loe(30); // age <= 30
        member.age.lt(30); // age < 30

        member.username.like("member%"); //like ??????
        member.username.contains("member"); //like '%like%' ??????
        member.username.startsWith("member"); //like '%like' ??????

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



    public void resultFetch() {
        //List
        List<Member> memberList = queryFactory
                .selectFrom(member)
                .fetch();

        //??? ??? ?????? (????????? null , ????????? ???????????? NonUnitqueResultException ??? ????????????.)
        Member findMember = queryFactory
                .selectFrom(member)
                .fetchOne();

        //?????? ??? ??? ??????
        Member findMemberFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        //??????????????? ??????
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        //count ????????? ????????????
        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();

    }


    /**
     * ?????? ?????? ??????
     * 1. ?????? ?????? ???????????? (desc)
     * 2. ?????? ?????? ???????????? (asc)
     * ??? 2?????? ?????? ????????? ????????? ???????????? ?????? (nulls last)
     */
    @Test
    public void sort() {
        //???????????? ?????? ?????? ????????? ??????
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

    //?????????
    //?????? ?????? ??????
    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)  //0 ?????? ?????? (zero index)
                .limit(2)  //?????? 2??? ??????
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
     *     COUNT(m), //?????????
     *     SUM(m.age), //?????? ???
     *     AVG(m.age). //?????? ??????
     *     MAX(m.age). //?????? ??????
     *     MIN(m.age). //?????? ??????
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
     * ?????? ????????? ??? ?????? ?????? ????????? ?????????...
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
     * ??? A??? ????????? ?????? ??????
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
     * ?????? ?????? (??????????????? ?????? ????????? ??????)
     * ????????? ????????? ??? ????????? ?????? ?????? ??????
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
     * ???) ????????? ?????? ???????????????, ??? ????????? TeamA??? ?????? ??????, ????????? ?????? ??????
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
     * 2. ??????????????? ?????? ????????? ?????? ??????
     * ???) ????????? ????????? ?????? ????????? ?????? ?????? ?????? ??????
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

    @Test
    public void fetchJoinNo() throws Exception {

        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("?????? ?????? ?????????").isEqualTo(false);

    }

    @Test
    public void fetchJoin() throws Exception {

        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("?????? ?????? ??????").isTrue();

    }

    /**
     * ????????? ?????? ?????? ?????? ??????
     */
    @Test
    public void subQuery() throws Exception{
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(
                        member.age.eq(
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub)))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(40);

    }

    /**
     * ????????? ?????? ?????? ????????? ??????
     */

    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(
                        member.age.goe(
                                JPAExpressions
                                        .select(memberSub.age.avg())
                                        .from(memberSub)))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    /**
     * ???????????? ?????? ??? ?????? in ??????
     */
    @Test
    public void subQueryIn() throws Exception{
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(
                        member.age.in(
                                JPAExpressions
                                        .select(memberSub.age)
                                        .from(memberSub)
                                        .where(memberSub.age.gt(10))))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    /**
     * select ?????? subQuery
     */
    @Test
    public void selectSubQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> fetch = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("username = " + tuple.get(member.username)) ;
            System.out.println("age = " + tuple.get(JPAExpressions.select(memberSub.age.avg()).from(memberSub)));
        }

    }

    /**
     * static import ??????
     * 
     */
    @Test
    public void StaticImportJPAExpresstion() throws Exception {
        QMember memberSub = new QMember("memberSub");


        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)))
                .fetch();

    }

    /**
     * ????????? ????????? case ???
     */
    @Test
    public void simpleCaseExample() throws Exception{
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("??????")
                        .when(20).then("?????????")
                        .otherwise("??????"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("result = " + s);
        }
    }
    /**
     * ????????? ?????? Case ???
     */
    @Test
    public void complexCaseExample() throws Exception {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20???")
                        .when(member.age.between(21, 30)).then("21~30???")
                        .otherwise("??????"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /**
     * orderBy case Example
     */
    @Test
    public void orderByCaseExample () throws Exception{
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> fetch = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple tuple : fetch) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(rankPath);
            System.out.println("username = " + username +" age  = " + age  +" rank = " + rank);
        }

    }

    /**
     * ?????? ?????? ??????
     * @throws Exception
     */
    @Test
    public void constantExample() throws Exception {
        Tuple result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetchFirst();

        System.out.println("result = " + result);
    }

    @Test
    public void concatExample(){
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        System.out.println("result = " + result);
    }

    @Test
    public void projectionBasicExample() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("result = " + s);
        }
    }

    @Test
    public void tupleBasicExample() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            int age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    /**
     * ?????? JPA?????? DTO ??????
     */
    @Test
    public void pureJPADtoSelect() throws Exception {
        List<MemberDto> result = em.createQuery(
                        "select new study.querydsl.dto.MemberDto(m.username , m.age) " +
                                "from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * ???????????? ????????????
     */
    @Test
    public void DtoByUseSetter() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    /**
     * ?????? ????????????
     */
    @Test
    public void DtoByUseField() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * ????????? ?????????
     */
    @Test
    public void DtoByUseFieldDiffAlias() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class, member.username.as("name"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub),"age")))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    /**
     * ????????? ??????
     */
    @Test
    public void DtoByConstructor() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    /**
     * @QueryProjection ??????
     */
    @Test
    public void queryProejctionExample() throws Exception {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * distinct ??????
     */
    @Test
    public void querydslDistinctExample() throws Exception {
        List<String> result = queryFactory
                .select(member.username).distinct()
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("distinct Result = " + s);
        }
    }

    /**
     * ?????? ?????? : BooleanBuilder ????????????
     */
    @Test
    public void dynamic_BooleanBuilder() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);

    }

    private List<Member> searchMember1 (String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();

        if(usernameCond != null ) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }
    @Test
    public void dynamic_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond),ageEq(ageCond))
                .fetch();

    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
        //null ????????? ???????????? ???????????????
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    /**
     * ?????? ???????????? ?????? ????????? ??????
     */
    @Test
    public void bulkUpdate() throws Exception {
        long count = queryFactory
                .update(member)
                .set(member.username, "?????????")
                .where(member.age.lt(28))
                .execute();

        System.out.println("count = " + count);
        //????????? ???????????? ?????????
        em.flush();
        em.clear();
    }

    /**
     * ?????? ????????? 1?????????
     * ????????? : multiply(x)
     */
    @Test
    public void bulkAddCount() throws Exception {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
        System.out.println("count = " + count);

        //????????? ???????????? ?????????
        em.flush();
        em.clear();
    }

    /**
     * ?????? ???????????? ????????? ??????
     */
    @Test
    public void bulkBatchDelete() throws Exception {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
        System.out.println("count = " + count);

        //????????? ???????????? ?????????
        em.flush();
        em.clear();
    }

    /**
     * member M?????? ???????????? replace ?????? ??????
     */
    @Test
    public void replaceFunction() throws Exception {
        String result = queryFactory
                .select(
                        Expressions
                                .stringTemplate("function('replace', {0}, {1}, {2})", member.username, "member", "M"))
                .from(member)
                .fetchFirst();

        System.out.println("result = " + result);
    }

    /**
     * ???????????? ???????????? ????????????
     */
    @Test
    public void lowerFunction() throws Exception {
        String result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(Expressions.stringTemplate("function('lower',{0})", member.username)))
                .fetchFirst();

        System.out.println("result = " + result);

        //lower ?????? ansi ?????? ???????????? querydsl??? ?????? ?????? ???????????? ??????. ????????? ????????? ?????? ???????????? ????????? ??????.
        String result2 = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(member.username.lower()))
                .fetchFirst();
        System.out.println("result2 = " + result2);

    }

}


