package org.firstinspires.ftc.teamcode.teleops;

import org.firstinspires.ftc.teamcode.subsystems.Camera;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.IndicatorLight;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Odometry;

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
    //IndicatorLight light;

    public double[] odometryConstants = {0, 0, 0};
    double distanceToAprilTag = 0;
    int targetAprilTagID = 1;

    boolean a2Pressed = false;
    boolean b2Pressed = false;
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

        //light = new IndicatorLight();
        //light.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.left_trigger_pressed || gamepad2.left_trigger_pressed) {
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
        //                a2Pressed);
        intake.toggleIntake(gamepad1.right_trigger_pressed || gamepad2.right_trigger_pressed);


        drive.driveUpdateFromGamepadInput(
                (double) gamepad1.left_stick_x,
                (double) -gamepad1.left_stick_y,
                (gamepad1.x ? drive.turnToHive(isRed ? 1 : 0) : (double) gamepad1.right_stick_x),
                odometry.getRobotAngle());



        //launcher handling
        // pollen launcher

        launcherClass.toggleLauncher(1,
                launcherClass.interpolateLauncherPowerFromTable(1,
                        launcherClass.pollen_distance_list,
                        launcherClass.pollen_power_list,
                        projection_distance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                a2Pressed);

        //nectar launcher
        launcherClass.toggleLauncher(2,
                launcherClass.interpolateLauncherPowerFromTable(2,
                        launcherClass.nectar_distance_list,
                        launcherClass.nectar_power_list,
                        projection_distance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                b2Pressed);

        if (gamepad2.aWasPressed()) {
            a2Pressed = !a2Pressed;
        }
        if (gamepad2.bWasPressed()) {
            b2Pressed = !b2Pressed;
        }


        telemetry.addData("current camera change angle:", camera.getAprilTagAngle(isRed ? camera.red_cluster_list : camera.blue_cluster_list));
        telemetry.addData("robot angle", odometry.getRobotAngle());
        if (gamepad2.right_bumper) {
            launcherClass.feederPower += 0.001;
        }
        else if (gamepad2.left_bumper) {
            launcherClass.feederPower -= 0.001;
        }

        //feeder for turrets
        launcherClass.feedLauncher(launcherClass.targetRPMList, launcherClass.globalEpsilon, gamepad2.y);

        //selecting team
        if (gamepad2.dpadUpWasPressed()){
            isRed = true;
        } else if (gamepad2.dpadDownWasPressed()){
            isRed = false;
        }


        //indicator light
//        if (launcherClass.isLauncherPowered(1, launcherClass.targetRPMList[0], launcherClass.globalEpsilon)){
//            light.setColor(IndicatorLight.Color.YELLOW);
//        } else if (launcherClass.isLauncherPowered(2, launcherClass.targetRPMList[1], launcherClass.globalEpsilon)){
//            light.setColor(isRed ? IndicatorLight.Color.RED : IndicatorLight.Color.BLUE);
//        } else {
//            light.setColor(IndicatorLight.Color.OFF);
//        }


        telemetry.addData("Feeder Power: ", launcherClass.feederPower);


        telemetry.addLine();
        telemetry.addLine("Drive Coach Information:");

        telemetry.addData("Team: ", (isRed ? "Red" : "Blue"));
        telemetry.addData("Pollen Turret Up to Speed: ", launcherClass.isLauncherPowered(1, launcherClass.targetRPMList[0], launcherClass.globalEpsilon));
        telemetry.addData("Nectar Turret Up to Speed: ", launcherClass.isLauncherPowered(2, launcherClass.targetRPMList[1], launcherClass.globalEpsilon));
        telemetry.addData("Angle Offset: ", camera.getAprilTagAngle((isRed ? camera.red_cluster_list : camera.blue_cluster_list)));

        telemetry.update();

    }
    
}