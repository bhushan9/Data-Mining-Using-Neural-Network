/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writefile;

import bpn.*;
import neural.gui.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author PROBOOK
 */
public class WriteOutput {
	public  void writeEncode(int x[][],int option) {
		try {
                        File file;
			FileWriter fw;
			BufferedWriter bw;                    
                        int buffersize = 1024*8;
                        
                switch(option){
                 case 1:
			file = new File("trainencoded.txt");
                      
			//if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

		        fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw,buffersize);
                        bw.write("Encoded Data:");
                        bw.newLine();
			 for (int i = 0; i < DmGui.trainSize; i++) {
                             for (int j = 0; j < 16; j++) {
                               bw.write(x[i][j]+"");         
                             }
                               bw.newLine();
                            }
                         bw.flush(); 
                         bw.close();
                         break;
                         
                 case 2:file = new File("testencoded.txt");
                      
			//if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw,buffersize);
                        bw.write("Encoded Data:");
                        bw.newLine();
			 for (int i = 0; i < DmGui.testSize; i++) {
                             for (int j = 0; j < 16; j++) {
                               bw.write(x[i][j]+"");         
                             }
                               bw.newLine();
                            }   
                         bw.flush(); 
                         bw.close();
                        break;
                   }    
          
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        
       public void writeTestOp(String x[]){
            try{
                     
                      int buffersize = 1024*8;
                      File file = new File("testOp.txt");
                      
			//if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
                        
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw,buffersize);
                     for (int i = 0; i < DmGui.testSize; i++) {
                       bw.write(x[i]);
                       bw.newLine();
                }
                         
                        //  bw.newLine();
                        bw.flush();
                        bw.close();
            }
            catch (IOException e) {
			e.printStackTrace();
		}
        }
 
}