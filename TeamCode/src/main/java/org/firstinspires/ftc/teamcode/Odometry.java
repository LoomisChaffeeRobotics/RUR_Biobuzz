package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;


public class Odometry {
    //Class tracks the robot's position, orientation, and velocity.

    GoBildaPinpointDriver pinpoint;
    //The odometry sensor is named "pinpoint".


    public void init(HardwareMap hardwareMap, double[] odometryParameters) {
        double xStartInches = odometryParameters[0];
        double yStartInches = odometryParameters[1];
        double headingStartDegrees = odometryParameters[2];
        //xStartIcnehs = The robot's starting position in inches.
        //yStarticnehs = The robot's starting position in inches.
        //headingStartDegeres = The robot's starting orientation in degrees.
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        //Configure the odometry sensor as "pinpoint".

        //Tracking Point at Center of Robot.
        double xOffset = 0;
        double yOffset = -2.5;
        //xOffset = Sensor tracking forward/backword movement's distance in inches to the left from the center.
        //yOffset = Sensor tracking left/right movement's distance in inches to the front from the center.

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.INCH);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        //Setting Basic Information for Sensor.

        pinpoint.resetPosAndIMU();
        pinpoint.recalibrateIMU();
        //Requires robot to be completely stationary for 0.25 seconds.
        //IMPORTANT: MAKE SURE THE ROBOT IS COMPLETELY STATIONARY FOR 0.25 SECONDS WHEN INIT IS RUN.




        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, xStartInches, yStartInches, AngleUnit.DEGREES, headingStartDegrees));
        //Setting Robot's Starting Position and Orientation.

    }


    public double[] getRobotPosition() {
        //Returns in Inches

        pinpoint.update();

        double x = pinpoint.getPosX(DistanceUnit.INCH);
        double y = pinpoint.getPosY(DistanceUnit.INCH);

        // FUNCTIONALITY:
        // uses odometry sensor to get the robot's current position
        // return [x, y]

        // CLASS INTERACTIONS:
        // N/A
        return new double[] {x, y};
    }

    public double getRobotAngle() {
        //Returns in Degrees

        pinpoint.update();

        // FUNCTIONALITY:
        // uses odometry sensor to get the robot's current facing angle
        // return theta

        // CLASS INTERACTIONS:
        // N/A
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }

    public double[] getRobotVelocity() {
        pinpoint.update();
        //FUNCTIONALITY:
        //uses pinpoint to get robot x vel, y vel, and rotational vel
        //units inches per second/degrees per second
        //return 3 things (xvel, yvel, headingvel)

        //class interactions; None
        double xVel = pinpoint.getVelX(DistanceUnit.INCH);
        double yVel = pinpoint.getVelY(DistanceUnit.INCH);
        double headingVel = pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
        return new double[] {xVel, yVel, headingVel};
    }
}
