package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class FlowerHood {
    //Class controls the position of flower outtake hood.

    HardwareMap hardwareMap;
    void doHoodState(int stateID) {
        // FUNCTIONALITY:
        // this is a state machine:
            // state #1: idle -> checking for gamepad input
            // state #2: moves to position for flower outtake
            // state #3: idle2 -> checking if no longer doing flower
            // end: goes back to state #1
        // returns nothing

        // CLASS INTERACTIONS:
        // needs info from Launcher class if it wants to shoot flower vs. HIVE
    }

}
