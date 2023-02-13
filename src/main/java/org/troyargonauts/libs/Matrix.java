package org.troyargonauts.libs;

public class Matrix {


    public static double[][] inverse(double [][] matrix){
        double [][] inverse = new double[matrix.length][matrix[0].length];
        for (int i =0; i < matrix.length; i++){
            for (int j = 0; i < matrix[0].length; j ++){
                inverse[i][j] = Math.pow(matrix[i][j], -1);
            }
        }

        return inverse;
    }
}
