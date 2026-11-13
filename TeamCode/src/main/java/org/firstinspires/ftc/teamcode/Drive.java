package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Drive {
    //Class turns driver inputs into field centric driving and controls drive motors.
    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;

    public static double strafeCounteract = 1; //this is a multiplier to make strafing more accurate, since the robot tends to drift when strafing


    public double currentYaw = 0;
    Camera cameraClass;
    public void init(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        cameraClass = new Camera();
        cameraClass.init(hardwareMap, new double[] {0, 0, 0});
    }
    public void driveTankDrive(double leftStick, double rightStick) {

        frontLeft.setPower(leftStick);
        backLeft.setPower(leftStick);
        frontRight.setPower(rightStick);
        backRight.setPower(rightStick);
    }
    public void driveUpdateFromGamepadInput(double x, double y, double rx, double yaw) {

        currentYaw = yaw;

        //remember that y is the opposite of the gamepad y
        //yaw degrees
        //idk what rx is but it is the rotation of the robot
        double rotX = x * Math.cos(-Math.toRadians(yaw)) - y * Math.sin(-Math.toRadians(yaw));
        double rotY = x * Math.sin(-Math.toRadians(yaw)) + y * Math.cos(-Math.toRadians(yaw));

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

    public double turnToHive(int colorID, double yaw) {

        int[] cluster_list = colorID == 1 ? cameraClass.red_cluster_list : cameraClass.blue_cluster_list;

        // angle assumes y is up/down, x is left/right
        double angle = cameraClass.getAprilTagAngle(cluster_list) > 3 ? cameraClass.getAprilTagAngle(cluster_list) : 0;

        // MUST add +angle to get the target yaw
        // -- > targetYaw = yaw + angle;


        // maybe replace with PIDF soon but im too lazy
        //decay is basically just making so that the smaller the angle, the smaller the motor power so that no overshoot
        double decayConstant = Math.min(Math.abs(angle)/60, 1);

        driveUpdateFromGamepadInput(0, 0, decayConstant * Math.signum(angle), yaw);

        return angle;

    }

}
