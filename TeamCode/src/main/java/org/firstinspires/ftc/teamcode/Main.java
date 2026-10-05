package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class Main extends OpMode {
    //Main OpMode run in TeleOp.

    Intake intake;
    Drive drive;
    Launcher launcherClass;
    Camera camera;
    Odometry odometry;

    double[] odometryConstants = {0, 0, 0};
    double distanceToAprilTag = 0;
    int targetAprilTagID = 1;

    boolean a2Pressed = false;
    boolean b2Pressed = false;

    @Override
    public void init() {
        // in init(),
        // get intake hardware (hardware.get()),
        // in loop(),
        // make so that: pressing A key -> toggleIntake,
        // with correct parameters passed in

        intake = new Intake();
        intake.init(hardwareMap);

        launcherClass = new Launcher();
        launcherClass.init(hardwareMap);

        camera = new Camera();
        camera.init(hardwareMap, odometryConstants);

        odometry = new Odometry();
        odometry.init(hardwareMap, odometryConstants);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            intake.toggleIntake(true);
        } else {
            intake.toggleIntake(false);
        }

        drive.driveUpdateFromGamepadInput((double) gamepad1.left_stick_x, (double) gamepad1.left_stick_y, (double) gamepad1.right_stick_y, (float) odometry.getRobotAngle());


        //launcher handling
        // pollen launcher

        launcherClass.toggleLauncher(1,
                launcherClass.interpolateLauncherPowerFromTable(1,
                        launcherClass.pollen_distance_list,
                        launcherClass.pollen_power_list,
                        camera.getDistanceFromAprilTag(),
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                a2Pressed);

        //nectar launcher
        launcherClass.toggleLauncher(2,
                launcherClass.interpolateLauncherPowerFromTable(2,
                        launcherClass.pollen_distance_list,
                        launcherClass.pollen_power_list,
                        camera.getDistanceFromAprilTag(),
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                b2Pressed);

        if (gamepad2.aWasPressed()) {
            a2Pressed = !a2Pressed;
        }
        if (gamepad2.bWasPressed()) {
            b2Pressed = !b2Pressed;
        }


    }
    
    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;   // x position
        double y = -gamepad1.left_stick_y;   // y position (flipped: gamepad up = negative)
        double rx = gamepad1.right_stick_x;  // rotation

        float yaw = (float) odometry.getRobotAngle(); // radians

        drive.driveUpdateFromGamepadInput(x, y, rx, yaw);
    }
}