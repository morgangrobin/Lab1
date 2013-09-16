import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 200;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	final int DESIRED_DISTANCE = 25;
	final int DEADBAND = 3;
	final int DELTA = 70;
	final int SENSOR_ANGLE = -40;
	final int ZERO = 0;
	
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		//Run the next line once every time you start working
		//Motor.B.rotateTo(SENSOR_ANGLE);
	}
	
	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		// TODO: process a movement based on the us distance passed in (BANG-BANG style)
		int error = distance-DESIRED_DISTANCE;
		
		if(Math.abs(error)<=DEADBAND){ //If the error is small enough, do nothing
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
			rightMotor.forward();
			leftMotor.forward();
		}
		else if(error<0){ //If the robot is too close, STOP, then turn right
			rightMotor.setSpeed(ZERO);
			leftMotor.setSpeed(motorStraight+DELTA);
			leftMotor.forward();
		}
		else{ //If the robot is too far, turn left
			rightMotor.setSpeed(motorStraight+DELTA);
			leftMotor.setSpeed(motorStraight-DELTA);
			rightMotor.forward();
			leftMotor.forward();
		}
		
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
