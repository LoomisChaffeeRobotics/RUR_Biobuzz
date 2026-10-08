package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class IntakeTesting extends OpMode {


    Drive driveClass;
    Intake intakeClass;
    Odometry odometry;
    @Override
    public void init() {
        driveClass = new Drive();
        driveClass.init(hardwareMap);
        intakeClass = new Intake();
        intakeClass.init(hardwareMap);
        odometry = new Odometry();
        odometry.init(hardwareMap, new double[] {0,0,0});

    }

    @Override
    public void loop() {
        driveClass.driveUpdateFromGamepadInput(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, (float) Math.toRadians(odometry.getRobotAngle()));
        if (gamepad1.a) {
            intakeClass.intakeMotor.setPower(1);
        } else if (gamepad1.y) {
            intakeClass.intakeMotor.setPower(-1);
        } else {
            intakeClass.intakeMotor.setPower(0);
        }
        telemetry.addData("heading in degtrerrees", odometry.getRobotAngle());
        telemetry.update();

    }
}
