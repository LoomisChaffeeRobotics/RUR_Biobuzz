package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class OdometryTesting extends OpMode {


    Odometry odometry;
    @Override
    public void init() {
        odometry = new Odometry();
        odometry.init(hardwareMap, new double[] {0,0,0});

    }

    @Override
    public void loop() {
        telemetry.addData("Robot X: ", odometry.getRobotPosition()[0]);
        telemetry.addData("Robot Y: ", odometry.getRobotPosition()[1]);
        telemetry.addData("Robot Heading: ", odometry.getRobotAngle());
        telemetry.addData("robot vel x: ", odometry.getRobotVelocity()[0]);
        telemetry.addData("robot vel y: ", odometry.getRobotVelocity()[1]);
        telemetry.addData("robot vel heading: ", odometry.getRobotVelocity()[2]);
        telemetry.update();

    }
}
