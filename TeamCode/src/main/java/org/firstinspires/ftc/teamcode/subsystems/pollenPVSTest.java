package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Pollen-only experiment; call update() once per OpMode loop.
 * PVS formula follows BeepBot99/CodeBloodedDecodeV2, subsystems/Flywheel.java:
 * https://github.com/BeepBot99/CodeBloodedDecodeV2/blob/master/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Flywheel.java
 * Gains here use RPM, while that implementation uses encoder ticks/second.
 */
@Config
public class pollenPVSTest {
    // Initial estimates converted from the reference's tick-based gains at 28 ticks/rev.
    // Tune on this robot; these are not the existing Launcher's hub PIDF gains.
    public static double kP = 0.01 * 28.0 / 60.0;
    public static double kV = 0.000365 * 28.0 / 60.0;
    public static double kS = 0.065;
    // Encoder ticks per revolution of the shaft whose RPM is being controlled.
    // Assumes the existing Launcher's 28-tick comment and direct drive; verify gearing.
    public static double ticksPerRevolution = 28;
    public static double bangBangThresholdRPM = 100;
    public static double readyToleranceRPM = 40 * 60.0 / 28.0;
    public static double velocityOffsetTicksPerSecond = 75;
    public static double feederPower = 1;

    public DcMotorEx pollen_launcher;
    public DcMotorEx turret_feeder;
    public CRServo pollen_servo;

    // Original pollen calibration data: distances in the existing table's units,
    // velocities in encoder ticks/second (despite the historical "power" name).
    public final double[] pollen_distance_list = {30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74};
    public final double[] pollen_power_list = {1264, 1320, 1348, 1308, 1328, 1312, 1292, 1320, 1340, 1340, 1344, 1348, 1348, 1348, 1368, 1400, 1412, 1420, 1428, 1456, 1464, 1472, 1468};

    public enum ControlMode { OFF, BANG_BANG, PVS }
    private boolean enabled;
    private boolean feedRequested;
    private double targetRPM;
    private double measuredRPM;
    private double errorRPM;
    private double motorPower;
    private ControlMode mode = ControlMode.OFF;

    public void init(HardwareMap hardwareMap) {
        pollen_launcher = hardwareMap.get(DcMotorEx.class, "pollen_launcher");
        pollen_servo = hardwareMap.get(CRServo.class, "pollen_servo");
        turret_feeder = hardwareMap.get(DcMotorEx.class, "turret_feeder");
        // Software PVS controls power; do not stack the hub's velocity PID on top.
        pollen_launcher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pollen_launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        stop();
    }

    /** Direct RPM target, with no lookup-table offset added. */
    public void setTargetRPM(double rpm) {
        targetRPM = Double.isFinite(rpm) && rpm > 0 ? rpm : 0;
    }

    /** Original lookup and +75 ticks/second offset, converted to true RPM. */
    public void setTargetFromDistance(double distance) {
        if (!Double.isFinite(distance)) {
            setTargetRPM(0);
            return;
        }
        double[] result = interpolateLauncherPowerFromTable(distance);
        setTargetRPM(ticksPerSecondToRPM(result[0] + velocityOffsetTicksPerSecond));
    }

    /** Returns {velocity in ticks/second, lower index, upper index}. */
    public double[] interpolateLauncherPowerFromTable(double distance) {
        if (!Double.isFinite(distance)) return new double[] {0, 0, 0};
        int bottom = 0;
        int top = pollen_distance_list.length - 1;
        for (int i = 0; i < pollen_distance_list.length; i++) {
            if (pollen_distance_list[i] <= distance) bottom = i;
            if (pollen_distance_list[i] >= distance) {
                top = i;
                break;
            }
        }
        double velocity = pollen_power_list[bottom];
        if (bottom != top) {
            double fraction = (distance - pollen_distance_list[bottom])
                    / (pollen_distance_list[top] - pollen_distance_list[bottom]);
            velocity += fraction * (pollen_power_list[top] - velocity);
        }
        return new double[] {velocity, bottom, top};
    }

    public void toggleLauncher(boolean toggle) {
        enabled = toggle;
        if (!toggle) stop();
    }

    /** Request feeding; update() applies the speed interlock every loop. */
    public void feedLauncher(boolean toggle) {
        feedRequested = toggle;
    }

    public static double ticksPerSecondToRPM(double ticksPerSecond) {
        if (!Double.isFinite(ticksPerRevolution) || ticksPerRevolution <= 0) return Double.NaN;
        return ticksPerSecond * 60.0 / ticksPerRevolution;
    }

    public void update() {
        measuredRPM = ticksPerSecondToRPM(pollen_launcher.getVelocity());
        errorRPM = targetRPM - measuredRPM;
        boolean valid = Double.isFinite(measuredRPM) && Double.isFinite(errorRPM)
                && Double.isFinite(kP) && Double.isFinite(kV) && Double.isFinite(kS)
                && Double.isFinite(bangBangThresholdRPM) && bangBangThresholdRPM >= 0;
        if (!enabled || targetRPM <= 0 || !valid) {
            motorPower = 0;
            mode = ControlMode.OFF;
        } else if (Math.abs(errorRPM) > bangBangThresholdRPM) {
            mode = ControlMode.BANG_BANG;
            // Unidirectional bang-bang: accelerate below target, coast above it.
            motorPower = errorRPM > 0 ? 1 : 0;
        } else {
            mode = ControlMode.PVS;
            motorPower = clipPower(kP * errorRPM + kV * targetRPM + kS * Math.signum(targetRPM));
        }
        pollen_launcher.setPower(motorPower);
        pollen_servo.setPower(mode == ControlMode.OFF ? 0 : -1);
        turret_feeder.setPower(feedRequested && isLauncherPowered() ? clipPower(feederPower) : 0);
    }

    public boolean isLauncherPowered() {
        return enabled && mode != ControlMode.OFF && targetRPM > 0
                && Double.isFinite(readyToleranceRPM) && readyToleranceRPM >= 0
                && Math.abs(errorRPM) <= readyToleranceRPM;
    }

    private static double clipPower(double power) {
        return Double.isFinite(power) ? Math.max(0, Math.min(1, power)) : 0;
    }

    public void stop() {
        enabled = false;
        feedRequested = false;
        mode = ControlMode.OFF;
        motorPower = 0;
        if (pollen_launcher != null) pollen_launcher.setPower(0);
        if (pollen_servo != null) pollen_servo.setPower(0);
        if (turret_feeder != null) turret_feeder.setPower(0);
    }

    public double getTargetRPM() { return targetRPM; }
    public double getMeasuredRPM() { return measuredRPM; }
    public double getErrorRPM() { return errorRPM; }
    public double getMotorPower() { return motorPower; }
    public ControlMode getControlMode() { return mode; }
}
