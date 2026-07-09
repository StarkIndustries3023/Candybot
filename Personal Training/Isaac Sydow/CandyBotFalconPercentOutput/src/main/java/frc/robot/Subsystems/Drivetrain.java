// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  TalonFX leftMotor = new TalonFX(1);
  TalonFX rightMotor = new TalonFX(2);

  /** Creates a new Drivetrain. */
  public Drivetrain() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /** Sets the drive motors to a desired percent output
   * @param leftSpeed left motor speed
   * @param rightSpeed right motor speed
   */
  public void drive(double leftSpeed, double rightSpeed){
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
  }

  
}
