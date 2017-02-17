package org.usfirst.frc.team6213.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	double maxSpeed = 0.4; // Driving Speed Adjustments
	double shooterSpeed = 1.0; // Shooter Speed Adjustments
	double fMove; // RTrigger Value
	double rMove; // LTrigger Value
	double turn; // Left Stick X Axis For Turning Robot
	double climb; // Speed of the Climber from Right Stick Y Axis
	boolean aButton; // A Button For Switching Drive Speed
	boolean bButton; //  B Button For Activating Shooter
	boolean xButton; // X Button For Activating Shooter In Other Direction
	boolean rightBumper; // Right Bumper Increases Shooter Speed
	boolean leftBumper; //  Left Bumper Decreases Shooter Speed
	Timer timer; // Timer
	Spark leftM; // Left Wheels
	Spark rightM; // Right Wheels
	Spark shootM; // Shooter
	Spark climbM; // Climber
	Joystick controller; // Xbox Controller
	RobotDrive rDrive; // RobotDrive Object

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		timer = new Timer();
		leftM = new Spark(0);
		rightM = new Spark(1);
		shootM = new Spark(2);
		climbM = new Spark(3);
		controller = new Joystick(0);
		rDrive = new RobotDrive(leftM,rightM);
		rDrive.setSafetyEnabled(false);
		UsbCamera cam0 = CameraServer.getInstance().startAutomaticCapture(); // Simplified Camera
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		fMove = controller.getRawAxis(3); // Right Trigger
		rMove = controller.getRawAxis(2); // Left Trigger
		turn = controller.getRawAxis(0); // Left Stick
		climb = controller.getRawAxis(5); // TBD, right stick Y
		aButton = controller.getRawButton(1); // Robot Speed
		bButton = controller.getRawButton(2); // B - Shooter Out
		xButton = controller.getRawButton(4); // X - Shooter In
		rightBumper = controller.getRawButton(6); // RB - Shoot Speed Increase
		leftBumper = controller.getRawButton(5); // LB - Shoot Speed Decrease
		
		if(fMove > 0){ // Drive forwards
			rDrive.drive(fMove * -maxSpeed, turn);
		}
		
		else if(rMove > 0){ // Drive backwards
			rDrive.drive(rMove * maxSpeed, turn);
		}
		
		else if(climb > 0.1 || climb < -0.1){ // Climb Control
			climbM.set(-(climb));
			climbM.stopMotor(); // May need to be removed
		}
		
		else if(aButton){ //Set Max Speed
			if(maxSpeed == 0.4){
				maxSpeed = 1;
				timer.delay(0.25);
			}
			else{
				maxSpeed = 0.4;
				timer.delay(0.25);
			}
		}
		
		else if(bButton){ //Shoot
			shootM.set(shooterSpeed);
		}
		
		else if(xButton){ //Shooter Reverse
			shootM.set(-shooterSpeed);
		}
		
		else if(rightBumper){ // Increase shooter speed
			if (shooterSpeed < 1.0){
				shooterSpeed += 0.1;
				timer.delay(0.3);
				
			}
		}
		
		else if(leftBumper){ // Decrease shooter speed
			if(shooterSpeed > 0.0){
				shooterSpeed -= 0.1;
				timer.delay(0.3);
			}
		}
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

