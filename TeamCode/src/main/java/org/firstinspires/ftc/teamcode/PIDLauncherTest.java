package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
@Config
public class PIDLauncherTest extends OpMode {

    //this class can be used for either nectar or pollen launcher, just change the first parameter of toggleLauncher to 1 for pollen and 2 for nectar.
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry telemetry2 = dashboard.getTelemetry();
    public static double targetDistance = 40;
    Launcher launcherClass;
    boolean x1Pressed = false;
    double velocityNumber = 0;

    @Override
    public void init() {
        launcherClass = new Launcher();
        launcherClass.init(hardwareMap);
    }

    @Override
    public void loop() {
        launcherClass.pollen_launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, Launcher.pollenLauncherPIDF);
        launcherClass.toggleLauncher(1,
                launcherClass.interpolateLauncherPowerFromTable(2,
                        launcherClass.nectar_distance_list,
                        launcherClass.nectar_power_list,
                        targetDistance,
                        launcherClass.globalDelta),
                launcherClass.globalEpsilon,
                x1Pressed);

        if (x1Pressed) {
            velocityNumber = launcherClass.interpolateLauncherPowerFromTable(2,
                    launcherClass.nectar_distance_list,
                    launcherClass.nectar_power_list,
                    targetDistance,
                    launcherClass.globalDelta)[0];
        } else {
            velocityNumber = 0;
        }

        if (gamepad1.xWasPressed()) {
            x1Pressed = !x1Pressed;
        }

        telemetry2.addData("Target Velocity", velocityNumber);
        telemetry2.addData("Real Velocity", launcherClass.pollen_launcher.getVelocity());
        telemetry2.update();
    }
}
