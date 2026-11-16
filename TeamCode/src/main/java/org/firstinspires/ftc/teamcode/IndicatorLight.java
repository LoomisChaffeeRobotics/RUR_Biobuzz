package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class IndicatorLight {

    Servo light;

    public enum Color {
        OFF(0),
        RED(0.279),
        ORANGE(0.333),
        YELLOW(0.388),
        GREEN(0.500),
        BLUE(0.611),
        PURPLE(0.722),
        WHITE(1);

        private final double value;

        Color(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

    }

    public void init(HardwareMap hardwareMap) {
        light = hardwareMap.get(Servo.class, "light");

    }

    public void setColor(Color color) {
        light.setPosition(color.getValue());
    }

}

//Color Guide:
//Black – 0
//Red – 0.279
//Orange – 0.333
//Gold – 0.357
//Yellow – 0.388
//Sage – 0.444
//Green – 0.500
//Azure – 0.555
//Blue – 0.611
//Indigo – 0.666
//Violet – 0.722
//White – 1