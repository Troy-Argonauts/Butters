package org.troyargonauts.libs;

public class Newton_Raphson {
    public static double[] Derive(double [] variables){


        double[] derivedVariable = new double[variables.length - 1];

        for (int i = 0; i < derivedVariable.length; i++){

            derivedVariable[i] = variables[i] * (i - derivedVariable.length + 1);

        }




        return derivedVariable;

    }

    public static double rootEstimate(double[] variables){

        double rootEstimateLow = -1;
        double tempXVal = -50;

        while(rootEstimateLow < 0){
            rootEstimateLow = Math.pow(tempXVal,8)*variables[0]+ Math.pow(tempXVal,7)*variables[1]+ Math.pow(tempXVal,6)*variables[2] + Math.pow(tempXVal,5)*variables[3] + Math.pow(tempXVal,4)*variables[4]+Math.pow(tempXVal,4)*variables[5]+Math.pow(tempXVal,2)*variables[6] +tempXVal*variables[7]+ variables[8];
            tempXVal++;
        }

        tempXVal += 0.5;

        return tempXVal;
    }

    public static double Newton_Raphson_Calc(double [] variables, double[] derivedVariables, double tolerence, double estimate){



        double initalDerivitive;
        double prevEstimate = estimate;
        try{
            initalDerivitive = Math.pow(prevEstimate,7)*derivedVariables[0]+ Math.pow(prevEstimate,6)*derivedVariables[1] + Math.pow(prevEstimate,5)*derivedVariables[2] + Math.pow(prevEstimate,4)*derivedVariables[3]+Math.pow(prevEstimate,3)*variables[4]+Math.pow(prevEstimate,2)*derivedVariables[5] +prevEstimate*derivedVariables[6]+ derivedVariables[7];
        } catch (Exception e){
            initalDerivitive = Math.pow(prevEstimate,6)*derivedVariables[0]+ Math.pow(prevEstimate,5)*derivedVariables[1] + Math.pow(prevEstimate,4)*derivedVariables[2] + Math.pow(prevEstimate,3)*derivedVariables[3]+Math.pow(prevEstimate,2)*variables[4]+Math.pow(prevEstimate,1)*derivedVariables[5] + derivedVariables[6];

        }

        double currentEstimate = 0;
        double difference = currentEstimate - prevEstimate;
        if (initalDerivitive <0.5 && initalDerivitive >-0.5){
            return 0;
        }

        else {

            while (Math.abs(difference) > tolerence) {

                try {
                    currentEstimate = prevEstimate - ((Math.pow(prevEstimate, 8) * variables[0] + Math.pow(prevEstimate, 7) * variables[1] + Math.pow(prevEstimate, 6) * variables[2] + Math.pow(prevEstimate, 5) * variables[3] + Math.pow(prevEstimate, 4) * variables[4] + Math.pow(prevEstimate, 3) * variables[5] + Math.pow(prevEstimate, 2) * variables[6] + prevEstimate * variables[7] + variables[8]) / Math.pow(prevEstimate, 7) * derivedVariables[0] + Math.pow(prevEstimate, 6) * derivedVariables[1] + Math.pow(prevEstimate, 5) * derivedVariables[2] + Math.pow(prevEstimate, 4) * derivedVariables[3] + Math.pow(prevEstimate, 4) * variables[4] + Math.pow(prevEstimate, 2) * derivedVariables[5] + prevEstimate * derivedVariables[6] + derivedVariables[7]);
                } catch (Exception e){
                    currentEstimate = prevEstimate - ((Math.pow(prevEstimate, 7) * variables[0] + Math.pow(prevEstimate, 6) * variables[1] + Math.pow(prevEstimate, 5) * variables[2] + Math.pow(prevEstimate, 4) * variables[3] + Math.pow(prevEstimate, 3) * variables[4] + Math.pow(prevEstimate, 2) * variables[5] + Math.pow(prevEstimate, 1) * variables[6] + variables[7]/ Math.pow(prevEstimate, 6) * derivedVariables[0] + Math.pow(prevEstimate, 5) * derivedVariables[1] + Math.pow(prevEstimate, 4) * derivedVariables[2] + Math.pow(prevEstimate, 3) * derivedVariables[3] + Math.pow(prevEstimate, 2) * variables[4] + Math.pow(prevEstimate, 1) * derivedVariables[5] +derivedVariables[6]));

                }

                difference = currentEstimate - prevEstimate;
                prevEstimate = currentEstimate;
            }
            return currentEstimate;
        }



    }
}
