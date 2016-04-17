package pl.edu.agh;

public class Equation {
    double matrix[][];
    double values[];
    double matrixWx[][];
    double matrixWy[][];
    int cols;

    public Equation(double []eq1, double []eq2, double[] values, int cols)
    {
        matrix = new double[cols][cols];
        matrixWx = new double[cols][cols];
        matrixWy = new double[cols][cols];
        matrix[0] = eq1;
        matrix[1] = eq2;
        this.values = values;
        this.cols = cols;
        if(cols > 1) {
            for (int i = 0; i < cols; i++) {
                matrixWx[i][0] = values[i];
                matrixWx[i][1] = matrix[i][1];
                matrixWy[i][1] = values[i];
                matrixWy[i][0] = matrix[i][0];
            }
        }
    }

    private double getDeterminant(double[][] tab)
    {
        return (tab[0][0]*tab[1][1]) - (tab[1][0]*tab[0][1]);
    }

    public double[] solveEquation()
    {
        double w = getDeterminant(matrix);
        if(w != 0) {
            double wx = getDeterminant(matrixWx);
            double wy = getDeterminant(matrixWy);
            double[] ret = new double[2];
            ret[0] = wx / w;
            ret[1] = wy / w;
            return ret;
        }
        else
        {
            return new double[2];
        }
    }

    /*public void testPrint()
    {
        for(int i=0;i<cols;i++)
        {
            for(int j=0;j<cols;j++)
            {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("DET: " + getDeterminant(matrixWx));
        //System.out.println("VALUES: [" + values[0] + ", " + values[1] + "]");


    }*/
}
