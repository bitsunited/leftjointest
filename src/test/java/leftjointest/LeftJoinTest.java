package leftjointest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import leftjointest.entities.A;
import leftjointest.entities.B;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LeftJoinTest {

  private static EntityManagerFactory factory;

  private EntityManager entityManager;

  private A a;

  @BeforeClass
  public static void setupClass() {
    // create entity manager factory for derby in memory database
    factory = Persistence.createEntityManagerFactory("leftjointest");
  }

  @Before
  public void setup() {
    // create new entity manager
    entityManager = factory.createEntityManager();

    // create and persist A
    entityManager.getTransaction().begin();
    a = new A(17L);
    entityManager.persist(a);
    entityManager.getTransaction().commit();
  }

  @After
  public void tearDown() {
    // delete all objects from database
    entityManager.getTransaction().begin();
    entityManager.createQuery("DELETE FROM B").executeUpdate();
    entityManager.createQuery("DELETE FROM A").executeUpdate();
    entityManager.getTransaction().commit();

    entityManager.close();
  }

  @Test
  public void testSelectIdsWithReference() {
    // create and persist B with reference to A
    entityManager.getTransaction().begin();
    entityManager.persist(new B(31L, a));
    entityManager.getTransaction().commit();

    // query for the ids of A and B
    String queryString = "SELECT b.id, a.id FROM B b LEFT JOIN b.a a";
    Object[] result = (Object[])entityManager.createQuery(queryString).getSingleResult();

    // Expecting the ids of B and A
    assertEquals(2, result.length);
    assertEquals(31L, result[0]);
    assertEquals(17L, result[1]);
  }

  @Test
  public void testSelectIdsWithoutReference() {
    // create and persist B without reference to A
    entityManager.getTransaction().begin();
    entityManager.persist(new B(31L, null));
    entityManager.getTransaction().commit();

    // query for the ids of A and B
    String queryString = "SELECT b.id, a.id FROM B b LEFT JOIN b.a a";
    Object[] result = (Object[])entityManager.createQuery(queryString).getSingleResult();

    // Expecting the ids of B and null
    assertEquals(2, result.length);
    assertEquals(31L, result[0]);
    // should be null because B has no reference to A
    assertNull("should be null because B has no reference to A", result[1]);
  }

  @Test
  public void testSelectObjectsWithReference() {
    // create and persist B with reference to A
    entityManager.getTransaction().begin();
    B b = new B(31L, a);
    entityManager.persist(b);
    entityManager.getTransaction().commit();

    // query for the entities A and B
    String queryString = "SELECT b, a FROM B b LEFT JOIN b.a a";
    Object[] result = (Object[])entityManager.createQuery(queryString).getSingleResult();

    // Expecting the entities B and A
    assertEquals(2, result.length);
    assertEquals(b, result[0]);
    assertEquals(a, result[1]);
  }

  @Test
  public void testSelectObjectsWithoutReference() {
    // create and persist B without reference to A
    entityManager.getTransaction().begin();
    B b = new B(31L, null);
    entityManager.persist(b);
    entityManager.getTransaction().commit();

    // query for the entities A and B
    String queryString = "SELECT b, a FROM B b LEFT JOIN b.a a";
    Object[] result = (Object[])entityManager.createQuery(queryString).getSingleResult();

    // Expecting the entities B and null
    assertEquals(2, result.length);
    assertEquals(b, result[0]);
    assertNull(result[1]);
  }
}
