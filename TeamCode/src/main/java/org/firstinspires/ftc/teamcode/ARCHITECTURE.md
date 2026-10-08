## Auto Class
For the 30 secs auto at the start of the game.

PedroPathing: Lightweight, easy to use, designed specifically for FTC, simple, quick setup,
      supports basic path following, localization, and simple configuration.

RoadRunner: powerful, lots of features, motion planning library for FTC.
      supports advanced trajectory generation (splines, curves), velocity/acceleration constraints,
      and integrates with odometry and localization. steeper learning curve and more complex setup.


pose factory = make poses in degrees.
input the poses (x,y, angle) value for each stop after route planned out

**Functions**

_intakeGarden()_
path for intake

_driveToFlower()_
path for intake from flower

_parkFromFlower()_
path for park from flower

ALL OF THE ABOVE:
line = straight line path, linear = constant heading change

_autoRoutine()_

Put the path function in order to create the whole routine.

_sequential_: Ivy command to run the path in order.

Scheduler: Ivy scheduler that runs the commands in order.

## Camera Class
A limelight camera is mounted on to the robot where it’s used to detect AprilTag and get the distance from it to adjust the turret’s speed to shoot.

**Variables**

_camera_mounted_angle_

_camera_height_

_target_height_

_angleToGoalDegrees_

**Functions**

_getDistanceFromAprilTag_

Help get the distance of the robot from the April tag. We do this by using trigonometry where we have the camera height, target height, the RobotAngle from Odometry class.

_getAprilTagPosition(int aprilTagID)_

Get latest result from limelight(built-in function for limelight), if nothing detected return null.
If detected it will getFiducialId which is the April tag ID that we need. Position p is determined as we get the target pose camera space for the ID that get the position. We then store each x, y, z value in different doubles to return.

## Drive Class
Field centric driving=move in direction relative to the driver. Wheels needs direction relative to the robot, have to rotate the input by the opposite of robot’s heading. Double rotX and rotY are the 2D rotation of x and Y by -yaw. We then rotate the input. And add the strafe part to move side way. Denominator d the largest possible total power so robot can still move in wanted direction. Math equation to account for what the wheels should be doing when moving different direction. Apply power.

**Functions**

_driveUpdateFromGamepadInput(double x, double y, double rx, float yaw)_

Respond based on gamepad input, this function is sued in main class.

## Intake Class
Run intake to collect pollen and nectar to later be sorted and shoot. Name intakeMotor as a DcMotor. Toggle variable to control motor on or off.

**Functions**

_isFull(int targetArtifacts)_

See if there’s 4 artifacts in the robot. Return whether is full.

_toggleIntake (boolean toggle)_

* Boolean function where it’s returns only true or false. If the toggle isFull it does certain things.
* If toggle of 4 artifacts is not full (robot can only hold 4 artifacts) run intake to intake more artifacts and return true.
* If toggle is full return false and no intake.
* Lastly, if condition is false than return false.

## Launcher Class
(finding the ideal speed for motors to shoot pollen and nectar. Different for pollen and nectar for their size and weight. Test with turrent to create the look up table- distance with their paired shooting speed that could shoot in the hive)

**Variables**

Pollen_distance_list = the distance value from the look up table for pollen shooting (list of doubles)

Nectar_distance_ist = the distance value from the look up table for nectar shooting(list of doubles)

Pollen_power_list = the column of power in the look up table for pollen

Nectar_power_list =  the column of power in the look up table for nectar

globalDelta = change in x value so we could calculate the slope of every point for the look up table created through testing.

globalEpsilon = maximum acceptable error for the velocity. If there is too much velocity difference between what is the real velocity and the look up table’s ideal velocity return false (not shoot).

pollen launcher = DcMotor + hardwaremap
nectar_launcher = DcMotor = hardwaremap

**Functions**

_getBoundingIndexes(double[] list, double targetValue)_

Based the distance find the bottom velocity and top velocity for the distance further away and the velocities closer to find the bound velocity for the current distance.(we cannot find power for each distance we can approximate using the data we already have, this is finding the highest and lowest power for the turret to run based on the distance a bit higher and a bit lower than the current one)
this would return the list of two number (bottom bound position in the list, top bound position in the list)

_interpolateLauncherPowerFromTable(int launcherID, double[] distanceList, double[] powerList, double targetDistance, double deltaDistance)_

Based on the found of bounding indexes we try to find the targetRPM the turret should run to shoot in the hive.
Find the powerBounds based on distanceList(since it’s respective to power list we could use the same one)

targetRPM = ((y_2 - y_1)/delta)(targetDistance - x_1) + y_1
  
-This would return a list of double with targeRPM, bottom power bounds and top power bounds.

_isLauncherPowered(int launcherID, double targetRPM, double epsilon)_

Check is the launcher velocity is the same with the target RPM calculated. Epsilon is the different between the ream rpm and targetRPM. Epsilon is a number input by our self.


_toggleLauncher(int launcherID, double[] result, double epsilon, boolean toggle)_

If launcher ID =1 the pollen launcher shoot, if ID = 2 nectar launcher shoot.

## Main Class
For the 1.5 mins auto teleop during the game. Import different classes for each individual parts to together function in this class.
If gamepad1.a is pressed intake.toggleIntake function is true meaning it runs. Vise visa.
drive.driveupdateFromGamepadInput function used to correspond x value to the gamepad1.left_stick_x similarly y to right_stick_y and angle as a float from odometry.
If gamepad aWasPressed or bWasPressed, turn off if it’s on turn on if it’s off.

**Init**

Odometry constant list = 0, 0, 0 XYZ position should be initialized

a2pressed = whether button pressed = false (not pressed) at start

b2pressed = whether button pressed = false (not pressed) at start




**Function**
_toggleLauncher(launcherID 1 [pollen])_

Shoot corresponding artifact in their shooter when a2pressed

_toggleLauncher(launcherID 2 [nectar])_

Shoot corresponding artifact in their shooter when b2Pressed.



## Odometry Class
Import GoBildaPinpointDriver as pinpoint. Setting basic info for sensor and make sure robot is stationary. Setting up the starting position and orientation.

**Variables**

xStartInches = x value at the starting position

ystartInches = y value at the starting position

headingStartDegrees = starting orientation in degrees

xOffset = Sensor tracking forward/backward movement's distance in inches to the left from the center.

yOffset = Sensor tracking left/right movement's distance in inches to the front from the center.

**Function**

_getRobotPosition()_

Update value from pinpoint. Create x, y variable which is the x y value of robot’s position. Done by directly getPosx and getPosY function built in.

_getRobotAngle()_

Update value from pinpoint. Use getHeading function from pinpoint with angle unit of degrees.

_getRobotVelocity()_

Update value from pinpoint. Create xVel and Yvel variables. They equals to the getVelX and getVelY value from pinpoint functions. Same as headingVel which is get from getHeadingVelocity. This is gonna return a double list with three stuff.







