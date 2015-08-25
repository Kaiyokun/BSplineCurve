package com.ict.algo;

public class CtrlPoint {

    public CtrlPoint(  CoxDeBoorAlgo deBoorAlgo
                     , InterpNode    interpNode
                     , DataPoint[]   modelPoint
                     , int           p ) {

        int n = (modelPoint.length - 1) + (p - 1);

        this.ctrlPoint = new DataPoint[ n + 1 ];

        double[]    a = new double[ n + 1 ];
        double[]    b = new double[ n + 1 ];
        double[]    c = new double[ n ];
        DataPoint[] d = new DataPoint[ n + 1 ];

        // 1       0                                      P[0]    V[0]
        // N'[0]   N'[1]   N'[2]   N'[3]                  P[1]    V[0]
        //         N[1]    N[2]    N[3]                   P[2]    V[1]
        //            ..................
        //              N[n-3]   N[n-2]   N[n-1]          P[n-2]  V[n-3]
        //              N'[n-3]  N'[n-2]  N'[n-1]  N'[n]  P[n-1]  V[n-2]
        //                                0        1      P[n]    V[n-2]
        for (int i=p+1; i<=n; ++i) {

            IndexSet<Double> deBoorPoint = deBoorAlgo.calc( interpNode.at(i) );
            int k = deBoorPoint.begin();

            a[i-p+1] = deBoorPoint.at(k);
            b[i-p+1] = deBoorPoint.at(k+1);
            c[i-p+1] = deBoorPoint.at(k+2);
            d[i-p+1] = modelPoint[i-p];
        }

        b[0] = 1.0; c[0] = 0.0; d[0] = modelPoint[0];
        a[n] = 0.0; b[n] = 1.0; d[n] = modelPoint[n-(p-1)];

        double eps = 1e-12;

        IndexSet<Double> deBoorPoint = deBoorAlgo.calc( eps );
        double factor = - deBoorPoint.at(3) / c[2];
        int k = deBoorPoint.begin();

        a[1] = deBoorPoint.at(k);
        b[1] = deBoorPoint.at(k+1) + factor * a[2];
        c[1] = deBoorPoint.at(k+2) + factor * b[2];
        d[1] = DataPoint.add( modelPoint[0], DataPoint.mul( factor, d[2] ) );

        deBoorPoint = deBoorAlgo.calc( 1 - eps );
        factor = - deBoorPoint.at(n-p) / a[n-2];
        k = deBoorPoint.begin();

        a[n-1] = deBoorPoint.at(k+1) + factor * b[n-2];
        b[n-1] = deBoorPoint.at(k+2) + factor * c[n-2];
        c[n-1] = deBoorPoint.at(k+3);
        d[n-1] = DataPoint.add( modelPoint[n-(p-1)], DataPoint.mul( factor, d[n-2] ) );

        double[]    r = new double[ n ];
        DataPoint[] y = new DataPoint[ n + 1 ];

        r[0] = c[0] / b[0];
        y[0] = DataPoint.mul( 1.0 / b[0], d[0] );

        for (int i=1; i<n; ++i) {

            double denominator = b[i] - a[i] * r[i-1];

            r[i] = c[i] / denominator;
            y[i] = DataPoint.mul(  1.0 / denominator
                                 , DataPoint.sub(  d[i]
                                                 , DataPoint.mul( a[i], y[i-1] ) ) );
        }

        double denominator = b[n] - a[n] * r[n-1];
        y[n] = DataPoint.mul(  1.0 / denominator
                             , DataPoint.sub(  d[n]
                                             , DataPoint.mul( a[n], y[n-1] ) ) );

        this.ctrlPoint[n] = y[n];

        for (int i=n-1; i>=0; --i) {

            this.ctrlPoint[i] = DataPoint.sub( y[i], DataPoint.mul( r[i], this.ctrlPoint[i+1] ) );
        }
    }

    public CtrlPoint(  CoxDeBoorAlgo deBoorAlgo
                     , InterpNode    interpNode
                     , DataPoint[]   modelPoint ) {

        this( deBoorAlgo, interpNode, modelPoint, 3 );
    }

    public DataPoint at( int index ) {

        return this.ctrlPoint[index];
    }

    private DataPoint[] ctrlPoint; // ¿ØÖÆµã
}
