/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.Commands.ManualCommandDrive;
import frc.robot.Util.Xbox;

/**
 * Subsystem controlling the motors in the drivetrain
 */
public class SubsystemDrive extends Subsystem {

  private static CANSparkMax leftMaster;
  private static CANSparkMax leftSlave;
  private static CANSparkMax rightMaster;
  private static CANSparkMax rightSlave;

  private static double[] highestRPM;

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ManualCommandDrive());
  }

  public SubsystemDrive() {
    DriverStation.reportWarning("SUB_DRIVE CREATED", false);
    leftMaster  = new CANSparkMax(Constants.LEFT_MASTER_ID, MotorType.kBrushless);
    leftSlave   = new CANSparkMax(Constants.LEFT_SLAVE_ID, MotorType.kBrushless);

    rightMaster = new CANSparkMax(Constants.RIGHT_MASTER_ID, MotorType.kBrushless);
    rightSlave  = new CANSparkMax(Constants.RIGHT_SLAVE_ID, MotorType.kBrushless);

    highestRPM = new double[]{0,0};
  }

  /**
   * Rocket League/Tank hybrid control system
   * Left and right triggers accelerate linearly and left stick rotates
   * @param joy the joystick to be used
   */
  public void driveRLTank(Joystick joy, double ramp, double inhibitor) {
    setInverts();
    setBraking(true);
    setRamps(ramp);
    updateBrownoutRummble(joy);

    double adder = Xbox.RT(joy) - Xbox.LT(joy);
    double left = adder + (Xbox.LEFT_X(joy) / 1.333333);
    double right = adder - (Xbox.LEFT_X(joy) / 1.333333);
    left = (left > 1.0 ? 1.0 : (left < -1.0 ? -1.0 : left));
    right = (right > 1.0 ? 1.0 : (right < -1.0 ? -1.0 : right));
    
    left *= inhibitor;
    right *= inhibitor;
    
    leftMaster.set(left);
      leftSlave.set(left);
    rightMaster.set(right);
      rightSlave.set(right);
  }

  /**
   * Directly sends a percent output value to each side
   * @param leftOutput  percent output of left side
   * @param rightOutput percent output of right side
   */
  public void driveByPercentOutputs(double leftOutput, double rightOutput) {
    leftMaster.set(leftOutput);
      leftSlave.set(leftOutput);
    rightMaster.set(rightOutput);
      rightSlave.set(rightOutput);
      DriverStation.reportError("DRIVE COMMAND IS RUNNING", false);
  }

  public double[] getEncoderPositions() {
    double[] output = new double[2];
    output[0] = leftMaster.getEncoder().getPosition();
    output[1] = rightMaster.getEncoder().getPosition();
    return output;
  }

  /**
   * Sets all motor controller values to zero
   */
  public void stopMotors() {
    leftMaster.set(0);
      leftSlave.set(0);
    rightMaster.set(0);
      rightSlave.set(0);
  }

  /**
   * Sets the inverts of each motor controller
   */
  private void setInverts() {
    leftMaster.setInverted(Constants.LEFT_DRIVE_INVERT);
      leftSlave.setInverted(Constants.LEFT_DRIVE_INVERT);
    rightMaster.setInverted(Constants.RIGHT_DRIVE_INVERT);
      rightSlave.setInverted(Constants.RIGHT_DRIVE_INVERT);
  }

  /**
   * Sets each motor to braking or coasting mode
   * @param braking true if braking mode, false if coasting mode
   */
  public void setBraking(Boolean braking) {
    leftMaster.setIdleMode(braking ? IdleMode.kBrake : IdleMode.kCoast);
      leftSlave.setIdleMode(braking ? IdleMode.kBrake : IdleMode.kCoast);
    rightMaster.setIdleMode(braking ? IdleMode.kBrake : IdleMode.kCoast);
      rightSlave.setIdleMode(braking ? IdleMode.kBrake : IdleMode.kCoast);
  }

  /**
   * Sets the ramp rate of each motor
   * @param ramp ramp rate in seconds
   */
  private void setRamps(double ramp) {
    leftMaster.setOpenLoopRampRate(ramp);
      leftSlave.setOpenLoopRampRate(ramp);
    rightMaster.setOpenLoopRampRate(ramp);
      rightSlave.setOpenLoopRampRate(ramp);
  }

  /**
   * Retrieves the percentOutput/speed values of each motor controller
   * @return array of percentOutputs/speeds stored as doubles
   *         [0] = Left Master speed
   *         [1] = Left Slave speed
   *         [2] = Right Master speed
   *         [3] = Right Slave speed
   */
  public double[] getMotorValues() {
    double[] output = new double[4];
    output[0] = leftMaster.get();
    // output[1] = leftSlave.get();
    output[2] = rightMaster.get();
    // output[3] = rightSlave.get();
    return output;
  }

  /**
   * Retrieves an array with the absolute RPM of each motor controller
   * @return [0] = Current absolute RPM from left side
   *         [1] = Current absolute RPM from right side
   */
  public double[] getVelocities() {
    double[] output = new double[2];
    output[0] = Math.abs(leftMaster.getEncoder().getVelocity());
    output[1] = Math.abs(rightMaster.getEncoder().getVelocity());
    return output;
  }

  public double[] getAmps() {
    return new double[]{leftMaster.getOutputCurrent(), rightMaster.getOutputCurrent()};
  }
  /**
   * Returns the highest recorded RPM of each motor controller
   * @return [0] = Highest absolute RPM from left side
   *         [1] = Highest absolute RPM from right side
   */
  public double[] getHighestVelocities() {
    if (leftMaster.getEncoder().getVelocity() > highestRPM[0]) {
      highestRPM[0] = Math.abs(leftMaster.getEncoder().getVelocity()); }
    if (rightMaster.getEncoder().getVelocity() > highestRPM[1]) {
      highestRPM[1] = Math.abs(rightMaster.getEncoder().getVelocity()); }
    return highestRPM;
  }

  /**
   * Checks if the robot is pushing by checking if both sides 
   * are pulling high amperage
   * @return left and right motor are both pulling over the threshold amperage
   */
  public Boolean isPushing() {
    return leftMaster.getOutputCurrent() >= Constants.PUSHING_AMPERAGE && rightMaster.getOutputCurrent() >= Constants.PUSHING_AMPERAGE;
  }

  public void updateBrownoutRummble(Joystick joy) {
    if (DriverStation.getInstance().isBrownedOut()) {
      joy.setRumble(RumbleType.kRightRumble, 1); }
    else {
      joy.setRumble(RumbleType.kRightRumble, 0);
    }
  } 

  
}
