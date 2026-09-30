package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


public class Odometry extends OpMode {
    //Class tracks the robot's position, orientation, and velocity.

    GoBildaPinpointDriver pinpoint;
    //The odometry sensor is named "pinpoint".

    @Override
    public void init() {

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        //Configure the odometry sensor as "pinpoint".

        //Tracking Point at Center of Robot.
        double xOffset = 0;
        double yOffset = 0;
        //xOffset = Sensor tracking forward/backword movement's distance in inches to the left from the center.
        //yOffset = Sensor tracking left/right movement's distance in inches to the front from the center.

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.INCH);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        //Setting Basic Information for Sensor.

        pinpoint.resetPosAndIMU();
        pinpoint.recalibrateIMU();
        //Requires robot to be completely stationary for 0.25 seconds.
        //IMPORTANT: MAKE SURE THE ROBOT IS COMPLETELY STATIONARY FOR 0.25 SECONDS WHEN INIT IS RUN.

        double xStart = 0;
        double yStart = 0;
        double headingStart = 0;
        //xStart = The robot's starting position in inches.
        //yStart = The robot's starting position in inches.
        //headingStart = The robot's starting orientation in degrees.

        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, xStart, yStart, AngleUnit.DEGREES, headingStart));
        //Setting Robot's Starting Position and Orientation.

    }

    @Override
    public void loop() {

        telemetry.addData("Odometry Status", pinpoint.getDeviceStatus());
        telemetry.update();
        //Adds the Odometry Sensor's Status to Odometry.

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
}
