/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Commands.ManualCommandTestMast;
import frc.robot.Enumeration.MastPosition;
import frc.robot.Util.Xbox;

/**
 * Two-stage system that moves the maniuplator vertically
 */
public class SubsystemMast extends Subsystem {

  private static MastPosition storedPosition;

  private static TalonSRX innerStage;
  private static TalonSRX outerStage;

  private static Boolean loopRunning;

  @Override
  public void initDefaultCommand() {}

  public SubsystemMast() {
    storedPosition = MastPosition.HATCH_1;

    innerStage  = new TalonSRX(Constants.INNER_STAGE_ID);
    outerStage = new TalonSRX(Constants.OUTER_STAGE_ID);

    initConfig(50, 0, true);
  }

  public void setStoredPosition(MastPosition position) {
    storedPosition = position;
    loopRunning    = false;
  }

  public MastPosition getStoredPosition() {
    return storedPosition;
  }

  public Boolean getLoopRunning() {
    return loopRunning;
  }

  public void moveInnerStageByPercent(double speed) {
    innerStage.set(ControlMode.PercentOutput, speed);
  }

  public void moveInnerStageByPosition(double inches) {
    innerStage.set(ControlMode.Position, inches);
  }

  public Boolean innerStageWithinRange(double inches) {
    double position = innerStage.getSensorCollection().getQuadraturePosition();
    double target   = inches * Constants.CTRE_TICKS_PER_ROTATION * Constants.MAST_ROTATIONS_PER_INCH;
    return Math.abs(position - target) < Constants.MAST_ALLOWABLE_ERROR;
  }

  public void moveOuterStageByPercent(double speed) {
    outerStage.set(ControlMode.PercentOutput, speed);
  }

  public void moveOuterStageByPosition(double inches) {
    outerStage.set(ControlMode.Position, inches);
  }

  public Boolean outerStageWithinRange(double inches) {
    double position = outerStage.getSensorCollection().getQuadraturePosition();
    double target = inches * Constants.CTRE_TICKS_PER_ROTATION * Constants.MAST_ROTATIONS_PER_INCH;
    return Math.abs(position - target) < Constants.MAST_ALLOWABLE_ERROR;
  }

  public void moveWithJoystick(Joystick joy, double innerStageInhibitor, double outerStageInhibitor) {
    innerStage.set(ControlMode.PercentOutput, Xbox.LEFT_Y(joy) * Math.abs(innerStageInhibitor));
    outerStage.set(ControlMode.PercentOutput, Xbox.RIGHT_Y(joy) * Math.abs(outerStageInhibitor));
  }

  public Boolean[] getLimitSwitches() {
    Boolean[] array = new Boolean[4];
    // array[0] = innerStageLow;
    // array[1] = innerStageHigh;
    // array[2] = outerStageLow;
    // array[3] = outerStageHigh;
    array[0] = innerStage.getSensorCollection().isFwdLimitSwitchClosed();
    array[1] = innerStage.getSensorCollection().isRevLimitSwitchClosed();
    array[2] = outerStage.getSensorCollection().isFwdLimitSwitchClosed();
    array[3] = outerStage.getSensorCollection().isRevLimitSwitchClosed();
    return array;
  }

  public void publishLimitSwitches() {
    SmartDashboard.putBoolean("Inner Stage Low [0]", getLimitSwitches()[0]);
    SmartDashboard.putBoolean("Inner Stage High [1]", getLimitSwitches()[1]);
    SmartDashboard.putBoolean("Outer Stage Low [2]", getLimitSwitches()[2]);
    SmartDashboard.putBoolean("Outer Stage High [3]", getLimitSwitches()[3]);
  }

  /**
   * 
   * @param ampLimit
   * @param ramp
   * @param braking
   */
  public void initConfig(int ampLimit, double ramp, Boolean braking) {
    innerStage.setInverted(Constants.INNER_STAGE_INVERT);
      innerStage.configOpenloopRamp(ramp);
      innerStage.configContinuousCurrentLimit(ampLimit);
      innerStage.setNeutralMode(braking ? NeutralMode.Brake : NeutralMode.Coast);;
    outerStage.setInverted(Constants.OUTER_STAGE_INVERT);
      outerStage.configOpenloopRamp(ramp);
      outerStage.configContinuousCurrentLimit(ampLimit);
      outerStage.setNeutralMode(braking ? NeutralMode.Brake : NeutralMode.Coast);;
  }

  /**
   * 
   * @return
   */
  public double[] getAmperage() {
    return new double[]{innerStage.getOutputCurrent(), outerStage.getOutputCurrent()};
  }

  /**
   * Sets the encoder position of both masts to 0
   */
  public void zeroEncoders() {
    innerStage.getSensorCollection().setQuadraturePosition(0, 0);
    outerStage.getSensorCollection().setQuadraturePosition(0, 0);
  }

}
