package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class NectarLauncherTest extends OpMode {

    Intake intake;
    Drive drive;
    Launcher launcherClass;
//    Odometry odometry;

    double[] odometryConstants = {0, 0, 0};
    double distance = 30;
    double distanceToAprilTag = 0;
    int targetAprilTagID = 1;

    boolean a2Pressed = false;
    boolean b1Pressed = false;

    double[] result;

    @Override
    public void init() {
        // in init(),
        // get intake hardware (hardware.get()),
        // in loop(),
        // make so that: pressing A key -> toggleIntake,
        // with correct parameters passed in

        launcherClass = new Launcher();
        launcherClass.init(hardwareMap);


//        odometry = new Odometry();
//        odometry.init(hardwareMap, odometryConstants);
    }

    @Override
    public void loop() {
        //launcher handling
        // pollen launcher

        //nectar launcher
        launcherClass.toggleLauncher(2,
                launcherClass.interpolateLauncherPowerFromTable(2,
                        launcherClass.nectar_distance_list,
                        launcherClass.nectar_power_list,
                        distance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                b1Pressed);

        result = launcherClass.interpolateLauncherPowerFromTable(2,
                launcherClass.nectar_distance_list,
                launcherClass.nectar_power_list,
                distance,
                launcherClass.globalDelta);


        if (gamepad1.bWasPressed()) {
            b1Pressed = !b1Pressed;
        }
        if (gamepad1.dpadUpWasReleased()) {
            distance += .25;
        }
        if (gamepad1.dpadDownWasReleased()) {
            distance -= .25;
        }

        telemetry.addData("targetVelocity", result[0]);
//        telemetry.addData("realvelocity", launcherClass.nectar_launcher.getVelocity());
        telemetry.addData("distance", distance);
        telemetry.addData("power_1", result[1]);
        telemetry.addData("power_2", result[2]);

        telemetry.update();


    }
}
