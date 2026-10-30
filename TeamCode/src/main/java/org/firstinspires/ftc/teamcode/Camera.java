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
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.openftc.apriltag.AprilTagPose;

import java.util.HashMap;
import java.util.Map;

public class Camera {
    //Class detects April Tags, finds robot's relative position, and determines which hive to shoot at. (team/tilt direction)
    public Limelight3A limelight;
//    Odometry odometry;
    HardwareMap hardwareMap;
    public double camera_height = 15.5;
    public double target_height = 48;

    public double camera_mounted_angle = 30;
    public double height_from_apriltag = target_height - camera_height;

    public int[] red_cluster_list = {30, 31, 32, 33, 34, 35, 36, 37};
    public int[] blue_cluster_list = {38, 39, 40, 41, 42, 43, 44, 45};

    public double[] lastPos = new double[] {0, 0, 0};

    public void init(HardwareMap hardwareMap, double[] odometryParameters) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
//        odometry = new Odometry();
//        odometry.init(hardwareMap, odometryParameters);
    }

    public double getDistanceFromAprilTag(int[] cluster_list) {
//        double angleToGoalDegrees = camera_mounted_angle;
//        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
//        double distance_from_apriltag = (target_height - camera_height) / Math.sin(angleToGoalRadians);

        double[] pos = getAprilTagPosition(cluster_list);


        if (Double.isNaN((pos[0] * pos[0]) + (pos[1] * pos[1]) + (pos[2] * pos[2])) || ((pos[0] * pos[0]) + (pos[1] * pos[1]) + (pos[2] * pos[2])) == 0) {
            pos = lastPos;
            return Math.sqrt((pos[0] * pos[0]) + (pos[1] * pos[1]) + (pos[2] * pos[2]));
        }
        else {
            lastPos = pos;
        }

        double distance_from_apriltag = Math.sqrt((pos[0] * pos[0]) + (pos[1] * pos[1]) + (pos[2] * pos[2]));

        return distance_from_apriltag;
    }

    public double[] getAprilTagPosition(int[] cluster_list) {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) return new double[]{0, 0, 0};

        for (LLResultTypes.FiducialResult fr : result.getFiducialResults()) {

            for (int id : cluster_list) {

                if (fr.getFiducialId() == id) {
                    // is it relative to robot or for the entire space??????
                    Position p = fr.getTargetPoseCameraSpace().getPosition().toUnit(DistanceUnit.INCH);
                    return new double[]{p.x, p.y, p.z};
                }

            }
        }
        return new double[]{0, 0, 0};
    }
}


// FUNCTIONALITY:
// needs to use Limelight camera sensor to get position of a specific aprilTag
// use aprilTagID to identify which april tag you are getting the distance from
// returns the April tag position [x, y, z]

