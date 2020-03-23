package com.ipen.test;

public class NewClass {
  public static void main(String args[]) {
	  try
      { 
          // Just one line and you are done !  
          // We have given a command to start cmd 
          // /K : Carries out command specified by string 
		  String cmd1="mode con:cols=80 lines=40";
		  String command = "cmd /c start cmd.exe /K \" " + cmd1 + "\" ";
		  Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
       //  Runtime.getRuntime().exec("mode con:cols=80 lines=40");
      } 
      catch (Exception e) 
      { 
          System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
          e.printStackTrace(); 
      } 
  }
}