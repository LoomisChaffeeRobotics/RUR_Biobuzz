package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


// PedroPathing: Lightweight, easy to use, designed specifically for FTC, simple, quick setup,
//      supports basic path following, localization, and simple configuration.
// RoadRunner: powerful, lots of features, motion planning library for FTC.
//      supports advanced trajectory generation (splines, curves), velocity/acceleration constraints,
//      and integrates with odometry and localization. steeper learning curve and more complex setup.
@Autonomous
public class Auto extends OpMode {
    Launcher launcherClass;
    Intake intakeClass;
    Camera cameraClass;

    //Main OpMode run in Autonomous.
    public Follower follower;
    public PoseFactory poseFactory = PoseFactory.degrees(); //makes poses, uses degrees
    public Pose startPose = poseFactory.of(9.5,15,90); //pose (change it)
    public Pose intakePose = poseFactory.of(9.5,10,90); //pose (change it)
    public Pose flowerPose = poseFactory.of(39.3,129.5,260); //pose (change it)
    public Pose parkPose = poseFactory.of(9.3, 108.2, 0); //pose (change it)
    public Path intakeGarden() {
        return line(startPose, intakePose).constant(90); //line = straight line path, linear = constant heading change
    }
    public Path driveToFlower() {
        return line(intakePose, flowerPose).linear(90, 260); //line = straight line path, linear = constant heading change
    }
    public Path parkFromFlower() {
        return line(flowerPose, parkPose).linear(260, 0); //line = straight line path, linear = constant heading changege
    }
    public Command autoRoutine(){
        return sequential( //sequential = state machine, include all the states below and it does it for you
                Commands.instant(() ->
                    launcherClass.toggleLauncher(1,
                            launcherClass.interpolateLauncherPowerFromTable(1,
                                    launcherClass.pollen_distance_list,
                                    launcherClass.pollen_power_list,
                                    cameraClass.getDistanceFromAprilTag(),
                                    launcherClass.globalDelta),
                            launcherClass.globalEpsilon,
                            true) //set pose to start pose
                ), //this whole instant turns launcherclass.togglelauncher into a runnable and runs it once i believe
                Commands.waitMs(1000),
                Commands.instant(() ->
                    launcherClass.toggleLauncher(1,
                            new double[]{0.0},
                            launcherClass.globalEpsilon,
                            false) //set pose to start pose
                ), //turn launcher off
                Commands.instant(() -> intakeClass.toggleIntake(true)),
                follow(follower, intakeGarden()), //follows path
                Commands.waitMs(500),
                Commands.instant(() -> intakeClass.toggleIntake(false)),
                follow(follower, driveToFlower()),
                Commands.instant(() ->
                    launcherClass.toggleLauncher(1,
                            launcherClass.interpolateLauncherPowerFromTable(1,
                                    launcherClass.pollen_distance_list,
                                    launcherClass.pollen_power_list,
                                    cameraClass.getDistanceFromAprilTag(),
                                    launcherClass.globalDelta),
                            launcherClass.globalEpsilon,
                            true) //set pose to start pose
                ), //this whole instant turns launcherclass.togglelauncher into a runnable and runs it once i believe
                Commands.waitMs(500),
                Commands.instant(() ->
                        launcherClass.toggleLauncher(1,
                                new double[]{0.0},
                                launcherClass.globalEpsilon,
                                false) //set pose to start pose
                ), //turn launcher off
                Commands.instant(() -> intakeClass.toggleIntake(true)),
                Commands.waitMs(1000),
                Commands.instant(() -> intakeClass.toggleIntake(false)),
                follow(follower, parkFromFlower())
                
                //Add things here
        );
    }

    @Override
    public void init() {
        Scheduler.reset(); // reset scheduler, clears all commands and states
        launcherClass = new Launcher();
        launcherClass.init(hardwareMap);
        intakeClass = new Intake();
        intakeClass.init(hardwareMap);
        follower = Constants.create(hardwareMap);
        if (follower != null) {
            follower.setPose(startPose);
        }
    }
    @Override
    public void start() {
        schedule(autoRoutine()); //schedule = adds command to scheduler, starts executing it


    }
    @Override
    public void loop() {
        Scheduler.execute();
        follower.update();
        //add other update methods here like PIDs



    }
}