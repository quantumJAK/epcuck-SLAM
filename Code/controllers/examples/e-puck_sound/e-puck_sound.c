//------------------------------------------------------------------------------------------
// File:         e-puck_sound.c
// Description:  Sound plugin demo using two e-puck robots.
//               One e-puck emits sound samples on its speaker, the other e-puck listens to
//               one of its 3 microphones and tries to detect these sound samples.
//               Note that the same controller code (this file) is used for both robots.
//               The particular "microphone" or "speaker" role is assigned according to what
//               is specified in the "controllerArgs" field of the DifferentialWheels robot.
// Author:       Yvan Bourquin, Swarm-intelligent Systems Group (SWIS), EPFL, Switzerland
// Created:      26-Oct-2007
//------------------------------------------------------------------------------------------

#include <device/robot.h>
#include <device/speaker.h>
#include <device/microphone.h>
#include <device/distance_sensor.h>
#include <device/differential_wheels.h>
#include <string.h>

#define TIME_STEP 32
#define NUM_SENSORS 8
#define RANGE (1024 / 2)

enum { UNKNOWN, SPEAKER, MICROPHONE };

static int role = UNKNOWN;
static DeviceTag speaker, microphone;
static DeviceTag sensors[NUM_SENSORS];

static const float EPUCK_MATRIX[8][2] = {
  { 150, -35 }, { 100, -15 }, {  80, -10 }, { -10, -10 },
  { -10, -10 }, { -10,  80 }, { -30, 100 }, { -20, 150 }
};

static void reset() {

  // use speaker or microphone according to specified role
  if (role == SPEAKER)
    speaker = robot_get_device("speaker");
  else if (role == MICROPHONE) {
    // use "mic0" only for this demo
    microphone = robot_get_device("mic0");
    microphone_enable(microphone, TIME_STEP / 2);
  }

  // find distance sensors
  int i;
  for (i = 0; i < NUM_SENSORS; i++) {
    char sensor_name[64];
    sprintf(sensor_name, "ps%d", i);
    sensors[i] = robot_get_device(sensor_name);
    distance_sensor_enable(sensors[i], TIME_STEP);
  }
}

// sound sample to emit
static const short SAMPLE[] = {
  -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128,
  127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127
};

static int prev_audible = -1;

static int run(int ms) {

  int i, j; // for loops

  if (role == SPEAKER) {
    speaker_emit_sample(speaker, SAMPLE, sizeof(SAMPLE));
  }
  else if (role == MICROPHONE) {
    const short *rec_buffer = (const short *)microphone_get_sample_data(microphone);
    int numSamples = microphone_get_sample_size(microphone) / sizeof(SAMPLE[0]);
    if (rec_buffer) {
      int audible = 0;
      for (i = 0; i < numSamples; i++) {
        // warning this demo assumes no noise and therefore depends on the sound plugin configuration !
        if (rec_buffer[i] != 0)
          audible = 1;
      }
      
      if (audible != prev_audible) {
        robot_console_printf(audible ? "I hear you now !\n" : "I can't hear you !\n");
        prev_audible = audible;
      }
    }
    else
      robot_console_printf("received: nothing this time ...\n");
  }

  // read distance sensor values
  float sensor_values[NUM_SENSORS];
  for (i = 0; i < NUM_SENSORS; i++)
    sensor_values[i] = distance_sensor_get_value(sensors[i]);
  
  // compute braitenberg collision avoidance
  float speed[2];
  for (i = 0; i < 2; i++) {
    speed[i] = 0.0;
    for (j = 0; j < NUM_SENSORS; j++)
      speed[i] += EPUCK_MATRIX[j][i] * (1 - (sensor_values[j] / RANGE));
  }

  // set the motors speed
  differential_wheels_set_speed((int)speed[0], (int)speed[1]);

  return TIME_STEP;
}

int main(int argc, const char *argv[]) {

  // determine role
  if (argc >= 2) {
    if (! strcmp(argv[1], "speaker"))
      role = SPEAKER;
    else if (! strcmp(argv[1], "microphone"))
      role = MICROPHONE;
  }
 
  robot_live(reset);
  robot_run(run);
  return 0;
}
