package com.ict.algo;

import java.util.ArrayList;

public class BSplineCurve {

    public static void main( String[] args ) {

        ArrayList<DataPoint> modelPoint = DataPointIO.load( args[0] );

        BSplineCurve thisInstance = new BSplineCurve( modelPoint );
        /*
        DataPoint[] V = new DataPoint[ modelPoint.size() ];
        modelPoint.toArray( V );

        InterpNode    interpNode = new InterpNode( V, new AccumulatedChordAlgo() );
        CoxDeBoorAlgo deBoorAlgo = new CoxDeBoorAlgo( interpNode );
        CtrlPoint     ctrlPoint  = new CtrlPoint( deBoorAlgo, interpNode, V );

        BSplineCurve thisInstance = new BSplineCurve( ctrlPoint, interpNode );
        */
        int split = (args.length == 1) ? 10 : Integer.parseInt( args[1] );

        DataPointIO.save(  thisInstance.interpolate( modelPoint.size() * split ), args[0] + ".out" );
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

    public BSplineCurve( CtrlPoint ctrlPoint, InterpNode interpNode ) {

        this.deBoorAlgo = new CoxDeBoorAlgo(  interpNode
                                              // m + 1 = n + 1 + p + 1
                                            , interpNode.length() - ctrlPoint.length() - 1 );
        this.ctrlPoint  = ctrlPoint;
    }

    public ArrayList<DataPoint> interpolate( int split ) {

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
