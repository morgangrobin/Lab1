import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 300, FILTER_OUT = 200;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	final int DESIRED_DISTANCE = 20;
	final int DEADBAND = 3;
	final int SENSOR_ANGLE = -45;

	
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
		//Run the next line once every time you start working to set sensor angle
		//Motor.B.rotateTo(SENSOR_ANGLE);
		
	}
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter
		if (distance >= 90 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance >= 90){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		int error = distance-DESIRED_DISTANCE;
		int delta;
		//if the robot is too far away, set delta to be large
		if(error>15){
			delta = 100;
		}
		//otherwise scale delta linearly according to size of error
		else{ 
			delta = (100*error)/255;
		}
		
		if(Math.abs(error)<=DEADBAND){ //If the error is small enough, do nothing
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
			rightMotor.forward();
			leftMotor.forward();
		}
		else if(error<0){ //If the robot is too close, turn right
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight+delta);
			leftMotor.forward();
			rightMotor.backward();
		}
		else{ //If the robot is too far, turn left
			rightMotor.setSpeed(motorStraight+(2*delta));
			leftMotor.setSpeed(motorStraight);
			rightMotor.forward();
			leftMotor.forward();
		
		}
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
