package hu.bme.mit.spaceship;

import java.util.Random;

/**
* Class storing and managing the torpedoes of a ship
*
* (Deliberately contains bugs.)
*/
public class TorpedoStore {
  private final Random random = new Random();

  // rate of failing to fire torpedos [0.0, 1.0]
  private double FAILURE_RATE = 0.0; //NOSONAR

  private int torpedoCount = 0;

  public TorpedoStore(int numberOfTorpedos){
    this.torpedoCount = numberOfTorpedos;

    // update failure rate if it was specified in an environment variable
    String failureEnv = System.getenv("IVT_RATE");
    if (failureEnv != null){
      try {
        FAILURE_RATE = Double.parseDouble(failureEnv);
      } catch (NumberFormatException nfe) {
        FAILURE_RATE = 0.0;
      }
    }
  }

  public boolean fire(int numberOfTorpedos){
    if(numberOfTorpedos < 1 || numberOfTorpedos > this.torpedoCount){
      /* 
       * An exception should be thrown when entering this if statement
       * The reason for this is that the statements in the if operator represent states which are illegal
       * The variable `numberOfTorpedoes` is an integer, so if we say it is less that 1 (<1), it means it is 0 or smaller
       * We cannot shoot if we have 0 torpedoes, while negative amounts of torpedoes are not possible.
       * 
       * Furthermore, if we want to shoot more torpedoes than we have (numberOfTorpedoes parameter is larger than this.torpedoCount),
       * we would get a negative amount of torpedoes, which is illegal
      */
      throw new IllegalArgumentException("numberOfTorpedos");
    }

    boolean success = false;

    // simulate random overheating of the launcher bay which prevents firing
    double r = this.random.nextDouble();

    if (r >= FAILURE_RATE) {
      // successful firing
      this.torpedoCount -= numberOfTorpedos;
      success = true;
    } else {
      // simulated failure
      success = false;
    }

    return success;
  }

  public boolean isEmpty(){
    return this.torpedoCount <= 0;
  }

  public int getTorpedoCount() {
    return this.torpedoCount;
  }
}
