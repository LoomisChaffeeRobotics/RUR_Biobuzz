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
    public static volatile double p = 20;
    public static volatile double i = 1;
    public static volatile double d = 0.5;
    public static volatile double f = 0.5;
    public double appliedP = Double.NaN;
    public double appliedI = Double.NaN;
    public double appliedD = Double.NaN;
    public double appliedF = Double.NaN;

    //this class can be used for either nectar or pollen launcher, just change the first parameter of toggleLauncher to 1 for pollen and 2 for nectar.
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry telemetry2 = dashboard.getTelemetry();
    boolean x1Pressed = false;
    double velocityNumber = 0;
    DcMotorEx pollen_launcher;

    @Override
    public void init() {
        pollen_launcher = hardwareMap.get(DcMotorEx.class, "pollen_launcher");
        pollen_launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        updatePidfCoefficients();
    }

    @Override
    public void loop() {

        updatePidfCoefficients();



        if (x1Pressed) {
            velocityNumber = -1300;
        } else {
            velocityNumber = 0;
        }
       pollen_launcher.setVelocity(velocityNumber);


        if (gamepad1.xWasPressed()) {
            x1Pressed = !x1Pressed;
        }

        telemetry2.addData("pidf cooeficients", pollen_launcher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER));
        telemetry2.addData("Target Velocity", velocityNumber);
        telemetry2.addData("Real Velocity", pollen_launcher.getVelocity());
        telemetry2.update();
    }
    public void updatePidfCoefficients() {
        if (Double.compare(p, appliedP) == 0
                && Double.compare(i, appliedI) == 0
                && Double.compare(d, appliedD) == 0
                && Double.compare(f, appliedF) == 0) {
            return;
        }

        pollen_launcher.setVelocityPIDFCoefficients(p, i, d, f);
        appliedP = p;
        appliedI = i;
        appliedD = d;
        appliedF = f;
    }
}
