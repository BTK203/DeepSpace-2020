/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 inner. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the inner BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Enumeration.MastPosition;
import frc.robot.Util.Util;

public class IterativeCommandMoveMast extends Command {

  private static Boolean      stable;
  private static Boolean      loopRunning;

  private static double       innerStageSpeed;
  private static double       outerStageSpeed;

  private static MastPosition position;

  public IterativeCommandMoveMast() {
    requires(Robot.SUB_MAST);
  }

  // Called just before this Command runs the inner time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    position = Robot.SUB_MAST.getStoredPosition();
    loopRunning = Robot.SUB_MAST.getLoopRunning();

    innerStageSpeed = Math.abs(Util.getAndSetDouble("Inner Stage Speed", Constants.INNER_STAGE_SPEED));
    outerStageSpeed = Math.abs(Util.getAndSetDouble("Outer Stage Speed", Constants.OUTER_STAGE_SPEED));
    
    switch(position) {
      case SOMEWHERE:
      case HATCH_1:
        if (!Robot.SUB_MAST.getLimitSwitches()[0]) { Robot.SUB_MAST.moveInnerStageByPercent(-1 * innerStageSpeed); } // ram inner stage down if not already
        if (!Robot.SUB_MAST.getLimitSwitches()[2]) { Robot.SUB_MAST.moveOuterStageByPercent(-1 * outerStageSpeed); } // ram outer stage down if not already
        stable = Robot.SUB_MAST.getLimitSwitches()[0] && Robot.SUB_MAST.getLimitSwitches()[2];
        break;

      case CARGO_1:
        if (!Robot.SUB_MAST.getLimitSwitches()[2]) { Robot.SUB_MAST.moveOuterStageByPercent(-1 * outerStageSpeed); } // ram outer stage down if not already
        if (!loopRunning) {
          loopRunning = true;
          Robot.SUB_MAST.moveInnerStageByPosition(Constants.CARGO_1_HEIGHT); // move inner stage to x position
        }
        stable  = Robot.SUB_MAST.getLimitSwitches()[2] && Robot.SUB_MAST.innerStageWithinRange(Constants.CARGO_1_HEIGHT);
        break;

      case HATCH_2:
        if (!Robot.SUB_MAST.getLimitSwitches()[2]) { Robot.SUB_MAST.moveOuterStageByPercent(-1 * outerStageSpeed); } // ram outer stage down if not already
        if (!loopRunning) {
          loopRunning = true;
          Robot.SUB_MAST.moveInnerStageByPosition(Constants.HATCH_2_HEIGHT); // move inner stage to x position
        }
        stable = Robot.SUB_MAST.getLimitSwitches()[2] && Robot.SUB_MAST.innerStageWithinRange(Constants.HATCH_2_HEIGHT);
        break;

      case CARGO_2:
        if (!Robot.SUB_MAST.getLimitSwitches()[1]) { Robot.SUB_MAST.moveInnerStageByPercent(innerStageSpeed); } // ram inner stage up if not already
        if (!loopRunning) {
          loopRunning = true;
          Robot.SUB_MAST.moveOuterStageByPosition(Constants.CARGO_2_HEIGHT); // move outer stage to x position
        }
        stable = Robot.SUB_MAST.getLimitSwitches()[2] && Robot.SUB_MAST.outerStageWithinRange(Constants.CARGO_2_HEIGHT);
        break;

      case HATCH_3:
        if (!Robot.SUB_MAST.getLimitSwitches()[1]) { Robot.SUB_MAST.moveInnerStageByPercent(innerStageSpeed); } // ram inner stage up if not already
        if (!loopRunning) {
          loopRunning = true;
          Robot.SUB_MAST.moveOuterStageByPosition(Constants.HATCH_3_HEIGHT); // move outer stage to x position
        }
        stable = Robot.SUB_MAST.getLimitSwitches()[1] && Robot.SUB_MAST.outerStageWithinRange(Constants.HATCH_3_HEIGHT);
        break;

      case CARGO_3:
        if (!Robot.SUB_MAST.getLimitSwitches()[1]) { Robot.SUB_MAST.moveInnerStageByPercent(innerStageSpeed); } // ram inner stage up if not already
        if (!Robot.SUB_MAST.getLimitSwitches()[3]) { Robot.SUB_MAST.moveOuterStageByPercent(outerStageSpeed); } // ram outer stage up if not already
        stable = Robot.SUB_MAST.getLimitSwitches()[1] && Robot.SUB_MAST.getLimitSwitches()[3];
        break;
    }

    SmartDashboard.putBoolean("Stable Mast", stable);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  private Boolean innerStageInSafeRange() {
    // return Robot.SUB_MAST.getInnerStageInches() < Constants.INNER_STAGE_MAX_HEIGHT;
    // TODO finish
    return true;
  }

  private Boolean outerStageInSafeRange() {
    return true;
  }
}
