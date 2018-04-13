package helloworld;

import modules.*;
import lejos.hardware.Button;

public class TestModules {
	
	private static String getColor(int color) {
		String colorName;
		switch ((color)) {
	    case -1: colorName="undefined color ";
	    break;
	    case 0: colorName="Red";
	    break;
	    case 1: colorName="Green";
	    break;
	    case 2: colorName="Blue";
	    break;
	    case 3: colorName="Yellow";
	    break;
	    case 4: colorName="Magenta";
	    break;
	    case 5: colorName="Orange";
	    break;
	    case 6: colorName="White";
	    break;
	    case 7: colorName="Black";
	    break;
	    case 8: colorName="Pink";
	    break;
	    case 9: colorName="Gray";
	    break;
	    case 12: colorName="Cyan";
	    break;
	    case 13: colorName="Brown";
	    break;
	    default: colorName="inconnu";
	    }
		
		return colorName;
	}

	public static void main(String[] args) {
		LegMotor moteur0=new LegMotor(0);
		LegMotor moteur1=new LegMotor(1);
		ColorSensor infra= new ColorSensor();
		GyroSensor gyro=new GyroSensor();
		int number=0;
		while(!Button.ESCAPE.isDown()) {
			
			switch(number) {
				case 0:
					System.out.println("Color:" + getColor((int) infra.getColor()) + "/n");
					break;
				case 1:
					System.out.println("Gyro Angle:" + gyro.getAngle() + "/n");
					break;
				case 2:
					System.out.println("Gyro Angular Speed:" + gyro.getAngularSpeed() + "/n");
					break;
				case 3:
					System.out.println("Motor 0 Speed:" + moteur0.getSpeed() + "/n");
					break;
				case 4:
					System.out.println("Motor 1 Speed:" + moteur1.getSpeed() + "/n");
					break;
			}
			
		}
	}

}
