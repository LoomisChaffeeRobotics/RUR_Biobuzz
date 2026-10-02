package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class Main extends OpMode {
    //Main OpMode run in TeleOp.

    Intake intake;
    Launcher launcherClass;
    Camera camera;

    double distanceToAprilTag = 0;
    int targetAprilTagID = 1;
    double[] odometryConstants = {0, 0, 0};

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
    }

    @Override
    public void loop() {
        //intake handling
        if (gamepad1.a) {
            intake.toggleIntake(true);
        } else {
            intake.toggleIntake(false);
        }



        //launcher handling
        // pollen launcher
        if (gamepad2.a) {
            launcherClass.interpolateLauncherPowerFromTable(1, launcherClass.pollen_distance_list, launcherClass.pollen_power_list)
        }
    }
}