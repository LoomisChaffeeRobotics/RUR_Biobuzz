package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher {
    //Class calculates the target speed of turret and sets turret to correct power.

    HardwareMap hardwareMap;
    double interpolateLauncherPowerFromTable(int launcherID, double[] distanceList, double[] powerList, double targetDistance) {
        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // distanceList & powerList -> look up tables for distances and respective launcher motor powers
            // linearly interpolate targetDistance using these tables to find the targetPower
            // targetPower = (y_2 - y_1)(targetDistance) + y_0
        // return targetPower

        // CLASS INTERACTIONS:
        // N/A

        return 0;
    }

    boolean isLauncherPowered(int launcherID, double targetRPM, double epsilon) {

        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // needs to know if a specific launcher RPM is within epsilon of the targetRPM

        // CLASS INTERACTIONS:
        // N/A

        return true;
    }

    void powerLauncher(int launcherID, double targetRPM) {
        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // if the launcher isn't powered, it attempts to power it
            // can use along with isLauncherPowered() function

        // CLASS INTERACTIONS:
        // N/A


    }

}
