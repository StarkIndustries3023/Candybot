// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Commands.JoystickDrive;
import frc.robot.Commands.VoltageTest;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Spinner;

public class RobotContainer {

  Drivetrain drivetrain = new Drivetrain();
  Spinner spinner = new Spinner();

  CommandXboxController controller = new CommandXboxController(0);

  JoystickDrive joystickDrive = new JoystickDrive(drivetrain, spinner, controller);
  VoltageTest voltageTest = new VoltageTest(drivetrain, controller);

  public RobotContainer() {
    configureBindings();
    drivetrain.setDefaultCommand(joystickDrive);
  }

  private void configureBindings() {
    controller.leftBumper().whileTrue(voltageTest);

    controller.back().onTrue(Commands.runOnce(drivetrain::resetPID, drivetrain));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
