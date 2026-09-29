package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    //Runs the Intake.

    HardwareMap hardwareMap;

    boolean isFull(int targetArtifacts) {
        // FUNCTIONALITY:
        // needs to know if there are N many artifacts in the robot
        // returns whether is full

        // CLASS INTERACTIONS:
        // N/A
        return true;
    }

    boolean toggleIntake(boolean condition) {
        // FUNCTIONALITY:
        // when "condition" is true, run intake if not full ( use isFull() )
        // when "condition" is false, always turn off the intake
        // returns true if it succeeded and false if it failed (e.g. was full but tried to run)

        // CLASS INTERACTIONS:
        // N/A
        return true;
    }

}
