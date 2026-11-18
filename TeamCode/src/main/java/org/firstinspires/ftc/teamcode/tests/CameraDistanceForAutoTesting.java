package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.subsystems.Camera;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CameraDistanceForAutoTesting extends OpMode {

    Camera camera;


    @Override
    public void init() {

        camera = new Camera();
        camera.init(hardwareMap, new double[] {0,0,0});

    }

    @Override
    public void loop() {

        telemetry.addData("distance: ", camera.getDistanceFromAprilTag(camera.red_cluster_list));
        telemetry.update();

    }


}
