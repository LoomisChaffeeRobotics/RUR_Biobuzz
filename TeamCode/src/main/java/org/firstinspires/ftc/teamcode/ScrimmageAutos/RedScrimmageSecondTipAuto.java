package org.firstinspires.ftc.teamcode.ScrimmageAutos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Camera;
import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.Launcher;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


// Robot Starting Position (from our drive team's perspective):
// Against the left wall (side of the lower starting hive)
// Robot's left wheels against midline
// Facing the hive (right from our driver's perspective)

// Function:
// Waits until hive tips then shoots
// If no tip is detected with 5 seconds remaining, pass
// Parks

// Purpose:
// Start filling the hive for the second tip
// Compatible with an alliance partner who secures first tip

// Red vs. Blue:
// Uses camera to detect when hive tips
// Could track both color's april tags
//  - It will only ever see the correct one (other april tag is on the other side)

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous
public class RedScrimmageSecondTipAuto extends OpMode {

    Launcher launcher;
    Drive drive;

    Camera camera;

    Limelight3A limelight;

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    //Poses
    private final Pose start = poseFactory.of(61, 132, 90);
    private final Pose leave = poseFactory.of(61, 123.25, 270);
    private final Pose parkStart = poseFactory.of(61, 123.25, 270);
    private final Pose park = poseFactory.of(9, 123.25, 0);


    //Paths
    public Path leave() {
        return Paths.line(start, leave).constant(leave);
    }

    public Path park() {
        return Paths.line(parkStart, park).linear(parkStart, park);
    }
    double[] turretPower = launcher.interpolateLauncherPowerFromTable(
            1,
            launcher.pollen_distance_list,
            launcher.pollen_power_list,
            52,
            launcher.globalDelta
    );

    double[] RPM = launcher.targetRPMList;
    Command spinupwheel = Command.build()
            .setExecute(() -> {
                launcher.toggleLauncher(
                        1, turretPower,
                        launcher.globalEpsilon,
                        true);
            })

            .setDone(() -> {
                return(launcher.isLauncherPowered(1, RPM[0], launcher.globalEpsilon));
            });

    Command shoot = sequential(
            instant(()->launcher.feedLauncher(RPM,launcher.globalEpsilon,true)),
            waitMs(10000),
            instant(()->launcher.feedLauncher(RPM,launcher.globalEpsilon,false))
    );
    private Command autoRoutine() {

        return sequential(
            spinupwheel,
                waitUntil(() -> //wait based on limelight
                        camera.getDistanceFromAprilTag(camera.red_cluster_list)
                                >= 48
                ),
                shoot,
                follow(follower, leave()),
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
