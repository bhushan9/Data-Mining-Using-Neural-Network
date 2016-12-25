package bpn;
import encode.*;

import neural.gui.*;
import writefile.*;
import java.util.Random;
import java.io.IOException;
import java.text.DecimalFormat;

public class BPN
{
	public static int array2dtest[][];//Used to store encoded data 

	public static int number=0; //Used as a index for storing final activation values of test data
    private static final int INPUT_NEURONS = 15;
    private static final int HIDDEN_NEURONS = 8;
    private static final int OUTPUT_NEURONS = 1;

    public static  double LEARN_RATE = DmGui.learn_rate*0.1 ;    // Rho.
    public static  int TRAINING_REPS = DmGui.training_reps*1000;

    // Input to Hidden Weights (with Biases).
    public static double wih[][] = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];

    // Hidden to Output Weights (with Biases).
    public static double who[][] = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];

    // Activations.
    private static double inputs[] = new double[INPUT_NEURONS];
    private static double hidden[] = new double[HIDDEN_NEURONS];
    private static double target[] = new double[OUTPUT_NEURONS];
    private static double actual[] = new double[OUTPUT_NEURONS];

    // Unit errors.
    private static double erro[] = new double[OUTPUT_NEURONS];
    private static double errh[] = new double[HIDDEN_NEURONS];

    private static  int MAX_SAMPLES=DmGui.trainSize;  //MUST CHANGE ACCORDINGLY DATA SET
    public static double finalActivationVal[]; //Activation value of test data

    
 
    public static  void NeuralNetworkTrain(String data) //String data is in encoded form
    {
        //System.out.println("learning rate is : "+LEARN_RATE);
        //System.out.println("learning rate is : "+TRAINING_REPS);
    	int rows,col;
    	rows=DmGui.trainSize;  // change according to data
    	col=16;
    	MAX_SAMPLES=rows;
    	
    	System.out.println(DmGui.trainSize+"Training size");
    	System.out.println(rows+" "+col+"yo");
    	int trainInputs[] = new int[data.length()];
    	String dataArray[] = data.split("(?!^)");
    	//System.out.println(dataArray.length+"data array legnth");
    	for(int i=0;i<dataArray.length;i++){
    		trainInputs[i] = Integer.parseInt(dataArray[i]);
    	}
    	
    	System.out.println(trainInputs.length+" traininput length");
    	
    	int array2d[][] = new int[rows][col];  //adjust the boundaries properly //MUST CHANGE ACCORDINGLY DATA SET


    	for(int i=0; i<rows;i++)
    	{
    		for(int j=0;j<col;j++)
    		{	
                   // System.out.println((i*col+j));
    	       array2d[i][j] = trainInputs[(i*col) + j];   // Encoded data is stored
    		}
    	}	
        //write encoded data to output file
        WriteOutput wo = new WriteOutput();
        wo.writeEncode(array2d,1);
        
        int sample = 0;

        assignRandomWeights();

        // Train the network.
        for(int epoch = 0; epoch < TRAINING_REPS; epoch++)
        {
           sample += 1;
            if(sample == MAX_SAMPLES-1){
                sample = 0;
            }

            for(int i = 0; i < INPUT_NEURONS; i++)
            {
            	//System.out.println(sample+","+i);
                inputs[i] = array2d[sample][i];
            } // i

            for(int i = 0; i < OUTPUT_NEURONS; i++)
            {
                target[i] = array2d[sample][INPUT_NEURONS];
            } // i

        
         
            feedForward(0);

            backPropagate();
         
        } 
     return;
    }

    
    
    
    public static  void NeuralNetworkTest(String data)
    {
    	int rows,col;
    	rows=DmGui.testSize; //change according to data
    	col=16;
    	MAX_SAMPLES=rows;
        finalActivationVal=new double[DmGui.testSize];
    	
    	System.out.println(DmGui.testSize+"TEsting size");
    	
    	int trainInputs[] = new int[data.length()];
    	String dataArray[] = data.split("(?!^)");
    	//System.out.println(dataArray.length+"data array legnth");
    	for(int i=0;i<dataArray.length;i++){
    		trainInputs[i] = Integer.parseInt(dataArray[i]);
    	}
    	
    	System.out.println(trainInputs.length+"testinput length");
    	
    	 array2dtest = new int[rows][col];  //adjust the boundaries properly //MUST CHANGE ACCORDINGLY DATA SET


    	for(int i=0; i<rows;i++)
    	{
    		for(int j=0;j<col;j++)
    		{	  
    	       array2dtest[i][j] = trainInputs[(i*col) + j]; 
    		}
    	}	
        
        //write test encoded
            WriteOutput wo = new WriteOutput();
            wo.writeEncode(array2dtest,2);
        int sample = 0;

    

        // Train the network.
        for(sample= 0; sample < MAX_SAMPLES; sample++)
        {
           

            for(int i = 0; i < INPUT_NEURONS; i++)
            {
            	//System.out.println(sample+","+i);
                inputs[i] = array2dtest[sample][i];
            } // i

            for(int i = 0; i < OUTPUT_NEURONS; i++)
            {
        // System.out.println(array2dtest[sample][INPUT_NEURONS]);
                target[i] = array2dtest[sample][INPUT_NEURONS];
               
                
            } // i
            feedForward(1);
        } 
    return;
    }  

   
    private static void feedForward(int n)
    {
        double sum = 0.0;

        // Calculate input to hidden layer.
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            sum = 0.0;
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                sum += inputs[inp] * wih[inp][hid];
            } // inp

            sum += wih[INPUT_NEURONS][hid]; // Add in bias.
            hidden[hid] = sigmoid(sum);
        } // hid

        // Calculate the hidden to output layer.
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            sum = 0.0;
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                sum += hidden[hid] * who[hid][out];
            } // hid

            sum += who[HIDDEN_NEURONS][out]; // Add in bias.
            actual[out] = sigmoid(sum);
         
            if(n==1)
            {
                //System.out.println(number+"hdvfbgiasdbgsid");
            	finalActivationVal[number]=actual[out];
            	number++;
            }
           // System.out.println(actual[out]);
        } // out
        return;
    }

    private static void backPropagate()
    {
        // Calculate the output layer error (step 3 for output cell).
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
        }

        // Calculate the hidden layer error (step 3 for hidden cell).
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            errh[hid] = 0.0;
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                errh[hid] += erro[out] * who[hid][out];
            }
            errh[hid] *= sigmoidDerivative(hidden[hid]);
        }

        // Update the weights for the output layer (step 4).
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
            } // hid
            who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]); // Update the bias.
        } // out

        // Update the weights for the hidden layer (step 4).
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
            } // inp
            wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]); // Update the bias.
        } // hid
        return;
    }

    private static void assignRandomWeights()
    {
        for(int inp = 0; inp <= INPUT_NEURONS; inp++) // Do not subtract 1 here.
        {
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                // Assign a random weight value between -0.5 and 0.5
                wih[inp][hid] = new Random().nextDouble() - 0.5;
            } // hid
        } // inp

        for(int hid = 0; hid <= HIDDEN_NEURONS; hid++) // Do not subtract 1 here.
        {
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                // Assign a random weight value between -0.5 and 0.5
                who[hid][out] = new Random().nextDouble() - 0.5;
            } // out
        } // hid
        return;
    }

    private static double sigmoid(final double val)
    {
        return (1.0 / (1.0 + Math.exp(-val)));
    }

    private static double sigmoidDerivative(final double val)
    {
        return (val * (1.0 - val));
    }
    
 
   
}