#include <device/robot.h>
#include <device/differential_wheels.h>
#include <device/accelerometer.h>
#include <device/led.h>
#include <device/distance_sensor.h>
#include <device/light_sensor.h>
#include <device/camera.h>

#define TIME_STEP 64

#define ON        1
#define OFF       0
#define NB_LEDS   10
DeviceTag led[NB_LEDS];

DeviceTag accelerometer;
const float *a;

#define NB_DIST_SENS 8
DeviceTag ps[NB_DIST_SENS];

#define NB_LIGHT_SENS 8
DeviceTag ls[NB_LIGHT_SENS];

DeviceTag cam;
unsigned char* im;

short direction=1;

static void reset() {
  char text[5]="led0";  
  int it;
  for(it=0;it<NB_LEDS;it++) {
    led[it]=robot_get_device(text);
    text[3]++;
    led_set(led[it],OFF); 
  }
  char textPS[] = "ps0";
  for (it=0;it<NB_DIST_SENS;it++) {
    ps[it] = robot_get_device(textPS);
    textPS[2]++;
    distance_sensor_enable(ps[it],2*TIME_STEP);
  }

  char textLS[] = "ls0";
  for (it=0;it<NB_LIGHT_SENS;it++) {
    ls[it] = robot_get_device(textLS);
    textLS[2]++;
    light_sensor_enable(ls[it],2*TIME_STEP);
  }
  differential_wheels_enable_encoders(TIME_STEP);
  differential_wheels_set_encoders  (0,0);
  accelerometer=robot_get_device("accelerometer");
  accelerometer_enable(accelerometer,TIME_STEP);
  cam=robot_get_device("camera");
  camera_enable(cam,4*TIME_STEP);
}

static int run(int ms) {

  int it,m,n;
  long int sum=0;
  
  im = camera_get_image(cam);
  for (m=0;m<52;m++)
    for (n=0;n<39;n++)
      sum+=camera_image_get_grey(im,52,m,n);
  
  a=accelerometer_get_values(accelerometer);
  float x = accelerometer_value_x(a);
  float y = accelerometer_value_y(a);
  
  int count=0;
  for (it=0;it<NB_DIST_SENS;it++) {
    count+=distance_sensor_get_value(ps[it]);
  }
  
  for(it=0;it<NB_LEDS;it++) {
    led_set(led[it],OFF); 
  }
  
  if (robot_get_mode()==1){
    led_set(led[0],1);
  }
  
  if (x<=0.0f && y<=0.0f) led_set(led[1],1);
  if (x<=0.0f && y>0.0f)  led_set(led[3],1);
  if (x>0.0f && y>0.0f)   led_set(led[5],1);
  if (x>0.0f && y<=0.0f)  led_set(led[7],1);
  
  if (count>7000){
    led_set(led[8],1);
  }
  
  if (sum<100000){
    led_set(led[9],1);
  }
  
  if (differential_wheels_get_left_encoder()>1000 ||
      differential_wheels_get_left_encoder()<-1000){
	  if (direction==1) direction = -1;
    else direction = 1;
    differential_wheels_set_encoders(0,0);
    robot_console_printf("Other direction\n");
  }
  
  differential_wheels_set_speed(300*direction,-300*direction);
  
  return TIME_STEP;
}

int main(int argc, char *argv[]) {
  robot_live(reset);
  robot_run(run);
  return 1;
}
