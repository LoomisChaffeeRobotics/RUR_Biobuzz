package org.firstinspires.ftc.teamcode;

public class Camera {
    //Class detects April Tags, finds robot's relative position, and determines which hive to shoot at. (team/tilt direction)

    double getDistance(int aprilTagID) {
        // FUNCTIONALITY:
        // needs to use Limelight camera sensor to get distance from a specific AprilTag
        // use aprilTagID to identify which april tag you are getting the distance from
        // returns the distance

        // CLASS INTERACTIONS:
        // N/A
        return 0;
    }

    double[] getPosition(int aprilTagID) {
        // FUNCTIONALITY:
        // needs to use Limelight camera sensor to get position of a specific aprilTag
        // use aprilTagID to identify which april tag you are getting the distance from
        // returns the position [x, y]

        // CLASS INTERACTIONS:
        // N/A
        return new double[] {0.0, 0.0};
    }

}
