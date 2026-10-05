package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class PollenLauncherTest extends OpMode {

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

        camera = new Camera();
        camera.init(hardwareMap, odometryConstants);

        odometry = new Odometry();
        odometry.init(hardwareMap, odometryConstants);
    }

    @Override
    public void loop() {
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

        result = launcherClass.interpolateLauncherPowerFromTable(1,
                launcherClass.pollen_distance_list,
                launcherClass.pollen_power_list,
                camera.getDistanceFromAprilTag(),
                launcherClass.globalDelta);

        if (gamepad2.aWasPressed()) {
            a2Pressed = !a2Pressed;
        }

        telemetry.addData("targetVelocity", result[0]);

//        telemetry.addData("velocityError", result[0]);
        telemetry.addData("power_1", result[1]);
        telemetry.addData("power_2", result[2]);


    }
}
