package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Camera {
    //Class detects April Tags, finds robot's relative position, and determines which hive to shoot at. (team/tilt direction)
    public Limelight3A limelight;
    Pose3D botpose;
    Position positionrelativetoapriltag;

    Odometry odometry;
    HardwareMap hardwareMap;
    public void init(HardwareMap hardwareMap, Telemetry telemetry, double[] odometryParameters) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);   // your AprilTag pipeline index
        limelight.start();
        odometry = new Odometry();
        odometry.init(hardwareMap, odometryParameters);
    }


//    public double getDistance_from_apriltag() {
//        //this code might be totally wrong but I'm just putting it in based on the most straightforward
//        // way to do it cause we didn't ahe the odemetry inputed yet so i didn't get it based on that orientation
//        double camera_mounted_angle = 0;
//        double camera_height = 0;
//        double target_height = 0;
////        double angleToGoalDegrees = botpose.getOrientation();
////        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
////        double distance_from_apriltag = (target_height - camera_height) / Math.tan(angleToGoalRadians);
//
////        return distance_from_apriltag;
//
//        //I'm just gonna move stuff from the last yrs ones in here but I still didn't get the odemtry part that would have to be figure out...
//    }

     //roboposeRelativeToAT requires odometry initial code was from SparkFun
//    public double getDistance_from_apriltag(boolean isBlue) {
//        //BLUEEEEEEEEE
//        positionrelativetoapriltag = new Position(DistanceUnit.METER, roboPoseRelativeToAT.x,roboPoseRelativeToAT.y, 0, 0);
//        distance_from_apriltag = Math.sqrt(Math.pow(positionrelativetoapriltag.x, 2)+Math.pow(positionrelativetoapriltag.y, 2));
//        return (distance_from_apriltag - 10)/39.37;
//    }

//    public double getDiagonalDistanceToAprilTag(int tagId) {
////        double[] robotPosition = odometry.getRobotPosition();
////        double[] pos = getPositionOfAprilTag(tagId);
////        if (pos == null) return -1; // tag not visible
//
////        return Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1] + pos[2] * pos[2]);
//    }

    public double[] getAprilTagPosition(int aprilTagID) {
            LLResult result = limelight.getLatestResult();
            if (result == null || !result.isValid()) return null;

            for (LLResultTypes.FiducialResult fr : result.getFiducialResults()) {
                if (fr.getFiducialId() == aprilTagID) {
                    Position p = fr.getTargetPoseCameraSpace().getPosition();
                    return new double[] {
                            p.toUnit(DistanceUnit.METER).x,
                            p.toUnit(DistanceUnit.METER).y,
                            p.toUnit(DistanceUnit.METER).z
                    };
                }
            }
            return null; // ther eis no tag
    }
    }

        // FUNCTIONALITY:
        // needs to use Limelight camera sensor to get position of a specific aprilTag
        // use aprilTagID to identify which april tag you are getting the distance from
        // returns the April tag position [x, y, z]

