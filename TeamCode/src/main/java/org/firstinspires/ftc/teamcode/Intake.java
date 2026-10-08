package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    //Runs the Intake.

    DcMotor intakeMotor;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
    }
    public boolean isFull(int targetArtifacts) {
        // FUNCTIONALITY:
        // needs to know if there are N many artifacts in the robot
        // returns whether is full

        // CLASS INTERACTIONS:
        // N/A
        return false;
    }

    public boolean toggleIntake(boolean condition) {
        // FUNCTIONALITY:
        // when "condition" is true, run intake if not full ( use isFull() )
        // when "condition" is false, always turn off the intake
        // returns true if it succeeded and false if it failed (e.g. was full but tried to run)
        if (condition && !isFull(4)) {
            intakeMotor.setPower(1);
            return true;
        }

        else if (condition && isFull(4)) {
            return false;
        }
        else if (!condition) {
            intakeMotor.setPower(0);
            return true;
        }

        // CLASS INTERACTIONS:
        // N/A
        return true;
    }

}
