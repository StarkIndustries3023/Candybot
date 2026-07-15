// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveMode;

public class Drivetrain extends SubsystemBase {

  TalonFX leftMotor = new TalonFX(1);
  TalonFX rightMotor = new TalonFX(2);

  DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Constants.TRACK_WIDTH);


  private final GenericEntry leftVelocityEntry =
    Shuffleboard.getTab("Drive").add("Left Velocity", 0).getEntry();
  private final GenericEntry rightVelocityEntry = 
    Shuffleboard.getTab("Drive").add("Right Velocity", 0).getEntry();
  private final GenericEntry leftDesiredVelocityEntry =
    Shuffleboard.getTab("Drive").add("Desired Left Velocity", 0).getEntry();
  private final GenericEntry rightDesiredVelocityEntry =
    Shuffleboard.getTab("Drive").add("Desired Right Velocity", 0).getEntry();
  private final GenericEntry leftVoltageEntry =
    Shuffleboard.getTab("Drive").add("Left Voltage", 0).getEntry();
  private final GenericEntry rightVoltageEntry =
    Shuffleboard.getTab("Drive").add("Right Voltage", 0).getEntry();


  private final GenericEntry pEntry = 
    Shuffleboard.getTab("Drive").add("Drive P", Constants.DRIVE_P).getEntry();
  private final GenericEntry sEntry = 
    Shuffleboard.getTab("Drive").add("Drive S", Constants.DRIVE_S).getEntry();
  private final GenericEntry vEntry = 
    Shuffleboard.getTab("Drive").add("Drive V", Constants.Drive_V).getEntry();

  private final SendableChooser<DriveMode> driveModeChooser = new SendableChooser<>();

  

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = Constants.DRIVE_P;
    config.Slot0.kS = Constants.DRIVE_S;
    config.Slot0.kV = Constants.Drive_V;
    config.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.5;
    config.Feedback.SensorToMechanismRatio = Constants.DRIVE_GEAR_RATIO / (Math.PI * Constants.WHEEL_DIAMETER);

    rightMotor.getConfigurator().apply(config);

    config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    leftMotor.getConfigurator().apply(config);



    driveModeChooser.setDefaultOption("Curvature", DriveMode.CURVATURE);
    driveModeChooser.addOption("Voltage", DriveMode.VOLTAGE);

    Shuffleboard.getTab("Drive").add("Drive Mode", driveModeChooser);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    leftVoltageEntry.setDouble(leftMotor.getMotorVoltage().getValueAsDouble());
    rightVoltageEntry.setDouble(rightMotor.getMotorVoltage().getValueAsDouble());

    leftVelocityEntry.setDouble(leftMotor.getVelocity().getValueAsDouble());
    rightVelocityEntry.setDouble(rightMotor.getVelocity().getValueAsDouble());

    leftDesiredVelocityEntry.setDouble(leftMotor.getClosedLoopReference().getValueAsDouble());
    rightDesiredVelocityEntry.setDouble(rightMotor.getClosedLoopReference().getValueAsDouble());
  }

  /**
   * Drive in curvature mode
   * @param throttle velocity of robot centroid
   * @param curvature curvature in 1/m
   */
  public void driveCurvature(double throttle, double curvature){
    double velocity = throttle * Constants.MAX_SPEED;
    double omega = velocity * curvature * Constants.MAX_CURVATURE;
    
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(velocity, 0, omega);
    DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);


    leftMotor.setControl(
      new VelocityVoltage(wheelSpeeds.leftMetersPerSecond)
    );

    rightMotor.setControl(
      new VelocityVoltage(wheelSpeeds.rightMetersPerSecond)
    );
  }

  /**
   * Run direct voltage control of drivetrain
   * @param voltageLeft left motor Volts
   * @param voltageRight right motor Volts
   */
  public void driveRaw(double voltageLeft, double voltageRight){
    leftMotor.setVoltage(voltageLeft);
    rightMotor.setVoltage(voltageRight);
  }

  /**
   * Drive in percent output mode
   * @param leftSpeed left motor percent output
   * @param rightSpeed right motor percent outpu
   */
  public void driveDutyCycle(double leftSpeed, double rightSpeed){
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
  }

  /**
   * Refreshes the PID values if they have changed on the Network Tables
   */
  public void resetPID(){
    Slot0Configs config = new Slot0Configs();
    config.kP = pEntry.getDouble(Constants.DRIVE_P);
    config.kS = sEntry.getDouble(Constants.DRIVE_S);
    config.kV = vEntry.getDouble(Constants.Drive_V);

    leftMotor.getConfigurator().apply(config);
    rightMotor.getConfigurator().apply(config);

  }

  /**
   * Gives the selected drive mode from the Network tables
   * @return the selected mode
   */
  public DriveMode getDriveMode(){
    return driveModeChooser.getSelected();
  }
  
}
