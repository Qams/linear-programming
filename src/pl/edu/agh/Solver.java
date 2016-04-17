package pl.edu.agh;

import java.util.ArrayList;

public class Solver {

    private ProblemData data;
    private ProblemData dualData;
    private double[] solution;
    private double destValue;
    private int minmax;

    public Solver(ProblemData data)
    {
        this.data = data;
        if(data.getDestMoveTo().equals("max")) {
            destValue = Double.MIN_VALUE;
            minmax = 1;
        }
        else {
            destValue = Double.MAX_VALUE;
            minmax = 0;
        }
        solution = new double[data.getCols()];
    }

    private double destCalculate(double[] result, ProblemData dataSolve)
    {
        double suma = 0;
        for(int i=0;i<result.length;i++)
        {
            suma += result[i]*dataSolve.getDest()[i];
        }
        return suma;
    }

    public double[] getSolution() {
        return solution;
    }

    public double getDestValue() {
        return destValue;
    }

    public boolean solve()
    {
        if(data.getCols()>2 && data.getRows()>2)
        {
            System.out.println("Cannot solve this problem");
            return false;
        }
        else if(data.getCols()>2 && data.getRows()<=2)
        {
            ProblemParser parser = new ProblemParser(data.getPath(), "dual.json");
            parser.generateJSONDualProblem();
            dualData = new ProblemData("dual.json");
            if(dualData.getDestMoveTo().equals("max")) {
                destValue = Double.MIN_VALUE;
                minmax = 1;
            }
            else {
                destValue = Double.MAX_VALUE;
                minmax = 0;
            }
            System.out.println("Solving dual problem");
            primaryIssue(dualData);

            double[] dualSolution = getDualSolution(dualData);
            System.out.println(dualSolution.length);
            ArrayList<Integer> toDelete = removeVariables(dualData, dualSolution);
            System.out.println(toDelete);
            double[][] arr = generateArray(data, toDelete);
            //printArrayTest(arr, 2, 2);
            double nextsol[] = dualToPrimarySolution(data.getArrayValue() ,arr, data.getRows(), data.getCols() - toDelete.size());
            modifySolution(nextsol, toDelete);
            return true;
        }
        else
        {
            primaryIssue(data);
            System.out.println("SOLUTION: [" + solution[0] + ", " + solution[1] + "]");
            return true;
        }
    }

    private void primaryIssue(ProblemData data)
    {

        ProblemData adjProblem = new ProblemData(data);
        double tmpArray[][] = adjProblem.getArray();
        double tmpValues[] = adjProblem.getArrayValue();
        printArrayTest(adjProblem.getArray(), adjProblem.getRows(), adjProblem.getCols());
        System.out.println("Solving problem");
        for(int i=1;i<adjProblem.getRows();i++)
        {
            for(int j=0;j<i;j++)
            {
                double valTab[] = {tmpValues[i], tmpValues[j]};
                Equation equation = new Equation(tmpArray[i], tmpArray[j], valTab, adjProblem.getCols());
                //equation.testPrint();
                double[] result = equation.solveEquation();
               // System.out.println("RESULT: [" + result[0] + " " + result[1] + "]");
                if(checkStatement(result, adjProblem)) {
                    System.out.println("X: " + result[0] + "Y: " + result[1]);
                    if (minmax == 0) {
                        if (destValue > destCalculate(result,adjProblem)) {
                            destValue = destCalculate(result, adjProblem);
                            resultToSolution(result, solution);
                        }

                    } else {
                        if (destValue < destCalculate(result,adjProblem)) {
                            destValue = destCalculate(result, adjProblem);
                            resultToSolution(result, solution);
                        }
                    }
                }
            }
        }
        //System.out.println("SOLVE: " + destValue);
    }

    private boolean checkStatement(double[] result, ProblemData dataSolve)
    {
        for(int i=0; i<dataSolve.getRows();i++)
        {
            double res = 0;
            for(int j=0; j<dataSolve.getCols();j++)
            {
                res += dataSolve.getArray()[i][j] * result[j];
            }
            switch (dataSolve.getArraySign()[i]) {
                case ">=":
                    if (res < dataSolve.getArrayValue()[i])
                        return false;
                    break;
                case "<=":
                    if (res > dataSolve.getArrayValue()[i])
                        return false;
                    break;
                case "=":
                    if (res != dataSolve.getArrayValue()[i])
                        return false;
                    break;
            }
        }
        for(int i=0;i<dataSolve.getCols();i++)
        {
            if(dataSolve.getSign()[i].equals(">="))
                if(result[i]<0)
                    return false;
            else if(dataSolve.getSign()[i].equals("<="))
                    if(result[i]>0)
                        return false;
            else if(dataSolve.getSign()[i].equals("="))
                        if(result[i]!=0)
                            return false;
        }
        return true;
    }

    private void printArrayTest(double[][] arr, int r, int c)
    {
        for(int i=0;i<r;i++)
        {
            for(int j=0; j<c;j++)
            {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }

    }

    private void resultToSolution(double[] res, double[] sol)
    {
        System.arraycopy(res, 0, sol, 0, res.length);
    }

    private double[] getDualSolution(ProblemData dual)
    {
        double[] dualSol = new double[dual.getCols()];
        for(int i=0; i < dual.getCols(); i++)
        {
            dualSol[i] = solution[i];
        }
        return dualSol;
    }

    private ArrayList<Integer> removeVariables(ProblemData problem, double[] solution)
    {
        ArrayList<Integer> toDelete = new ArrayList<>();
        for(int i=0; i < problem.getRows(); i++)
        {
            double suma = 0;
            for(int j=0; j < problem.getCols(); j++)
            {
                suma += problem.getArray()[i][j] * solution[j];
            }
            switch (problem.getArraySign()[i]) {
                case ">=":
                    if (suma > problem.getArrayValue()[i])
                        toDelete.add(i);
                    break;
                case "<=":
                    if (suma < problem.getArrayValue()[i])
                        toDelete.add(i);
                    break;
                case "=":
                    if (suma != problem.getArrayValue()[i])
                        toDelete.add(i);
                    break;
            }
        }

        return toDelete;
    }
    private double[][] generateArray(ProblemData problem, ArrayList<Integer> toDelete)
    {
        int r =problem.getRows();
        int c = problem.getCols() - toDelete.size();
        double[][] arr = new double[r][c];
        for(int i=0;i<c;i++)
        {
            int k = 0;
            for(int j=0;j<r;j++)
            {
                if(!toDelete.contains(i)) {
                    arr[i][j] = problem.getArray()[i][k];
                    k++;
                }
                else{
                    while(toDelete.contains(k))
                    {
                        k++;
                    }
                    arr[i][j] = problem.getArray()[i][k];
                }
            }
        }

        return arr;
    }

    private double[] dualToPrimarySolution(double[] val, double[][] arr, int r, int c)
    {
        double[] solution = null;
        if(r == 2 && c == 2)
        {
            Equation eq = new Equation(arr[0], arr[1], val, 2);
            solution = eq.solveEquation();

        }
        else if(r == 1 && c == 1)
        {
            solution = new double[1];
            solution[0] = arr[0][0]/val[0];
        }
        return solution;
    }

    private void modifySolution(double[] sol, ArrayList<Integer> zeros)
    {
        int k=0;
        for(int i=0; i<solution.length; i++)
        {
            if(!zeros.contains(i))
            {
                solution[i] = sol[k];
                k++;
            }
            else{
                solution[i] = 0;
            }
        }
    }


}
