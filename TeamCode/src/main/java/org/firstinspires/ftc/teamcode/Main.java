package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class Main extends OpMode {
    //Main OpMode run in TeleOp.

    Intake intake;

    @Override
    public void init() {
        // in init(),
        // get intake hardware (hardware.get()),
        // in loop(),
        // make so that: pressing A key -> toggleIntake,
        // with correct parameters passed in

        intake = new Intake();
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            intake.toggleIntake(true);
        } else {
            intake.toggleIntake(false);
        }
    }
}