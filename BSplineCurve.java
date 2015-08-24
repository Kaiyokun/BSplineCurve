import java.util.ArrayList;

public class BSplineCurve {

    private DataPoint[] ctrlPoint; // 控制点, n+1个, P[0], P[1], ..., P[n]
    private int         pth;       // 样条次数, p次
    private InterpNode    node;      // 节点, m+1个, m = n + 1 + p
    private double[][]  dNode;     // 节点间长度 b[i][p] = u[i+p] - u[i]

    private void calcCtrlPoint( ArrayList<DataPoint> modelPoint ) {

        this.ctrlPoint = new DataPoint[ modelPoint.size() + 2 ];

        // b[0]   c[0]
        // a[1]   b[1]   c[1]
        //        a[2]   b[2]   c[2]
        //            ..............
        //               a[n-1] b[n-1] c[n-1]
        //                      a[n]   b[n]
        int n = this.ctrlPoint.length - 1;

        double[]    a = new double[ n + 1 ];
        double[]    b = new double[ n + 1 ];
        double[]    c = new double[ n ];
        DataPoint[] d = new DataPoint[ n + 1 ];

        d[0] = modelPoint.get(0);

        for (int i=1; i<=n-1; ++i) {

            d[i] = modelPoint.get(i-1);
        }

        d[n] = modelPoint.get(n-2);

        //d[1].add( new DataPoint( 1e12, 1e12 ) );

        b[0] = 1.0;
        c[0] = 0.0;

        for (int i=2; i<n-1; ++i) {

            NonstandardDoubleArray deBoorPoint = calcDeBoorPoint( this.node.at(2+i) );

            a[i] = deBoorPoint.at(i-1);
            b[i] = deBoorPoint.at(i);
            c[i] = deBoorPoint.at(i+1);
        }

        NonstandardDoubleArray deBoorPoint = calcDeBoorPoint( 1e-12 );

        a[1] = deBoorPoint.at(0);
        b[1] = deBoorPoint.at(1);
        c[1] = deBoorPoint.at(2);

        double factor = - deBoorPoint.at(3) / c[2];
        b[1] += factor * a[2];
        c[1] += factor * b[2];

        deBoorPoint = calcDeBoorPoint( 1 - 1e-12 );

        a[n-1] = deBoorPoint.at(n-2);
        b[n-1] = deBoorPoint.at(n-1);
        c[n-1] = deBoorPoint.at(n);

        factor = - deBoorPoint.at(n-3) / a[n-2];
        a[n-1] += factor * b[n-2];
        b[n-1] += factor * c[n-2];

        a[n] = 0.0;
        b[n] = 1.0;

        double[]    r = new double[ n ];
        DataPoint[] y = new DataPoint[ n + 1 ];

        r[0] = c[0] / b[0];
        y[0] = DataPoint.mul( 1.0/b[0], d[0] );

        for (int i=1; i<=n-1; ++i) {

            double denominator = b[i] - a[i] * r[i-1];

            r[i] = c[i] / denominator;
            y[i] = DataPoint.mul(  1.0/denominator
                                 , DataPoint.sub(  d[i]
                                                 , DataPoint.mul( a[i], y[i-1] ) ) );
        }

        double denominator = b[n] - a[n] * r[n-1];
        y[n] = DataPoint.mul(  1.0/denominator
                             , DataPoint.sub(  d[n]
                                             , DataPoint.mul( a[n], y[n-1] ) ) );

        this.ctrlPoint[n] = y[n];

        for (int i=n-1; i>=0; --i) {

            this.ctrlPoint[i] = DataPoint.sub( y[i], DataPoint.mul( r[i], this.ctrlPoint[i+1] ) );
        }
    }

    private void calcNode( DataPoint[] modelPoint ) {

        // m + 1 = n + 1 + p + 1
        this.node = new InterpNode( modelPoint );
    }

