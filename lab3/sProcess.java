public class sProcess {
  public int id;
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int priority;
  public int delaytime;

  public sProcess (int id , int cputime, int ioblocking, int cpudone, int ionext, int numblocked,int priority,int delaytime) {
    this.id = id;
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.priority = priority;
    this.delaytime=delaytime;
  }
}