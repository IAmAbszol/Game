package Controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class ControllerAdapter {

	/*
	 * Reference LWJGL, though its mostly for games, use it for only the controller
	 */
	
	// failure to build controller (Controller not found) it'll simply pass.
	// The enabled will still be checking if it does show
	public static boolean ENABLE_CONTROLLER = false;
	public static boolean CONTROLLER_BUILT = false;
	
	private static Controller controller;
	private static boolean Start;
	
	public static void main(String[] args) {
		
		 try {
			 
			 Controllers.create();
			 
		 } catch (LWJGLException e) {
			 e.printStackTrace();
		 }
		 
		 Controllers.poll();
		 
		 for(int i = 0; i < Controllers.getControllerCount(); i++) {
			 controller = Controllers.getController(i);
			 System.out.println(controller.getName());
		 }
		 
		 controller = Controllers.getController(0);
		 for(int i = 0; i < controller.getAxisCount(); i++) {
			 System.out.println(i + ": " + controller.getAxisName(i));
			 controller.setDeadZone(i,  (float)0.3);
		 }
		 
		 for(int i = 0; i < controller.getButtonCount(); i++) {
			 System.out.println(i + ": " + controller.getButtonName(i));
		 }
		 
		 while(true) {
			 controller.poll();
			 Start = controller.isButtonPressed(7);
			 System.out.println(controller.getAxisValue(0));
			 System.out.println(controller.getAxisValue(1));
			 System.out.println(controller.getAxisValue(2));
			 System.out.println(controller.getAxisValue(3));
			 System.out.println(controller.getAxisValue(4));
			 System.out.println();
		}
		
	}
	
}
