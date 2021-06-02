package com.alex.ubc.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.alex.ubc.rest.RESTGear;
import com.alex.ubc.utils.UsefulMethod;
import com.alex.ubc.utils.Variables;
import com.alex.ubc.utils.Variables.requestType;

/**********************************
* To send request
* 
* @author RATEL Alexandre
**********************************/
public class Action
	{
	/**
	 * Variables
	 */
	
	
	public static void gooo()
		{
		/**
		 * We get the source files
		 */
		Variables.setSourceDirectory(Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("sourcedirectory")+"/");
		ArrayList<File> fileList = UsefulMethod.getFilesToProcess(Variables.getSourceDirectory());
		
		FileReader fileReader= null;
		BufferedReader tampon  = null;
		for(File file : fileList)
			{
			try
				{
				Variables.getLogger().debug("Processing file "+file.getName());
				
				fileReader = new FileReader(file);
				tampon = new BufferedReader(fileReader);
				String inputLine = new String();
				ArrayList<String> toProcess = new ArrayList<String>();
				
				while (((inputLine = tampon.readLine()) != null) && (inputLine.compareTo("") !=0))
		        	{
		        	toProcess.add(inputLine);
		        	}
				
				Variables.getLogger().debug("Found "+toProcess.size()+" line to process");
				
				/**
				 * We inject each line
				 */
				String ip = UsefulMethod.getTargetOption("uccxhost");
				String port = UsefulMethod.getTargetOption("uccxport");
				String login = UsefulMethod.getTargetOption("uccxusername");
				String password = UsefulMethod.getTargetOption("uccxpassword");
				for(String s : toProcess)
					{
					Variables.getLogger().debug("Injecting :"+s);
					
					String content = "<csq>\r\n" + 
							"	<name>"+s+"</name>\r\n" + 
							"	<queueType>VOICE</queueType>\r\n" + 
							"	<routingType>VOICE</routingType>\r\n" + 
							"	<queueAlgorithm>FIFO</queueAlgorithm>\r\n" + 
							"	<autoWork>false</autoWork>\r\n" + 
							"	<wrapupTime>0</wrapupTime>\r\n" + 
							"	<resourcePoolType>SKILL_GROUP</resourcePoolType>\r\n" + 
							"	<serviceLevel>5</serviceLevel>\r\n" + 
							"	<serviceLevelPercentage>70</serviceLevelPercentage>\r\n" + 
							"	<poolSpecificInfo>\r\n" + 
							"		<skillGroup>\r\n" + 
							"			<selectionCriteria>Longest Available</selectionCriteria>\r\n" + 
							"		</skillGroup>\r\n" + 
							"	</poolSpecificInfo>\r\n" + 
							"</csq>";
					
					RESTGear.send(requestType.POST, "https://"+ip+":"+port+"/adminapi/csq", content, login, password, 100);
					}
				}
			catch(Exception exc)
				{
				Variables.getLogger().error("Error : "+exc.getMessage(),exc);
				}
			}
		}
	/*2021*//*RATEL Alexandre 8)*/
	}
	
