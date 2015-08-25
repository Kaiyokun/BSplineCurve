package com.ict.algo;

import java.util.ArrayList;

public class BSplineCurve {

    public static void main( String[] args ) {

        BSplineCurve thisInstance = new BSplineCurve( DataPointIO.load( args[0] ) );

        DataPointIO.save( thisInstance.Interpolate( Integer.parseInt( args[1] ) ), args[2] );
    }

    public BSplineCurve( ArrayList<DataPoint> modelPoint, int pth ) {

        DataPoint[] V = new DataPoint[ modelPoint.size() ];
        modelPoint.toArray( V );

        InterpNode interpNode = new InterpNode( V, pth );

        this.deBoorAlgo = new CoxDeBoorAlgo( interpNode, pth );
        this.ctrlPoint  = new CtrlPoint( this.deBoorAlgo, interpNode, V, pth );
    }

    public BSplineCurve( ArrayList<DataPoint> modelPoint ) {

        this( modelPoint, 3 );
    }

    public ArrayList<DataPoint> Interpolate( int split ) {

        ArrayList<DataPoint> curve = new ArrayList<DataPoint>();

        double stepSize = 1.0 / split;
        double u        = 0.0;

        for (int i=0; i<split; ++i) {

            curve.add( this.calcCurve( u ) );
            u += stepSize;
        }

        return curve;
    }

    private DataPoint calcCurve( double u ) {

        IndexSet<Double> deBoorPoint = this.deBoorAlgo.calc( u );
        DataPoint        point       = new DataPoint();

        for (int i=deBoorPoint.begin(); i<=deBoorPoint.end(); ++i) {

            point.add( DataPoint.mul( deBoorPoint.at(i), this.ctrlPoint.at(i) ) );
        }

        return point;
    }

    private CtrlPoint     ctrlPoint;  // ¿ØÖÆµã
    private CoxDeBoorAlgo deBoorAlgo; // Cox-deBoorËã·¨
}
