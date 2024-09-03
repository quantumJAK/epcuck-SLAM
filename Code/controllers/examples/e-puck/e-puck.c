#include <device/robot.h>
#include <device/differential_wheels.h>
#include <device/distance_sensor.h>
#include <device/light_sensor.h>
#include <device/camera.h>
#include <device/accelerometer.h>

#define TIME_STEP 64
#define WHEEL_RADIUS 0.0205
#define AXLE_LENGTH 0.052
#define ENCODER_RESOLUTION 159.23

DeviceTag distance_sensor[8],light_sensor[8],accelerometer,camera;

static void reset() {
  char device_name[]="ps0";
  int i;
  for(i=0;i<8;i++) {
    device_name[2]='0'+i;
    distance_sensor[i]=robot_get_device(device_name);
    distance_sensor_enable(distance_sensor[i],TIME_STEP*4);
    device_name[0]='l';
    light_sensor[i]=robot_get_device(device_name);
    light_sensor_enable(light_sensor[i],TIME_STEP*4);
    device_name[0]='p';
  }
  camera=robot_get_device("camera");
  camera_enable(camera,TIME_STEP*16);
  accelerometer=robot_get_device("accelerometer");
  accelerometer_enable(accelerometer,TIME_STEP*4);
  differential_wheels_enable_encoders(TIME_STEP*4);
}

static void compute_odometry() {
  int l,r;
  double dl,dr,da;
  l = differential_wheels_get_left_encoder();
  r = differential_wheels_get_right_encoder();
  dl = (double)l / ENCODER_RESOLUTION * WHEEL_RADIUS; // distance covered by left wheel in meter
  dr = (double)r / ENCODER_RESOLUTION * WHEEL_RADIUS; // distance covered by right wheel in meter
  da = (dr - dl) / AXLE_LENGTH; // delta orientation
  /*
  robot_console_printf("estimated distance covered by left wheel: %g m.\n",dl);
  robot_console_printf("estimated distance covered by right wheel: %g m.\n",dr);
  robot_console_printf("estimated change of orientation: %g rad.\n",da);
  */
  differential_wheels_set_encoders(0,0); /* reset encoder values for next step */
}

static int run(int ms) {
  const float *a;
  compute_odometry();
  a=accelerometer_get_values(accelerometer);
  robot_console_printf("accelerometer values = %0.2f %0.2f %0.2f\n",
                       accelerometer_value_x(a),
                       accelerometer_value_y(a),
                       accelerometer_value_z(a));
  differential_wheels_set_speed(30,70);
  
  return TIME_STEP;
}

int main(int argc, char *argv[]) {
  robot_live(reset);
  robot_run(run);
  return 1;
}
