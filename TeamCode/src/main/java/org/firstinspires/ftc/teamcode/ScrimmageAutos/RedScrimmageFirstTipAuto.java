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



    private Command autoRoutine() {
        return sequential(
                //Shoot preloaded
                //Turn on Intake
                follow(follower, toGarden()),
                //Turn off Intake
                follow(follower, toShoot()),
                //Check hive position with camera
                //Wait until hive is ready or until 5 seconds of auto remain
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
