package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;


// PedroPathing: Lightweight, easy to use, designed specifically for FTC, simple, quick setup,
//      supports basic path following, localization, and simple configuration.
// RoadRunner: powerful, lots of features, motion planning library for FTC.
//      supports advanced trajectory generation (splines, curves), velocity/acceleration constraints,
//      and integrates with odometry and localization. steeper learning curve and more complex setup.
@Autonomous
public class Auto extends OpMode {
    //Main OpMode run in Autonomous.
    public Follower follower;
    public PoseFactory poseFactory = PoseFactory.degrees();

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

    }
    @Override
    public void start() {

    }
    @Override
    public void loop() {

    }
}