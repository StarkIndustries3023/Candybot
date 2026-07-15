// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.DriveMode;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Spinner;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class JoystickDrive extends Command {
  Drivetrain drivetrain;
  CommandXboxController controller;
  Spinner spinner;

  /** Creates a new JoystickDrive. */
  public JoystickDrive(Drivetrain drivetrain, Spinner spinner, CommandXboxController controller) {
    this.drivetrain = drivetrain;
    this.controller = controller;
    this.spinner = spinner;
    addRequirements(drivetrain, spinner);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    DriveMode mode = drivetrain.getDriveMode();

    if(mode == DriveMode.CURVATURE){
      drivetrain.driveCurvature(controller.getLeftY() * -1, controller.getRightX() * -1);
    }else if(mode == DriveMode.VOLTAGE){
      double leftVolts = controller.getLeftY() * -1;
      double rightVolts = controller.getRightY() * -1;
      drivetrain.driveRaw(leftVolts, rightVolts);
    }
    
    spinner.setSpeed(controller.getRightTriggerAxis());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveRaw(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
