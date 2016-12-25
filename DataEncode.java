package encode;
import neural.gui.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DataEncode {

	
	public static String printBinaryFormat(int number,int length){

		  int arrlength = 0;
		    if(length<=8){
		    	
		    	arrlength = 3;
		    }
		    else if(length>8 && length<=16){
		    	
		    	arrlength = 4;
		    }
		    else if(length>16){
		    	
		    	arrlength = 6;
		    }
		    
		  

	        int binary[] = new int[arrlength];
            int reverse[] = new int[arrlength];
	        int index = 0;
 
	        while(number > 0){
              
	     
              binary[index] = number%2;
     
              index++;
	            number = number/2;
	        
	        }


	        String encode = "";
           int index1=0;            
	        for(int i = binary.length-1;i >= 0;i--){
	        	reverse[index1++]=binary[i];
	            encode = encode+binary[i];
	        }
	
           return encode;
	    }
	
	public static int findIndexInString(String temp,String array[]){
		 for(int k=0;k<array.length;k++){
	        	if(temp.equalsIgnoreCase(array[k])){
	        		return k;
	        	}
	        }
		return -1;
	}
	
	public  String enc(int n) throws IOException {
		long start_time =  System.currentTimeMillis();
		int numberOfLines;
		BufferedReader br;
		FileReader fr;
		// TODO Auto-generated method stub
		if(n==1)
		{
			 fr=new FileReader("test.data");
		 br = new BufferedReader(fr);
		 numberOfLines = DmGui.testSize; //MUST CHANGE ACCORDINGLY DATA SET
                 
		}
		else
		{
			 fr=new FileReader("train.data");
			
			 br = new BufferedReader(fr);
			 numberOfLines = DmGui.trainSize; //MUST CHANGE ACCORDINGLY DATA SET
                         
		}
		
		
		int numOfAttrib = 15;
		int indexInString = -1;
		String delimiter = ",";
		
		String[] textData = new String[numberOfLines];
		String[] attribData;
		
		String temp=null;
		String encodeData= "";
		String encodeAge = "";
                String encodeEdu = "";
                String encodeSex = "";
		String encodeHrsPerWeek= "";
		String encodeResult  = "";
		String[] education = {" Bachelors"," Some-college"," 11th"," HS-grad"," Prof-school"," Assoc-acdm",
				" Assoc-voc"," 9th"," 7th-8th"," 12th"," Masters"," 1st-4th"," 10th"," Doctorate"," 5th-6th"," Preschool"};
	
		
		for(int k=0;k<numberOfLines;k++)
		{
            
			textData[k] = br.readLine();//kth element is entire kth row
		
		    attribData = textData[k].split(delimiter);//split kth row into individual attrib
		    boolean continueflag = false;
		    for(int m=0;m<attribData.length;m++){//to search qsmark
		    
				 String tempAttrib = attribData[m];
				 String qsmark = " ?";
				  if(qsmark.equalsIgnoreCase(tempAttrib)){//if there is qsmark discard that row
					 continueflag = true;
				  }
		  }
		 
		 if(continueflag){
			k--;
                     continue;//discards the row
		 }
		
		
		encodeAge = "";
		encodeEdu = "";
                encodeSex = "";
		encodeHrsPerWeek= "";
		encodeResult  = "";
	
		
		for(int j=0;j<numOfAttrib;j++){
			switch(j){
			
			
			case 0:
				   int age = Integer.parseInt(attribData[j]);
				   
				
				 if(age>0 && age<=20){
					 encodeAge = encodeAge+"10000"; 
				 }
				 else if(age>20 && age<=40){
					 encodeAge = encodeAge+"01000";
					 
				 }
				 else if(age>40 && age<=60){
					 encodeAge = encodeAge+"00100"; 
				 }
				 else if(age>60 && age<=80){
					 encodeAge = encodeAge+"00010"; 
				 }
				 else if(age>80 && age<=100){
					 encodeAge = encodeAge+"00001"; 
				 }
				 break;
		
			case 3: temp = attribData[j];
		            indexInString = findIndexInString(temp, education);
		            encodeEdu = printBinaryFormat(indexInString,education.length);
		            break;
		
                        case 9:temp = attribData[j];
	                if(temp.equalsIgnoreCase("male")){
	                	encodeSex = "1";
	                }
	                else{
	                	encodeSex = "0";
	                }
	                break;
	                
	        
	         case 12:temp = attribData[j];
    	     		String newtemp1 = temp.trim();
    	     		int hrsperweek = Integer.parseInt(newtemp1);
			         
			       
					 if(hrsperweek>0 && hrsperweek<=20){
						 encodeHrsPerWeek = encodeHrsPerWeek+"10000"; 
					 }
					 else if(hrsperweek>20 && hrsperweek<=40){
						 encodeHrsPerWeek = encodeHrsPerWeek+"01000"; 
					 }
					 else if(hrsperweek>40 && hrsperweek<=60){
						 encodeHrsPerWeek = encodeHrsPerWeek+"00100"; 
					 }
					 else if(hrsperweek>60 && hrsperweek<=80){
						 encodeHrsPerWeek = encodeHrsPerWeek+"00010"; 
					 }
					 else if(hrsperweek>80 && hrsperweek<=100){
						 encodeHrsPerWeek = encodeHrsPerWeek+"00001"; 
					 }
					 break;
					 
			
			 case 14:temp = attribData[j];
			         String newtemp2 = temp.trim();
			         String tempstr  = ">50K.";
			         String tempstr2 = ">50K";
		             if(tempstr.equalsIgnoreCase(newtemp2)||tempstr2.equalsIgnoreCase(newtemp2)){
			             	encodeResult = encodeResult+"1";
			             }
			             else{
			             	encodeResult = encodeResult+"0";
			             }
		              break;
			}
	        temp = encodeAge+encodeEdu+encodeSex+encodeHrsPerWeek+encodeResult;
		 }
		encodeData=encodeData+temp;
	  }
		long end_time = System.currentTimeMillis();
		double difference = (end_time - start_time)/1e3;
		System.out.println("total time="+difference);
                System.out.println(encodeData.length()+"length");
		return encodeData;
  }
}



