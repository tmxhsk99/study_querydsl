package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParentTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testCompositeKeyEntity() {
        Parent parent = new Parent();
        parent.setId1("myId1"); // 식별자
        parent.setId2("myId2"); // 식별자
        parent.setName("parent1");

        Parent parent2 = new Parent();
        parent2.setId1("myId1"); // 식별자
        parent2.setId2("myId4"); // 식별자
        parent2.setName("parent2");

        em.persist(parent);
        em.persist(parent2);
        em.flush();
        em.close();

        ParentId parentId = new ParentId("myId1", "myId2");
        Parent findParent = em.find(Parent.class, parentId);

        System.out.println("findParentId1 = " + findParent.getId1());
        System.out.println("findParentId2 = " + findParent.getId2());
        System.out.println("findParentName = " + findParent.getName());
    }

}