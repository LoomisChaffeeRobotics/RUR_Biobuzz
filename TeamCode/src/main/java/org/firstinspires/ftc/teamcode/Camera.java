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
    Odometry odometry;
    HardwareMap hardwareMap;

    public void init(HardwareMap hardwareMap, double[] odometryParameters) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        odometry = new Odometry();
        odometry.init(hardwareMap, odometryParameters);
    }

    public double getDistanceFromAprilTag() {
        double camera_mounted_angle = 0;
        double camera_height = 0;
        double target_height = 0;
        double angleToGoalDegrees = odometry.getRobotAngle();
        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
        double distance_from_apriltag = (target_height - camera_height) / Math.tan(angleToGoalRadians);

        return distance_from_apriltag;
    }

    public double[] getAprilTagPosition(int aprilTagID) {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) return null;

        for (LLResultTypes.FiducialResult fr : result.getFiducialResults()) {
            if (fr.getFiducialId() == aprilTagID) {
                // is it relative to robot or for the entire space??????
                Position p = fr.getTargetPoseCameraSpace().getPosition().toUnit(DistanceUnit.METER);
                return new double[]{p.x, p.y, p.z};
            }
        }
        return null;
    }
}


// FUNCTIONALITY:
// needs to use Limelight camera sensor to get position of a specific aprilTag
// use aprilTagID to identify which april tag you are getting the distance from
// returns the April tag position [x, y, z]