    private void calcIntervalLengthNode() {

        // [m-p-1][p]
        this.dNode = new double[ (this.node.length() - 1) - this.pth - 1 ][];

        for (int i=0; i<this.dNode.length; ++i) {

            this.dNode[i] = new double[ this.pth ];
        }

        //                             b[1][p]
        //                   b[2][p-1] b[2][p]
        //             .......................
        // b[p][1]     b[p][2]     ... b[p][p]
        // ...................................
        // b[m-p-1][1] b[m-p-1][2] ... b[m-p-1][p]
        for (int i=0; i<this.dNode.length; ++i) {

            int p = this.pth - i - 1;

            for (p=(p<0)?0:p; p<this.pth; ++p) {

                this.dNode[i][p] = node.distance( (i+1)+(p+1), i+1 );
            }
        }
    }

    private NonstandardDoubleArray calcIntervalLengthU( int k, double u ) {

        NonstandardDoubleArray du
            // a[k-p+1], ..., a[k], a[k+1], ..., a[k+p]
            = new NonstandardDoubleArray( k - this.pth + 1, k + this.pth );

        for (int i=du.begin(); i<=du.end(); ++i) {

            du.set( i, this.node.distance( u, i ) );
        }

        return du;
    }

    private NonstandardDoubleArray calcDeBoorPoint( double u ) {

        // u in [u[k], u[k+1])
        int k = this.node.leftBound( u );

        // a[i]
        NonstandardDoubleArray du
            = this.calcIntervalLengthU( k, u );

        //                     N[k-p][p]
        //       N[k-p+1][p-1] N[k-p+1][p]
        //         ...................
        // N[k][0] N[k][1] ... N[k][p]
        NonstandardDoubleArray deBoorPoint
            = new NonstandardDoubleArray( k - this.pth, k );

        // N[k][0] = 1
        deBoorPoint.set( k, 1.0 );

        for (int p=1; p<=this.pth; ++p) {

            for (int i=k-p; i<=k; ++i) {

                double forward  = 0.0;
                double backward = 0.0;

                if (i != (k-p) ) {

                    backward = ( du.at(i)
                                 / this.dNode[i-1][p-1] )
                             * deBoorPoint.at(i);
                }

                if (i != k ) {

                    forward = ( - du.at(i+1+p)
                                / this.dNode[i][p-1] )
                            * deBoorPoint.at(i+1);
                }

                deBoorPoint.set( i, backward + forward );
            }
        }

        return deBoorPoint;
    }

    private DataPoint calcCurve( double u ) {

        NonstandardDoubleArray deBoorPoint = this.calcDeBoorPoint( u );
        DataPoint              point       = new DataPoint();

        for (int i=deBoorPoint.begin(); i<=deBoorPoint.end(); ++i) {

            point.add( DataPoint.mul( deBoorPoint.at(i), this.ctrlPoint[i] ) );
        }

        return point;
    }

    public BSplineCurve( ArrayList<DataPoint> modelPoint, int pth ) {

        this.reset( modelPoint, pth );
    }

    public BSplineCurve( ArrayList<DataPoint> modelPoint ) {

        this( modelPoint, 3 );
    }

    public void reset( ArrayList<DataPoint> modelPoint, int pth ) {

        DataPoint[] V = new DataPoint[modelPoint.size()];
        modelPoint.toArray( V );

        this.pth = pth;

        this.calcNode( V );

        this.calcIntervalLengthNode();

        this.calcCtrlPoint( modelPoint );
    }

    public ArrayList<DataPoint> Interpolate( int split ) {

        ArrayList<DataPoint> curve    = new ArrayList<DataPoint>();
        double               stepSize = 1.0 / split;
        double               u        = 0.0;

        for (int i=0; i<split; ++i) {

            curve.add( this.calcCurve( u ) );
            u += stepSize;
        }

        return curve;
    }

    public static void main( String[] args ) {

        BSplineCurve thisInstance = new BSplineCurve( DataPointIO.load( args[0] ) );

        DataPointIO.save( thisInstance.Interpolate( Integer.parseInt( args[1] ) ), args[2] );
    }
}
