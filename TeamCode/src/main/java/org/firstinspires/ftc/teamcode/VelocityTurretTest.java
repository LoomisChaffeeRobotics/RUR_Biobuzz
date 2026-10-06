package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class VelocityTurretTest extends OpMode {

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

    double velocityNumber = 0;

    DcMotorEx launcher;

    @Override
    public void init() {
        // in init(),
        // get intake hardware (hardware.get()),
        // in loop(),
        // make so that: pressing A key -> toggleIntake,
        // with correct parameters passed in

//        launcherClass = new Launcher();
//        launcherClass.init(hardwareMap);

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");


    }

    @Override
    public void loop() {
        //launcher handling
        // pollen launcher

//        launcherClass.toggleLauncher(1,
//                new double[] {velocityNumber},
//                launcherClass.globalEpsilon,
//                true);

        launcher.setVelocity(velocityNumber);

        if (gamepad2.a) {
            velocityNumber += 10;
        }

        telemetry.addData("targetVelocity", velocityNumber);
        telemetry.addData("realVelocity", launcher.getVelocity());


    }

}
