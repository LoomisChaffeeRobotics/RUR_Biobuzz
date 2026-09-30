package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Drive {
    //Class turns driver inputs into field centric driving and controls drive motors.
    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;
    HardwareMap hardwareMap;
    public static double strafeCounteract = 1.1; //this is a multiplier to make strafing more accurate, since the robot tends to drift when strafing


    void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);



    }
    public void driveUpdateFromGamepadInput(double x, double y, double rx, float yaw) {

        //remember thant y is the opposite of the gamepad y
        //i thunk yaw is radians
        //idk what rx is but it is the rotation of the robot
        double rotX = x * Math.cos(-yaw) - y * Math.sin(-yaw);
        double rotY = x * Math.sin(-yaw) + y * Math.cos(-yaw);

        rotX = rotX * strafeCounteract;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

        // FUNCTIONALITY:
        // needs to set motor powers for each of the 4 wheels
        // make sure that it's field centric; the direction the robot is facing doesn't affect how it moves
        // returns nothing (just drives)

        // CLASS INTERACTIONS:
        // needs gamepad inputs from OpMode + imu rotation from Odometry class
    }



}
