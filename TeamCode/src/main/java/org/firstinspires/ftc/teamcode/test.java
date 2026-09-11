package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp
public class test extends OpMode {
    DcMotorEx motor;
    @Override
    public void init(){
        motor = hardwareMap.get(DcMotorEx.class, "motor");

    }
    @Override
    public void loop() {
        if (gamepad1.a) {
            motor.setPower(0.5);
        } else if (gamepad1.b) {
            motor.setPower(-0.5);
        } else {
            motor.setPower(0);
        }
    }
}