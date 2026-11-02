package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
@Config
public class PIDLauncherTest extends OpMode {
    public static double p = 20;
    public static double i = 1;
    public static double d = 0.5;
    public static double f = 0.5;

    //this class can be used for either nectar or pollen launcher, just change the first parameter of toggleLauncher to 1 for pollen and 2 for nectar.
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry telemetry2 = dashboard.getTelemetry();
    boolean x1Pressed = false;
    double velocityNumber = 0;
    DcMotorEx pollen_launcher;

    @Override
    public void init() {
        pollen_launcher = hardwareMap.get(DcMotorEx.class, "pollen_launcher");
        pollen_launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        pollen_launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));


        if (x1Pressed) {
            velocityNumber = -1300;
        } else {
            velocityNumber = 0;
        }

       pollen_launcher.setVelocity(velocityNumber);


        if (gamepad1.xWasPressed()) {
            x1Pressed = !x1Pressed;
        }

        telemetry2.addData("Target Velocity", velocityNumber);
        telemetry2.addData("Real Velocity", pollen_launcher.getVelocity());
        telemetry2.update();
    }
}
