package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class TurretVelocityRealignment {
    //Class determines the target angle to turn the turret and turns the turret.

    HardwareMap hardwareMap;
    double[] getVelocityDirectionalOffset() {
        // FUNCTIONALITY:
        // gets the robots velocity vector
        // return [x, y]

        // CLASS INTERACTIONS:
        // need Odometry class to find the velocity
        return new double[] {0.0, 0.0};
    }

    double[] getAdjustedShootingInfo(double velocityOffset, int aprilTagID) {
        // FUNCTIONALITY:
        // uses the robot's velocity vector ( from getVelocityDirectionalOffset() ),
        // also uses the position of the robot, AND the position of the aprilTag to find new angle and distance
        // the math:
            // a = the original vector between the robot position and apriltag position
            // b = a + (velocity * dt) (where dt = estimated change in time elapsed from starting shoot cycle and actually shooting)
                // b_x = a_x + V*dt*cos(theta); b_y = a_y + V*dt*sin(theta);
            // D (a.k.a. new distance vector) = vector between new position and apriltag position
                // new distance = |D|
            // from this, you can get that angle = Math.atan2(D_y, D_x), which takes the arctangent
        // return new [distance, angle]

        //CLASS INTERACTIONS:
        // need Camera class to get apriltag position
        // (optional) might need Launcher class to find dt, unless it's a hardcoded value
        return new double[] {0.0, 0.0};
    }

}
