package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class IndicatorLightTesting extends OpMode {

    IndicatorLight light;



    public void init(){

        light = new IndicatorLight();
        light.init(hardwareMap);

    }


    public void loop(){

        if (gamepad1.a) {
            light.setColor(IndicatorLight.Color.RED);
        } else if (gamepad1.b) {
            light.setColor(IndicatorLight.Color.ORANGE);
        } else if (gamepad1.x) {
            light.setColor(IndicatorLight.Color.YELLOW);
        } else if (gamepad1.y) {
            light.setColor(IndicatorLight.Color.GREEN);
        } else if (gamepad2.a) {
            light.setColor(IndicatorLight.Color.BLUE);
        } else if (gamepad2.b) {
            light.setColor(IndicatorLight.Color.PURPLE);
        } else if (gamepad2.x) {
            light.setColor(IndicatorLight.Color.WHITE);
        } else {
            light.setColor(IndicatorLight.Color.OFF);
        }

    }




}
