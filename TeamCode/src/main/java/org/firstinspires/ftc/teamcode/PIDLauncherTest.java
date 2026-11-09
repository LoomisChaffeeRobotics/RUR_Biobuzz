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
    public static double launcherP = 71;
    public static double launcherI = 5.1;
    public static double launcherD = 17;
    public static double launcherF = -2.9;
    public double appliedP = 0;
    public double appliedI = 0;
    public double appliedD = 0;
    public double appliedF = 0;

    //this class can be used for either nectar or pollen launcher, just change the first parameter of toggleLauncher to 1 for pollen and 2 for nectar.
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry telemetry2 = dashboard.getTelemetry();
    boolean startPressed = false;
    double velocityNumber = 0;
    DcMotorEx nectar_launcher;

    @Override
    public void init() {
        nectar_launcher = hardwareMap.get(DcMotorEx.class, "nectar_launcher");
        nectar_launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        updatePidfCoefficients();
    }

    @Override
    public void loop() {

        updatePidfCoefficients();



        if (startPressed) {
            velocityNumber = -1300;
        } else {
            velocityNumber = 0;
        }
       nectar_launcher.setVelocity(velocityNumber);


        if (gamepad1.startWasPressed()) {
            startPressed = !startPressed;
        }

        if (gamepad1.aWasPressed()) {
            launcherP += 1;
        }
        if (gamepad1.yWasPressed()) {
            launcherP -= 1;
        }
        if (gamepad1.bWasPressed()) {
            launcherI += 0.1;
        }
        if (gamepad1.xWasPressed()) {
            launcherI -= 0.1;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            launcherD -= 0.1;
        }
        if (gamepad1.dpadRightWasPressed()) {
            launcherD += 0.1;
        }
        if (gamepad1.dpadUpWasReleased()) {
            launcherF += 0.1;
        } 
        if (gamepad1.dpadDownWasReleased()) {
            launcherF -= 0.1;
        }

        telemetry2.addData("pidf cooeficients", nectar_launcher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER));
        telemetry2.addData("Target Velocity", velocityNumber);
        telemetry2.addData("Real Velocity", nectar_launcher.getVelocity());
        telemetry2.addData("Motor PIDF",
                nectar_launcher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER));

        telemetry2.update();
        telemetry.addData("pidf cooeficients", nectar_launcher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER));
        telemetry.addData("Target Velocity", velocityNumber);
        telemetry.addData("Real Velocity", nectar_launcher.getVelocity());

        telemetry.addData("PIDLauncherTest p", launcherP);
        telemetry.addData("PIDLauncherTest i", launcherI);
        telemetry.addData("PIDLauncherTest d", launcherD);
        telemetry.addData("PIDLauncherTest f", launcherF);
        telemetry.addData("Applied PIDF", "%.4f, %.4f, %.4f, %.4f",
                appliedP, appliedI, appliedD, appliedF);
        telemetry.addData("Motor PIDF",
                nectar_launcher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER));

        telemetry.update();
    }
    public void updatePidfCoefficients() {
        if (Double.compare(launcherP, appliedP) == 0
                && Double.compare(launcherI, appliedI) == 0
                && Double.compare(launcherD, appliedD) == 0
                && Double.compare(launcherF, appliedF) == 0) {
            return;
        }

        nectar_launcher.setVelocityPIDFCoefficients(launcherP, launcherI, launcherD, launcherF);
        appliedP = launcherP;
        appliedI = launcherI;
        appliedD = launcherD;
        appliedF = launcherF;
    }
}
