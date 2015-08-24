/**
 * 用于描述Cox-deBoor算法的类
 */
public class CoxDeBoorAlgo {

    public CoxDeBoorAlgo( InterpNode interpNode, int pth ) {

        this.pth        = pth;
        this.interpNode = interpNode;
        this.calcInterpNodeDistance();
    }

    public CoxDeBoorAlgo( InterpNode interpNode ) {

        this( interpNode, 3 );
    }

    public IndexSet<Double> calc( double u ) {

        int p = this.pth;

        // u in [u[k], u[k+1])
        int k = this.interpNode.leftBound( u );

        // a[i]
        IndexSet<Double> uDistance = this.calcDistance( k, u );

        //                                 N[k-p][p]
        //                   N[k-p+1][p-1] N[k-p+1][p]
        //                  ......................
        // N[k][0]       N[k][1]       ... N[k][p]
        IndexSet<Double> deBoorPoint
            = new IndexSet<>( new Double[ p + 1 ], k - p, k );

        // N[k][0] = 1
        deBoorPoint.set( k, 1.0 );

        for (int j=1; j<=p; ++j) {

            for (int i=k-j; i<=k; ++i) {

                double forward  = 0.0;
                double backward = 0.0;

                if (i != (k-j) ) {

                    backward = ( uDistance.at(i)
                                 / this.interpNodeDistance[i][j] )
                             * deBoorPoint.at(i);
                }

                if (i != k ) {

                    forward = ( - uDistance.at(i+1+j)
                                / this.interpNodeDistance[i+1][j] )
                            * deBoorPoint.at(i+1);
                }

                deBoorPoint.set( i, backward + forward );
            }
        }

        return deBoorPoint;
    }

    private void calcInterpNodeDistance() {

        int m = interpNode.length() - 1;
        int p = this.pth;

        // [m-p-1][p]
        this.interpNodeDistance = new double[ m - p ][];

        for (int i=1; i<m-p; ++i) {

            this.interpNodeDistance[i] = new double[ p + 1 ];
        }

        //                             b[1][p]
        //                   b[2][p-1] b[2][p]
        //             .......................
        // b[p][1]     b[p][2]     ... b[p][p]
        // ...................................
        // b[m-p-1][1] b[m-p-1][2] ... b[m-p-1][p]
        for (int i=1; i<m-p; ++i) {

            int j = p - i + 1;

            for (j=(j<1)?1:j; j<=p; ++j) {

                this.interpNodeDistance[i][j]
                    = this.interpNode.distance( i + j, i );
            }
        }
    }

    private IndexSet<Double> calcDistance( int k, double u ) {

        int p = this.pth;

        IndexSet<Double> uDistance
            // a[k-p+1], ..., a[k], a[k+1], ..., a[k+p]
            = new IndexSet<>( new Double[ 2 * p ], k - p + 1, k + p );

        for (int i=uDistance.begin(); i<=uDistance.end(); ++i) {

            uDistance.set( i, this.interpNode.distance( u, i ) );
        }

        return uDistance;
    }

    private int        pth;
    private InterpNode interpNode;
    private double[][] interpNodeDistance;
}
