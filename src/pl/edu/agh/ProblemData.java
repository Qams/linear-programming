package pl.edu.agh;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ProblemData {

    private int r;
    private int c;
    private double[][] array;
    private double[] arrayValue;
    private String[] arraySign;
    private double[] dest;
    private String destMoveTo;
    private String[] sign;
    private JSONObject obj;
    private String path;

    public ProblemData(String path)
    {
        try {
            obj = new JSONObject(new JSONReader().read(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        r = obj.getJSONArray("cond").length();
        c = obj.getInt("count");
        array = new double[r][c];
        arrayValue = new double[r];
        arraySign = new String[r];
        dest = new double[c];
        destMoveTo = obj.getJSONObject("dest").getString("val");
        sign = new String[c];
        this.path = path;
        initialize();
    }

    public ProblemData(ProblemData data)
    {
        this.r = data.r + data.getCols();
        this.c = data.c;
        System.out.println("R: " + r);
        array = new double[r][c];
        copyArrayAdj(data.getArray(), this.array, data.getSign(), data.r, data.c);
        arrayValue = new double[r];
        copyArrayValueAdj(data.getArrayValue(), arrayValue, data.getRows());
        sign = data.getSign();
        arraySign = new String[r];
        copyArraySignAdj(data.getArraySign());
        //arraySign = data.getArraySign();
        dest = data.getDest();
        destMoveTo = data.getDestMoveTo();

    }
    public String getDestMoveTo()
    {
        return destMoveTo;
    }

    public String[] getArraySign() {
        return arraySign;
    }

    public double[] getDest() {
        return dest;
    }

    public int getRows() {
        return r;
    }

    public String[] getSign() {
        return sign;
    }

    public int getCols() {
        return c;
    }

    public String getPath()
    {
        return this.path;
    }

    private void initialize()
    {
        JSONArray arr = obj.getJSONArray("cond");
        for(int i=0;i<r;i++)
        {
            for(int j=0;j<c;j++)
            {
                array[i][j] = arr.getJSONObject(i).getDouble("x" + (j+1));
            }
            arraySign[i] = arr.getJSONObject(i).getString("s");
            arrayValue[i] = arr.getJSONObject(i).getDouble("v");
        }
        initializeDestAndSign();
    }

    public double[][] getArray() {
        return array;
    }

    public double[] getArrayValue() {
        return arrayValue;
    }

    private void initializeDestAndSign()
    {
        for(int i=0;i<c;i++)
        {
            dest[i] = obj.getJSONObject("dest").getDouble("x" + (i+1));
            sign[i] = obj.getJSONObject("sign").getString("x" + (i+1));
        }
        destMoveTo = obj.getJSONObject("dest").getString("val");
    }

    public void print()
    {
        for(int i=0;i<r;i++)
        {
            for(int j=0;j<c;j++)
                System.out.print(array[i][j] + " ");
            System.out.println();
        }
        System.out.println("SIGN");
        for(int i=0;i<arraySign.length;i++) {
            System.out.println("SIGN: " + arraySign[i]);
            System.out.println("VALUE: " + arrayValue[i]);
        }
        System.out.println("DEST");
        for(int i=0;i<c;i++)
        {
            System.out.println(sign[i] + " ");
        }
        System.out.println(destMoveTo);

    }

    private void copyArrayAdj(double[][] from, double[][] to, String[] sign, int rows, int cols)
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                to[i][j] = from[i][j];
            }
        }
        for(int i=0;i<sign.length;i++)
        {
            for(int j=0;j<cols;j++)
            {
                if(i==j)
                    to[rows+i][j] = 1;
                else
                    to[rows+i][j] = 0;
            }
        }

    }

    private void copyArraySignAdj(String[] arraySign)
    {
        for(int i=0;i<r;i++)
        {
            if(i<arraySign.length)
                this.arraySign[i] = arraySign[i];
            else
                this.arraySign[i] = sign[i-arraySign.length];
        }
    }

    private void copyArrayValueAdj(double[] from, double[] to, int rows)
    {
        for(int i=0;i<rows;i++)
        {
            to[i] = from[i];
        }
        for(int i = rows;i<r;i++)
        {
            to[i] = 0;
        }
    }

    public static void main(String[] args) {
        ProblemData problem = new ProblemData("pp.json");
        problem.print();

    }
}
