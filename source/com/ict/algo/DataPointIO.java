package com.ict.algo;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class DataPointIO {

    public static ArrayList<DataPoint> load( String fileIn ) {

        ArrayList<DataPoint> modelPoint = new ArrayList<DataPoint>();

        BufferedReader buffReader = null;
        String data = null;

        try {

            try {

                buffReader = new BufferedReader( new FileReader( new File( fileIn ) ) );

                while ( (data = buffReader.readLine() ) != null ) {

                    modelPoint.add( new DataPoint( data ) );
                }

            } finally {

                buffReader.close();
            }

        } catch (IOException e) {

             e.printStackTrace();
        }

        return modelPoint;
    }

    public static void save( ArrayList<DataPoint> curve, String fileOut ) {

        BufferedWriter buffWriter = null;

        try {

            try {

                buffWriter = new BufferedWriter( new FileWriter( new File( fileOut ) ) );

                for (DataPoint point : curve) {

                    String data = point.toString();

                    buffWriter.write( data, 0, data.length() );
                    buffWriter.newLine();
                }

            } finally {

                buffWriter.close();
            }

        } catch (IOException e) {

             e.printStackTrace();
        }
    }
}
