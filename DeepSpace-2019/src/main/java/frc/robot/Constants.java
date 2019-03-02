/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * A place to store magic numbers
 */
public class Constants {
    
    /**
     * Advanced Shifting Values
     */
    public static final double
        QUARTER_UPSHIFT_RPM              = 6000,
            QUARTER_DOWNSHIFT_RPM        = 4000,
        HALF_UPSHIFT_RPM                 = 5000,
            HALF_DOWNSHIFT_RPM           = 3000,
        THREE_QUARTERS_UPSHIFT_RPM       = 4000,
            THREE_QUARTERS_DOWNSHIFT_RPM = 2000,
        FULL_UPSHIFT_RPM                 = 3000,
            FULL_DOWNSHIFT_RPM           = 1000;

    /** 
     * Docking Values
     */
    public static final double
        DOCKING_TARGET_LOCK_RANGE = 50; // in inches

    /**
     * Drive values
     */
    public static final double
        BACKUP_RAMP          = 0,
        BACKUP_DOCKING_SPEED = .25,
        MAX_ALLOWABLE_AO     = .2, // percent output
        MAX_ALLOWABLE_ERROR  = .25, // inches
        DOWNSHIFT_RPM        = 900,
        UPSHIFT_RPM          = 1000;

    /**
     * Inverts
     */
    public static final Boolean
        LEFT_DRIVE_INVERT          = false,
        RIGHT_DRIVE_INVERT         = true,
        INNER_STAGE_INVERT         = false,
        INNER_STAGE_ENCODER_INVERT = false,
        OUTER_STAGE_INVERT         = false,
        OUTER_STAGE_ENCODER_INVERT = false,
        LAUNCHER_INVERT            = true,
        INTAKE_INVERT              = false,
        FLIPPER_INVERT             = false;

    /**
     * Mast Values
     */
    public static final double
        MAST_ALLOWABLE_ERROR = .5, // error in inches
        NOT_QUITE_ZERO = .5,
        CARGO_1_HEIGHT = 10.5,
        HATCH_2_HEIGHT = 28,
        CARGO_2_HEIGHT = 7.5,
        HATCH_3_HEIGHT = 25, // TODO these need to be calibrated
        CARGO_3_HEIGHT = 35.5,
        TOP_TIER_INNER_HEIGHT = 31;
        // CARGO 3 HEIGHT = 33.5 on stage 2 (if limit doesn't work)

    /**
     * Mast Speed Backup Values
     */
    public static final double
        INNER_STAGE_SPEED  = .5,
        OUTER_STAGE_SPEED = .5;

    /**
     * Per values
     */
    public static final double
        CTRE_ENCODER_TICKS_PER_ROTATION = 4096,
        REV_ENCODER_TICKS_PER_ROTATION = 42,
        INNER_MAST_TICKS_PER_INCH = 39560,//was39530
        OUTER_MAST_TICKS_PER_INCH = 39740,//was39990
        ROTATIONS_PER_INCH = 1 / (4.67 * 6 *  Math.PI); // calculated with a cimple box and 42 TPR

    /**
     * PID backup values (safety values)
     */
    public static final double
        BACKUP_POSITION_kP = 0,
        BACKUP_POSITION_kI = 0,
        BACKUP_POSITION_kD = 0,
        BACKUP_DOCKING_kP  = 0,
        BACKUP_DOCKING_kI  = 0,
        BACKUP_DOCKING_kD  = 0;

    /**
     * Safety Values
     */
    public static final int
        DANGER_AMPERAGE        = 55,
        PUSHING_AMPERAGE       = 30,
        FLIPPER_STALL_AMPERAGE = 25;
        
    /**
     * Solenoid IDS
     */
    public static final int
        CLOSE_CLAMP_ID = 1,
        OPEN_CLAMP_ID  = 0,
        DOWNSHIFT_ID   = 7,
        UPSHIFT_ID     = 6,
        EXTEND_ID      = 2,
        RETRACT_ID     = 3;
        
    /**
     * Spark IDs
     */
    public static final int
        LEFT_MASTER_ID  = 4,
        LEFT_SLAVE_ID   = 3,
        RIGHT_MASTER_ID = 1,
        RIGHT_SLAVE_ID  = 2;

    /**
     * Talon IDS
     */
    public static final int
    // PRACTICE
        // CLIMBER_ID      = 5,
        // INNER_STAGE_ID  = 8,
        // OUTER_STAGE_ID  = 9,
        // LAUNCHER_ID     = 6,
        // INTAKE_ID       = 10,
        // FLIPPER_ID      = 7;
    // COMPETITION
        CLIMBER_ID      = 8,
        INNER_STAGE_ID  = 6,
        OUTER_STAGE_ID  = 7,
        LAUNCHER_ID     = 9,
        INTAKE_ID       = 10,
        FLIPPER_ID      = 5;


    /**
     * Vision Values
     */
    public static final int
        CAM_HEIGHT      = 1080,
        CAM_WIDTH       = 1920,
        ACAM_ID         = 0,
        BCAM_ID         = 1,
        CCAM_ID         = 2,
        BACKUP_EXPOSURE = 35;

}
