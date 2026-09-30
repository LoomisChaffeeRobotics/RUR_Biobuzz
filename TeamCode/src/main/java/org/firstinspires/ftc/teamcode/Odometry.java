package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


public class Odometry extends OpMode {
    //Class tracks the robot's position, orientation, and velocity.

    GoBildaPinpointDriver pinpoint;

    @Override
    public void init() {

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        //Tracking Point at Center of Robot
        double xOffset = 0;
        double yOffset = 0;
        //xOffset = Sensor tracking forward/backword movement's distance in inches to the left
        //yOffset = Sensor tracking left/right movement's distance in inches to the front

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.INCH);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();
        pinpoint.recalibrateIMU();
        //Requires robot to be completely stationary for 0.25 seconds

        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));

    }

    @Override
    public void loop() {

        telemetry.addData("Odometry Status", pinpoint.getDeviceStatus());

    }

    public double[] getRobotPosition() {

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

        pinpoint.update();

        // FUNCTIONALITY:
        // uses odometry sensor to get the robot's current facing angle
        // return theta
        //^ Is theta in degrees or radians? - Brendan

        // CLASS INTERACTIONS:
        // N/A
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }
}
