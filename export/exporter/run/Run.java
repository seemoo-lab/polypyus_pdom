package run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.HashMap;
import org.eclipse.cdt.internal.core.pdom.PDOM;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNamedNode;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMProjectIndexLocationConverter;
import SQL.DBController;
import SQL.PDOM_export;


public class Run {
	
	public static PrintStream Exceptions_logFile;	
	
	public static void main(String args[]){
		
		FileOutputStream fos;
		try {
			String ProjectPath = System.getProperty("user.dir"); //$NON-NLS-1$
			Files.deleteIfExists(new File(ProjectPath + "/Exceptions.log").toPath()); //$NON-NLS-1$
			fos = new FileOutputStream(new File(ProjectPath + "/Exceptions.log"), true); //$NON-NLS-1$
			Exceptions_logFile = new PrintStream(fos); 
		} catch (IOException e) {
			e.printStackTrace();
		}   		
		
		boolean debug = false;
		HashMap<String, String> options = new HashMap<String, String>();
		
		for(int i = 0; i < args.length;i++) {
			if(args[i].equals("-S")) { //$NON-NLS-1$
				options.put("-S", args[i+1]); //$NON-NLS-1$
				i = i+1;				
			}
			else if(args[i].equals("-D")) { //$NON-NLS-1$
				options.put("-D", args[i+1]); //$NON-NLS-1$
				i = i+1;
			}else if(args[i].equals("-P")) { //$NON-NLS-1$
				String inPut = args[i+1];
				options.put("-P", inPut); //$NON-NLS-1$
				i = i+1;
			}else {
				options.put(args[i], "1"); //$NON-NLS-1$
			}
		}
		
		try {
			
			if(options.containsKey("-h")) { //$NON-NLS-1$
				PDOM_Printer.printHelp();
				return;
			}
			
			String PDOMPath = options.get("-P"); //$NON-NLS-1$
			File PDOMFile = new File(PDOMPath);
			PDOM pdom = new PDOM(PDOMFile, new PDOMProjectIndexLocationConverter() , PDOM_Init.getinkageFactoryMap());
			HashMap<String, PDOMNamedNode> SymbolList = PDOM_Init.init_PDOMSymbols(pdom);					
				

			if(options.containsKey("-H")) { //$NON-NLS-1$
				PDOM_Printer.printHeader(pdom);
				return;
			}				
			
			if(options.containsKey("-f")) //$NON-NLS-1${
			{
				PDOM_Printer.print_File_Index(pdom);
				return;
			}
				
			
			if(options.containsKey("-l")) { //$NON-NLS-1$				
				PDOM_Printer.printLinkages(pdom, true, true, false);
				return;
			}
				
			
			String DatabasePath = options.get("-D"); //$NON-NLS-1$
			if(DatabasePath == "" || DatabasePath == null) //$NON-NLS-1$
				DatabasePath = System.getProperty("user.dir"); //$NON-NLS-1$
							 						 
			System.out.println("start PDOM export: " + PDOMFile.getName()); //$NON-NLS-1$	
			String override = null;
			override = options.get("-o"); //$NON-NLS-1$
			if(override == null)
				override = ""; //$NON-NLS-1$
			DBController controll = DBController.createNewDatabase(DatabasePath +"/" + PDOMFile.getName() + ".db", (override.equals("1"))); //$NON-NLS-1$ //$NON-NLS-2$ 		
			PDOM_export Exporter = new PDOM_export(controll, 100);	
						
			String SymbolPath = options.get("-S"); //$NON-NLS-1$
			
			if(SymbolPath == "" || SymbolPath == null) { //$NON-NLS-1$
				Exporter.mapAllSymbols(SymbolList,debug);
				controll.closeConnection();
				return;
			}
			else
				Exporter.mapSymbolsFromFile(SymbolPath, SymbolList, debug);
				
			controll.closeConnection();		
			
		}catch(Exception exc) {
			System.out.println("Something is wrong, please check your options"); //$NON-NLS-1$
			options.forEach((x,y) -> System.out.println(x + ": " + y)); //$NON-NLS-1$
			exc.printStackTrace(Exceptions_logFile);
			PDOM_Printer.printHelp();
		}
		
		
	}
	
	
}
