/*
 * File:         
 * Date:         
 * Description:  
 * Author:       
 * Modifications:
 */

import com.cyberbotics.webots.Controller;

public abstract class JavaController extends Controller {
  /*
   * You should declare here int fields for storing pointers to robot
   * devices.
   */

  /*
   * This is the reset function called once upon initialization of the robot.
   */
  public static void reset() {
    /*
     * You should insert robot_get_device() calls to get pointers to robot
     * devices (int) and use them in the main control loop.
     */
  }
  /*
   * This is the main method which sets up the reset and run function.
   */
  public static void main(String args[]) {
    reset();
    main();
  }
  /*
   * This is the main control loop function
   */
  public static void main() {
    for(;;) {
      /*
       * Call here methods to read sensor data like
       * distance_sensor_get_value().
       */

      /*
       * Process sensor data here.
       */

      /*
       * Call here methods to send actuator commands,
       * like differential_wheels_set_speed().
       */

      robot_step(64); /* the time step value is expressed in milliseconds */
    }
  }
}
