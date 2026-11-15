package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class RightAuto extends OpMode {

    Drive drive;
    Odometry odometry;
//    int var = 0;
    int multiplier = 1;
    @Override
    public void init() {
        drive = new Drive();
        drive.init(hardwareMap);

        odometry = new Odometry();
        odometry.init(hardwareMap, new double[] {0, 0, 0});

    }

    @Override
    public void loop() {
//        var += 1;

        drive.driveUpdateFromGamepadInput(0, -multiplier, 0, odometry.getRobotAngle());

        if (odometry.getRobotPosition()[1] < -24) {
            multiplier = 0;
        }
    }
}
