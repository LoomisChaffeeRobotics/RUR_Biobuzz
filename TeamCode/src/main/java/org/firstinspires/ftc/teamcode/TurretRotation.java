package org.firstinspires.ftc.teamcode;

import com.pedropathing.controllers.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TurretRotation {

    CRServo turretServo;
    PIDController turretPID;
    DcMotorEx turretEncoder;
    double timeStep = 0.02;
    double kF = 0.1; // Feedforward gain, adjust as needed
    public static double kP = 0.01; // Proportional gain, adjust as needed
    public static double kI = 0.001; // Integral gain, adjust as needed
    public static double kD = 0.005; // Derivative gain, adjust as needed
    double targetAngle = 0; // Target angle for the turret
    double turretPower = 0; // Power to apply to the turret servo

    public void init(HardwareMap hardwareMap) {
        turretServo = hardwareMap.get(CRServo.class, "turretServo");
        turretEncoder = hardwareMap.get(DcMotorEx.class, "turretEncoder");
        turretEncoder.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        turretPID = new PIDController(kP, kI, kD);

    }

    public void rotateTurret(double power) {
        turretServo.setPower(power);
    }
    public void updateTurret(double targetAngle) {
        double currentAngle = turretEncoder.getCurrentPosition() / 114.0; //last year we used the same encoder + we dviided by 114 for some reason butr idk why
        double error = targetAngle - currentAngle;

        turretPower = turretPID.calculate(targetAngle, error);
        rotateTurret(turretPower);

    }


}
