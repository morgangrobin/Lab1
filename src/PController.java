import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 300, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	final int DESIRED_DISTANCE = 25;
	final int DEADBAND = 3;
	final int DELTA = 70;
	final int SENSOR_ANGLE = -40;
	final int ZERO = 0;
	
	public PController(int bandCenter, int bandwith) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
		
	}
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		// TODO: process a movement based on the us distance passed in (P style)
		//want a method that will correct error more quickly if the robot is farther away
		//from where it's supposed to be. **I think**
		int error = distance-DESIRED_DISTANCE;
		int DELTA = (200*error)/255;
		
		if(Math.abs(error)<=DEADBAND){ //If the error is small enough, do nothing
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
			rightMotor.forward();
			leftMotor.forward();
		}
		else if(error<0){ //If the robot is too close, STOP, then turn right
			rightMotor.setSpeed(motorStraight-DELTA/2);
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
