package org.firstinspires.ftc.teamcode.ScrimmageAutos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


// Robot Starting Position (from our drive team's perspective):
// Against the left wall (side of the lower starting hive)
// Near corner, not touching close wall
// Facing away from our drive team

// Function:
// Drives a little to the right to Park

// Purpose:
// Leave and park
// Stay out of alliance partner's way

// Red vs. Blue:
// No difference by alliance

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class RedScrimmageParkAuto extends OpMode {

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    //Poses
    private final Pose start = poseFactory.of(12, 132, 90);
    private final Pose path1 = poseFactory.of(12, 122.5, 0);


    //Paths
    public Path park() {
        return Paths.line(start, path1).constant(path1);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, park())
        );
    }


    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

    }

    @Override
    public void start() {
        schedule(autoRoutine());

    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

    }


}
