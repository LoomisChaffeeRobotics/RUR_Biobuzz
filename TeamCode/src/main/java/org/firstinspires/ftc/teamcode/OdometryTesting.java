package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;

@TeleOp
public class OdometryTesting extends OpMode {


    Odometry odometry;
    Drive drive;
    @Override
    public void init() {
        odometry = new Odometry();
        odometry.init(hardwareMap, new double[] {0,0,0});
        drive = new Drive();
        drive.init(hardwareMap);


    }

    @Override
    public void loop() {
        drive.driveTankDrive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        telemetry.addData("Robot X: ", odometry.getRobotPosition()[0]);
        telemetry.addData("Robot Y: ", odometry.getRobotPosition()[1]);
        telemetry.addData("Robot Heading: ", odometry.getRobotAngle());
        telemetry.addData("robot vel x: ", odometry.getRobotVelocity()[0]);
        telemetry.addData("robot vel y: ", odometry.getRobotVelocity()[1]);
        telemetry.addData("robot vel heading: ", odometry.getRobotVelocity()[2]);
        telemetry.update();

    }
}
