package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.pollenPVSTest;

/**
 * Dashboard: tune kP/kV/kS, ticksPerSecondPerRPM, threshold and tolerance under
 * pollenPVSTest. Target controls below are live under PollenPVSTuner.
 * Begin with kP=0 and kS=0, tune kV to hold speed, then kS and a small kP.
 * Bang-bang may cycle until feedforward holds speed inside the 100 RPM band.
 */
@Config
@TeleOp(name = "Pollen PVS Tuner", group = "Tests")
public class PollenPVSTuner extends OpMode {
    public static boolean useDistanceTable = false;
    public static double targetRPM = 3000;
    public static double distance = 30;
    public static double rpmStep = 50;
    public static double distanceStep = 0.25;

    private final pollenPVSTest launcher = new pollenPVSTest();
    private boolean enabled;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        launcher.init(hardwareMap);
        telemetry.addLine("A: toggle | B: stop | hold Y: feed when ready");
        telemetry.addLine("X: RPM/table mode | D-pad up/down: adjust RPM or distance");
        telemetry.addLine("Dashboard gains: pollenPVSTest; targets: PollenPVSTuner");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) enabled = !enabled;
        if (gamepad1.bWasPressed()) enabled = false;
        if (gamepad1.xWasPressed()) useDistanceTable = !useDistanceTable;
        int step = (gamepad1.dpadUpWasPressed() ? 1 : 0)
                - (gamepad1.dpadDownWasPressed() ? 1 : 0);
        if (useDistanceTable) distance += step * distanceStep;
        else targetRPM = Math.max(0, targetRPM + step * rpmStep);

        if (useDistanceTable) launcher.setTargetFromDistance(distance);
        else launcher.setTargetRPM(targetRPM);
        launcher.toggleLauncher(enabled);
        launcher.feedLauncher(gamepad1.y);
        launcher.update();

        telemetry.addData("Enabled", enabled);
        telemetry.addData("Target source", useDistanceTable ? "Distance table" : "Direct RPM");
        telemetry.addData("Distance", distance);
        telemetry.addData("Controller", launcher.getControlMode());
        telemetry.addData("Target RPM", launcher.getTargetRPM());
        telemetry.addData("Measured RPM", launcher.getMeasuredRPM());
        telemetry.addData("Error RPM", launcher.getErrorRPM());
        telemetry.addData("Motor power", launcher.getMotorPower());
        telemetry.addData("Ready", launcher.isLauncherPowered());
        telemetry.addData("Feeder power", launcher.turret_feeder.getPower());
        telemetry.addData("kP", pollenPVSTest.kP);
        telemetry.addData("kV", pollenPVSTest.kV);
        telemetry.addData("kS", pollenPVSTest.kS);
        telemetry.addData("Switch threshold RPM", pollenPVSTest.bangBangThresholdRPM);
        telemetry.addData("Ticks/sec per RPM", pollenPVSTest.ticksPerSecondPerRPM);
        telemetry.update();
    }

    @Override
    public void stop() {
        enabled = false;
        launcher.stop();
    }
}
