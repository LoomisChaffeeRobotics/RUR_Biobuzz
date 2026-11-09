package org.firstinspires.ftc.teamcode.ScrimmageAutos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Launcher;
import org.firstinspires.ftc.teamcode.Camera;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


// Robot Starting Position (from our drive team's perspective):
// Against the right wall (side of the upper starting hive)
// Robot's right wheels against midline
// Facing the hive (left from our driver's perspective)

// Function:
// Shoots 4 preloaded pollen
// Intakes as much pollen as possible from garden
// Returns to shooting location
// Checks hive position
// Shoots if hive is in correct position
// If hive position is wrong, wait
// With 5 seconds remaining, pass
// Parks

// Purpose:
// Secure the first tip, with backup or starting the third
// Compatible with an alliance partner who does nothing

// Red vs. Blue:
// Uses camera to detect when hive tips
// Could track both color's april tags
//  - It will only ever see the correct one (other april tag is on the other side)


@Autonomous
public class RedScrimmageFirstTipAuto extends OpMode {

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    //Poses
    private final Pose start = poseFactory.of(62, 9, 90);
    private final Pose toGarden = poseFactory.of(9, 9, -90);
    private final Pose toGardenControl1 = poseFactory.of(62, 48, 0);
    private final Pose toGardenControl2 = poseFactory.of(9, 48, 0);
    private final Pose toGardenSegment2Start = poseFactory.of(9, 9, -179.1567);
    private final Pose toGardenSegment2End = poseFactory.of(9, 9, 270);
    private final Pose toGardenSegment3Heading = poseFactory.of(9, 9, -90);
    private final Pose toShoot = poseFactory.of(62, 9, 90.7812);
    private final Pose toShootControl1 = poseFactory.of(9, 48, 0);
    private final Pose toShootControl2 = poseFactory.of(62, 48, 0);
    private final Pose toShootSegment1Heading = poseFactory.of(62, 9, 270);
    private final Pose park = poseFactory.of(9, 90, 0);
    private final Pose parkSegment1Start = poseFactory.of(9, 90, 90);
    private final Pose parkSegment1End = poseFactory.of(9, 90, 0);
    private final Pose parkSegment2Heading = poseFactory.of(9, 90, 0);


    //Paths
    public Path toGarden() {
        return Paths.curve(start, toGardenControl1, toGardenControl2, toGarden).heading(Interpolator.piecewise().until(0.5, Interpolator.tangent).until(0.9, Interpolator.linear(toGardenSegment2Start, toGardenSegment2End)).until(1, Interpolator.constant(toGardenSegment3Heading)));
    }

    public Path toShoot() {
        return Paths.curve(toGarden, toShootControl1, toShootControl2, toShoot).heading(Interpolator.piecewise().until(0.1, Interpolator.constant(toShootSegment1Heading)).until(1, Interpolator.tangent.reverse()));
    }

    public Path park() {
        return Paths.line(toShoot, park).heading(Interpolator.piecewise().until(0.75, Interpolator.linear(parkSegment1Start, parkSegment1End)).until(1, Interpolator.constant(parkSegment2Heading)));
    }


    Intake intake;
    Launcher launcherClass;
    Camera cameraClass;

    public double[] odometryConstants = {0, 0, 0};

    private double[] pollenTurretPower = launcherClass.interpolateLauncherPowerFromTable(1,
        launcherClass.pollen_distance_list,
        launcherClass.pollen_power_list,
        52,
        launcherClass.globalDelta);

    private double pollenTargetRPM = pollenTurretPower[0];


    //Commands
    Command spinUpPollen = Command.build()
            .setExecute(() -> {
                launcherClass.toggleLauncher(1, pollenTurretPower, launcherClass.globalEpsilon, true);
            })
            .setDone(() -> {
                return(launcherClass.isLauncherPowered(1, pollenTargetRPM, launcherClass.globalEpsilon));
            });

    Command shootPollen = sequential(
            instant(() -> launcherClass.feedLauncher(pollenTargetRPM, launcherClass.globalEpsilon, true)),
            Commands.waitMs(1000),
            instant(() -> launcherClass.feedLauncher(pollenTargetRPM, launcherClass.globalEpsilon, false))
    );

    Command stopPollen = instant(() -> launcherClass.toggleLauncher(1, pollenTurretPower, launcherClass.globalEpsilon, false));

    Command runIntake = instant(() -> intake.toggleIntake(true));
    Command stopIntake = instant(() -> intake.toggleIntake(false));

    Command waitForHive = waitUntil(() -> cameraClass.getDistanceFromAprilTag(cameraClass.red_audience_list) > 50); // change to hive distance when up +- some error

    Command stopAll = parallel(
            stopIntake,
            stopPollen,
            instant(() -> launcherClass.feedLauncher(pollenTargetRPM, launcherClass.globalEpsilon, false))
    );



    private Command autoRoutine() {
        return sequential(
                deadline(
                        waitMs(25000),
                        //Inside is run until 5 seconds of auto remain

                        spinUpPollen,
                        shootPollen,
                        stopPollen,
                        //Shoot preloaded

                        runIntake,
                        follow(follower, toGarden()),
                        stopIntake,


                        follow(follower, toShoot()),

                        waitForHive,

                        spinUpPollen,
                        shootPollen,
                        stopPollen

                ),

                stopAll,
                follow(follower, park())

        );
    }


    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        intake = new Intake();
        intake.init(hardwareMap);
        launcherClass = new Launcher();
        launcherClass.init(hardwareMap);
        cameraClass = new Camera();
        cameraClass.init(hardwareMap, odometryConstants);

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
