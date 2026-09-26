package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Odometry {
    //Class tracks the robot's position, orientation, and velocity.

    HardwareMap hardwareMap;
    double[] getRobotPosition() {
        // FUNCTIONALITY:
        // uses odometry sensor to get the robot's current position
        // return [x, y]

        // CLASS INTERACTIONS:
        // N/A
        return new double[] {0.0, 0.0};
    }

    double getRobotAngle() {
        // FUNCTIONALITY:
        // uses odometry sensor to get the robot's current facing angle
        // return theta

        // CLASS INTERACTIONS:
        // N/A
        return 0.0;
    }
}
