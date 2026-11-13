package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Main extends OpMode {
    //Main OpMode run in TeleOp.

    Intake intake;
    Drive drive;
    Launcher launcherClass;
    Camera camera;
    Odometry odometry;

    public double[] odometryConstants = {0, 0, 0};
    double distanceToAprilTag = 0;
    int targetAprilTagID = 1;

    boolean a1Pressed = false;
    boolean b1Pressed = false;
    boolean x1Pressed = false;

    boolean isRed = true;

    @Override
    public void init() {
        // in init(),
        // get intake hardware (hardware.get()),
        // in loop(),
        // make so that: pressing A key -> toggleIntake,
        // with correct parameters passed in


        drive = new Drive();
        drive.init(hardwareMap);

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
        if (gamepad1.options) {
            intake.intakeMotor.setPower(-1);
            launcherClass.nectar_launcher.setPower(-1);
            launcherClass.pollen_launcher.setPower(-1);
            launcherClass.nectar_servo.setPower(-1);
            launcherClass.pollen_servo.setPower(-1);
            launcherClass.turret_feeder.setPower(-1);
        }
        camera.loopsSinceCheck = (camera.loopsSinceCheck + 1) % 10;
        //only checks limelight every 10 loops

        //make sure to differentiate by color/team!!!!!!
        if (gamepad1.startWasPressed()) {
            odometry.resetYaw();
        }

        double projection_distance = Math.sqrt((camera.getDistanceFromAprilTag((isRed ? camera.red_cluster_list : camera.blue_cluster_list)) * camera.getDistanceFromAprilTag((isRed ? camera.red_cluster_list : camera.blue_cluster_list)) ) - (camera.height_from_apriltag * camera.height_from_apriltag));

        //            launcherClass.toggleLauncher(1,
        //                launcherClass.interpolateLauncherPowerFromTable(1,
        //                        launcherClass.pollen_distance_list,
        //                        launcherClass.pollen_power_list,
        //                        projection_distance,
        //                        launcherClass.globalDelta),
        //                launcherClass.globalEpsilon,
        //                a1Pressed);
        intake.toggleIntake(gamepad1.left_trigger_pressed);


        drive.driveUpdateFromGamepadInput((double) gamepad1.left_stick_x, (double) -gamepad1.left_stick_y, (double) gamepad1.right_stick_x, odometry.getRobotAngle());



        //launcher handling
        // pollen launcher

        launcherClass.toggleLauncher(1,
                launcherClass.interpolateLauncherPowerFromTable(1,
                        launcherClass.pollen_distance_list,
                        launcherClass.pollen_power_list,
                        projection_distance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                a1Pressed);

        //nectar launcher
        launcherClass.toggleLauncher(2,
                launcherClass.interpolateLauncherPowerFromTable(2,
                        launcherClass.nectar_distance_list,
                        launcherClass.nectar_power_list,
                        projection_distance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                b1Pressed);

        if (gamepad1.aWasPressed()) {
            a1Pressed = !a1Pressed;
        }
        if (gamepad1.bWasPressed()) {
            b1Pressed = !b1Pressed;
        }
        if (gamepad1.x) {

            telemetry.addData("turning angle:", drive.turnToHive((isRed ? 1 : 0), odometry.getRobotAngle()));
        }

        telemetry.addData("current camera change angle:", camera.getAprilTagAngle(isRed ? camera.red_cluster_list : camera.blue_cluster_list));
        telemetry.addData("robot angle", odometry.getRobotAngle());
        if (gamepad1.right_bumper) {
            launcherClass.feederPower += 0.001;
        }
        else if (gamepad1.left_bumper) {
            launcherClass.feederPower -= 0.001;
        }

        //feeder for turrets
        launcherClass.feedLauncher(launcherClass.targetRPMList, launcherClass.globalEpsilon, gamepad1.y);

        //selecting team
        if (gamepad1.dpadUpWasPressed()){
            isRed = true;
        } else if (gamepad1.dpadDownWasPressed()){
            isRed = false;
        }



        telemetry.addData("Team: ", (isRed ? "Red" : "Blue"));
        telemetry.addData("Feeder Power: ", launcherClass.feederPower);
        telemetry.update();

    }
    
    
}