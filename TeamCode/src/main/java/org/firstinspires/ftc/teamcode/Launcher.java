package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
@Config
public class Launcher {
    //Class calculates the target speed of turret and sets turret to correct power.

    HardwareMap hardwareMap;
    DcMotorEx pollen_launcher;
    DcMotorEx nectar_launcher;
    DcMotorEx turret_feeder;
    CRServo pollen_servo;
    CRServo nectar_servo;

    public double[] pollen_distance_list = {30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74};
    public double[] nectar_distance_list = {30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74};
    public double[] pollen_power_list = {1264, 1320, 1348, 1308, 1328, 1312, 1292, 1320, 1340, 1340, 1344, 1348, 1348, 1348, 1368, 1400, 1412, 1420, 1428, 1456, 1464, 1472, 1468};
    public double[] nectar_power_list = {1372, 1352, 1352, 1356, 1344, 1400, 1384, 1380, 1396, 1416, 1520, 1508, 1496, 1504, 1536, 1596, 1624, 1672, 1708, 1716, 1728, 1736, 1744};
    public double pollenP = 71;
    public double pollenI = 5.1;
    public double pollenD = 17;
    public double pollenF = -2.9;

    public double nectarP = 83;
    public double nectarI = 5.1;
    public double nectarD = 21;
    public double nectarF = -3.1;


    public PIDFCoefficients pollenLauncherPIDF = new PIDFCoefficients(pollenP, pollenI, pollenD, pollenF);
    public PIDFCoefficients nectarLauncherPIDF = new PIDFCoefficients(nectarP,nectarI,nectarD,nectarF);
    public double globalDelta = 2;
    public double globalEpsilon = 40;

    public double lastVelocity = 0;

    public static double[] targetRPMList = {0, 0};

    public void init(HardwareMap hardwareMap) {
        pollen_launcher = hardwareMap.get(DcMotorEx.class, "pollen_launcher");
        pollen_launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        nectar_launcher = hardwareMap.get(DcMotorEx.class, "nectar_launcher");
        pollen_servo = hardwareMap.get(CRServo.class, "pollen_servo");
        nectar_servo = hardwareMap.get(CRServo.class, "nectar_servo");
        turret_feeder = hardwareMap.get(DcMotorEx.class, "turret_feeder");

        nectar_launcher.setVelocityPIDFCoefficients(nectarP, nectarI, nectarD, nectarF);
        pollen_launcher.setVelocityPIDFCoefficients(pollenP, pollenI,pollenD, pollenF);
    }

    int[] getBoundingIndexes(double[] list, double targetValue) {

        int bottom = 0; int top = list.length - 1;
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

        return new int[] {bottom, top};

    }
    public double[] interpolateLauncherPowerFromTable(int launcherID, double[] distanceList, double[] powerList, double targetDistance, double deltaDistance) {

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

        int[] powerBounds = getBoundingIndexes(distanceList, targetDistance);
        // since each power index is directly associated with a distance, we can use distance indexes for power indexes

        double targetRPM =
                ((targetDistance - distanceList[powerBounds[0]]) * // this is just the change in x from point A to point B
                        ((powerList[powerBounds[1]] - powerList[powerBounds[0]])/deltaDistance))  // this is the slope of the line
                        + powerList[powerBounds[0]]; // this is the y intercept

        //target distance = current distance
        //powerBounds[0] = lower bound index for velocity speed
        //powerBounds[1] = upper bound index for velocity speed
        //deltaDistance = 2
        //powerList = Look up table of all the velocities
        //powerList[powerBounds[0]] = lower bound velocity
        //powerList[powerBounds[1]] = upper bound velocity
        //powerList[0] = lowest velocity in the look up table

        targetRPMList[launcherID - 1] = targetRPM;

        return new double[] {targetRPM, powerBounds[0], powerBounds[1]};
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

    public void toggleLauncher(int launcherID, double[] result, double epsilon, boolean toggle) {
        // FUNCTIONALITY:
        // launcherID -> is NECTAR launcher vs. POLLEN launcher
            // launcherID = 1 -> POLLEN
            // launcherID = 2 -> NECTAR
        // if the launcher isn't powered, it attempts to power it
            // can use along with isLauncherPowered() function

        //remember: ticks per second to revs per minute is 60/28
        // CLASS INTERACTIONS:
        // N/A

        if (launcherID == 1) {
            pollen_launcher.setVelocity(toggle ? -result[0] : 0);
            pollen_servo.setPower(-1);
        }
        else  {
//            nectar_launcher.setVelocity(toggle ? -result[0] : 0);
//            nectar_servo.setPower(-1);
        }
        lastVelocity = toggle ? -result[0] : 0;

    }

    public void feedLauncher(double[] RPMList, double epsilon, boolean toggle) {

        // FUNCTIONALITY:
        // feeds game pieces into turrets
        // only works if at least 1 turret is up to speed

        // CLASS INTERACTIONS:
        // N/A

        if(isLauncherPowered(1, RPMList[0], epsilon) || isLauncherPowered(2, RPMList[1], epsilon)) {
            turret_feeder.setVelocity(toggle ? 1 : 0);
        } else {
            turret_feeder.setVelocity(0);
        }

    }


}
