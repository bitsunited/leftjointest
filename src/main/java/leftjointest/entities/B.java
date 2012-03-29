package leftjointest.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class B {

  @Id
  private long id;
  
  @OneToOne(optional = true)
  private A a;
  
  @Deprecated
  public B() {
    // Just for JPA instantiation
  }

  public B(long id, A a) {
    this.id = id;
    this.a = a;
  }

  public long getId() {
    return id;
  }
  
  public A getA() {
    return a;
  }

}
