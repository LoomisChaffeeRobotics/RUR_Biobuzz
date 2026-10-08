package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class TurretVelocityRealignment {
    //Class determines the target angle to turn the turret and turns the turret.

    Odometry odometryClass;
    Camera cameraClass;
    public void init(HardwareMap hardwareMap, double[] odometryParameters) {
        odometryClass = new Odometry();
        odometryClass.init(hardwareMap, odometryParameters);
        cameraClass = new Camera();
        cameraClass.init(hardwareMap, odometryParameters);
    }
    double[] getVelocityDirectionalOffset() {
        // FUNCTIONALITY:
        // gets the robots velocity vector
        // return [x, y]
        return new double[]{odometryClass.getRobotVelocity()[0], odometryClass.getRobotVelocity()[1]};
        // CLASS INTERACTIONS:
        // need Odometry class to find the velocity
    }

    double[] getAdjustedShootingInfo(int aprilTagID) {

        double[] aprilTagPosition = cameraClass.getAprilTagPosition(aprilTagID);

        double[] robotPosition = odometryClass.getRobotPosition();
        double[] robotVelocity = getVelocityDirectionalOffset();

        double[] initial_displacement = new double[]{aprilTagPosition[0] - robotPosition[0], aprilTagPosition[1] - robotPosition[1]};
        double dt = 0.5; // estimated time delay in seconds (this should be configurable)
        double[] velocity_displacement = new double[]{robotVelocity[0] * dt, robotVelocity[1] * dt};

        double[] final_displacement = new double[]{initial_displacement[0] + velocity_displacement[0], initial_displacement[1] + velocity_displacement[1]};

        double new_distance = Math.sqrt(final_displacement[0] * final_displacement[0] + final_displacement[1] * final_displacement[1]);
        double new_angle = Math.atan2(final_displacement[1], final_displacement[0]);
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
        return new double[] {new_distance, new_angle};
    }

}
