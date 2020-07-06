import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.util.Arrays;
import java.util.Date;
import java.io.IOException;


public class Solution {

    public static long[] resultTemp = new long[50];
    public static int i = 0;

    public static long[] getNumbers(long N) {
        date = new Date();
        if (N <= 0) {
            long[] result = new long[0];
            return result;
        }

        int maxThreadsCount = 6;
        int needToProcess = String.valueOf( N ).length();
        ArrayList<getNumbersThread> getN = new ArrayList<>(  );

        for (int i=0;(i<maxThreadsCount-1)&&(i<=needToProcess);i++){
            getN.add( new getNumbersThread(needToProcess-1, needToProcess) );
            needToProcess--;
        }

        if (needToProcess>0){
            getN.add( new getNumbersThread(0, needToProcess));
        }

        ArrayList<Thread> threads = new ArrayList<>(  );
        for (int i=0;i<getN.size();i++){
            threads.add(new Thread (getN.get( i )));
            threads.get( i ).start();
        }

        try {
            for (int i=0;i<threads.size();i++){
                threads.get( i ).join();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }


        Arrays.sort( resultTemp, 0, i );
        int newCount = 0;
        for (int j = 0; j < i; j++) {
            if ((resultTemp[j] > 0) && (resultTemp[j] < N)) {
                newCount++;
            }
        }

        long[] result = new long[newCount];

        for (int j = 0; j < newCount; j++) {
            result[j] = resultTemp[j];
        }
        for (int m = 0; m < result.length; m++) {
            System.out.println( result[m] );
        }
        System.out.println( "Прошло миллисекунд: " + ((new Date().getTime() - date.getTime()) + (end.getTime() - begin.getTime())) );

        for(int i = 0; i < resultTemp.length; i++)
            resultTemp[i] = 0;
        i = 0;
        date = null;
        return result;
    }

    // нить, которая выполняет проверку
    static class getNumbersThread implements Runnable {

        public ArrayList<Integer> string2= new ArrayList<>(  );
        private int r1;
        private int r2;


        public getNumbersThread(int r1, int r2) {

            this.r1 = r1;
            this.r2 = r2;
            for (int i=0;i<=r1;i++)
                string2.add( 1 );

            for (int i=0;i<3;i++){
                string2.add( 0 );
            }
        }

        @Override
        public void run() {

            long res = 0;

            while (string2.size() <= r2+3) {

                res = isArmstrong( string2 );
                if (res > 0) {
                    resultTemp[i] = res;
                    i++;
                }
                string2 = incChar( string2, 0 );
            }
        }
    }

    //создание массива степеней

    public static long[][] matrixA;
    public static Date begin;
    public static Date end;
    public static Date date;

    static {
        begin = new Date();
        matrixA = new long[10][20];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                matrixA[i][j] = (long) (Math.pow( (double)i, (double)j ));
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 2; j < 20; j++) {
                matrixA[i][j] = (matrixA[i][j - 1]) * i;
            }
        }
        end = new Date();
    }

    public static void main(String[] args) throws IOException {
        long[] list = getNumbers(123);
        list = getNumbers(12333);
    }


    //Проверка, является ли полученное число числом Армстронга
    private static long isArmstrong(ArrayList<Integer> string2) {

        long isArmstrong = -1;
        int size = string2.size();

        if (size < 23) {
            isArmstrong = degreeArrays( size, string2, 3 );
        }

        if ((isArmstrong < 0) && (size < 22)) {
            isArmstrong = degreeArrays( size, string2, 2 );
        }

        if ((isArmstrong < 0) && (size < 21)) {
            isArmstrong = degreeArrays( size, string2, 1 );
        }

        if ((isArmstrong < 0) && (size < 20)) {
            isArmstrong = degreeArrays( size, string2, 0 );
        }

        return isArmstrong;
    }


    //Сортировка массива чисел, составляющих число Армстронга
    private static ArrayList<Integer> sort(ArrayList<Integer> degreeArray) {
        int dummy = 0;
        for (int i = degreeArray.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if ((degreeArray.get( j )) < (degreeArray.get( j + 1 ))) {
                    dummy = degreeArray.get( j + 1 );
                    degreeArray.set( j + 1, degreeArray.get( j ) );
                    degreeArray.set( j, dummy );
                }
            }
        }
        return degreeArray;
    }

    // инкремент проверяемого числа с условием 1<=2<=3... и тремя ведущими нулями
    public static ArrayList<Integer> incChar(ArrayList<Integer> string, int position) {
        int newSymbol = -1;
        switch (string.get( position )) {
            case 0:
                newSymbol = 1;
                break;
            case 1:
                newSymbol = 2;
                break;
            case 2:
                newSymbol = 3;
                break;
            case 3:
                newSymbol = 4;
                break;
            case 4:
                newSymbol = 5;
                break;
            case 5:
                newSymbol = 6;
                break;
            case 6:
                newSymbol = 7;
                break;
            case 7:
                newSymbol = 8;
                break;
            case 8:
                newSymbol = 9;
                break;
            case 9: {
                if ((position == string.size() - 4)) {
                    string.add( 0 );
                }
                incChar( string, position + 1 );
            }
            break;
        }
        if (newSymbol > -1) {
            for (int i = position; i >= 0; i--) {
                string.set( i, newSymbol );
            }
        }
        return string;
    }

    // непосредственно проверка чисел Армстронга для вариантов без ведущих нулей и с ними
    public static long degreeArrays(int stepen, ArrayList<Integer> string2, int n) {
        long degree = 0;
        long result = -1;

        for (int i = 0; i < stepen - n; i++)
            degree += matrixA[string2.get( i )][stepen - n];

        ArrayList<Integer> degreeArray = new ArrayList<>();
        long temp = degree;
        while (temp > 0) {
            int temp2 = (int) (temp % 10);
            degreeArray.add( temp2 );
            temp = temp / 10;
        }

        for (int i = 0; i < n; i++) {
            degreeArray.add( 0 );
        }

        if (degreeArray.size() > 1) {
            degreeArray = sort( degreeArray );
        }

        if (degreeArray.equals( string2 )) {
            result = degree;
        }
        return result;
    }
}
