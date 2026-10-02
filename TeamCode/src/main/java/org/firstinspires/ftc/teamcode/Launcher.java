package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Launcher {
    //Class calculates the target speed of turret and sets turret to correct power.

    HardwareMap hardwareMap;
    DcMotorEx pollen_launcher;
    DcMotorEx nectar_launcher;

    public double[] pollen_distance_list = {};
    public double[] nectar_distance_list = {};
    public double[] pollen_power_list = {};
    public double[] nectar_power_list = {};

    public double globalDelta = 0.1;
    public double globalEpsilon = 50;

    public void init(HardwareMap hardwareMap) {
        pollen_launcher = hardwareMap.get(DcMotorEx.class, "pollen_launcher");
        nectar_launcher = hardwareMap.get(DcMotorEx.class, "nectar_launcher");
    }

    double[] getBoundingIndexes(double[] list, double targetValue) {

        double bottom = 0; double top = list.length - 1;
        double bottomVal = 0; double topVal = 999999;

        for (int i = 0; i < list.length; i++) {

            if (list[i] >= bottomVal && list[i] <= targetValue) {

                bottom = i;
                bottomVal = list[i];

            }

            if (list[i] <= topVal && list[i] >= targetValue) {

                top = i;
                topVal = list[i];

            }

        }

        return new double[] {bottom, top};

    }
    public double interpolateLauncherPowerFromTable(int launcherID, double[] distanceList, double[] powerList, double targetDistance, double deltaDistance) {

        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // distanceList & powerList -> look up tables for distances and respective launcher motor powers
            // linearly interpolate targetDistance using these tables to find the targetRPM
            // targetRPM = ((y_2 - y_1)/delta)(targetDistance - x_1) + y_1
        // return targetRPM

        // CLASS INTERACTIONS:
        // N/A

        double[] powerBounds = getBoundingIndexes(distanceList, targetDistance);
        // since each power index is directly associated with a distance, we can use distance indexes for power indexes

        double targetRPM =
                ((targetDistance - distanceList[1]) * // this is just the change in x from point A to point B
                        ((powerBounds[2] - powerBounds[1])/deltaDistance))  // this is the slope of the line
                        + powerList[1]; // this is the y intercept

        return targetRPM;
    }

    public boolean isLauncherPowered(int launcherID, double targetRPM, double epsilon) {

        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // needs to know if a specific launcher RPM is within epsilon of the targetRPM

        // CLASS INTERACTIONS:
        // N/A

        double launcherVelocity = launcherID == 1 ? pollen_launcher.getVelocity() : nectar_launcher.getVelocity();

        double rpm = launcherVelocity / 6.0;

        return Math.abs(rpm - targetRPM) <= epsilon;
    }

    public void toggleLauncher(int launcherID, double targetRPM, double epsilon, boolean toggle) {
        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // if the launcher isn't powered, it attempts to power it
            // can use along with isLauncherPowered() function

        // CLASS INTERACTIONS:
        // N/A

        if (launcherID == 1) {
            pollen_launcher.setVelocity(toggle ? targetRPM*6.0 : 0);
        }
        else  {
            nectar_launcher.setVelocity(toggle ? targetRPM*6.0 : 0);
        }



    }

}
