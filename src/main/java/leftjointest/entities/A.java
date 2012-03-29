package leftjointest.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class A {
  
  @Id
  private long id;
  
  @Deprecated
  public A() {
    // Just for JPA instantiation
  }
  
  public A(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

}
