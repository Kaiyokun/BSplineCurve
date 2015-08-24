/**
 * 用于描述插值节点u = (u[0], u[1], ..., u[m])的类
 */
public class InterpNode {

    public InterpNode( DataPoint[] modelPoint, int pth ) {

        this.pth = pth;
        this.calcInterpNode( modelPoint );
    }

    public InterpNode( DataPoint[] modelPoint ) {

        this( modelPoint, 3 );
    }

    public double at( int index ) {

        return this.interpNode[index];
    }

    public int length() {

        return this.interpNode.length;
    }

    public int leftBound( double u ) {

        // u[0] = u[1] = ... = u[p] = 0
        int leftBound = this.pth;

        // u[m-p] = u[m-p+1] = ... = u[m] = 1
        int rightBound = (this.length() - 1) - this.pth;

        while (leftBound != rightBound) {

            int checkPoint = (leftBound + rightBound) / 2;

            if (u < this.at(checkPoint) ) {

                rightBound = checkPoint;

            } else if (u >= this.at(checkPoint+1) ) {

                leftBound = checkPoint + 1;

            } else {

                return checkPoint;
            }
        }

        return -1;
    }

    public double distance( int i, int j ) {

        return this.at(i) - this.at(j);
    }

    public double distance( double u, int i ) {

        return u - this.at(i);
    }

    private void calcInterpNode( DataPoint[] modelPoint ) {

        int p = this.pth;
        int m = (modelPoint.length - 1) + 2*p;

        this.interpNode = new double[ m + 1 ];

        for (int i=0; i<=p; ++i) {

            // u[0] = u[1] = ... = u[p] = 0
            this.interpNode[i] = 0.0;

            // u[m-p] = u[m-p+1] = ... = u[m] = 1
            this.interpNode[m-p+i] = 1.0;
        }

        this.defaultMethod( modelPoint );
    }

    /**
     * 累积弦长法
     */
    private void cumulateChordLength( DataPoint[] modelPoint ) {

        int n = modelPoint.length - 1;
        int p = this.pth;

        double[] chordLength    = new double[ n + 1 ];
                 chordLength[0] = 0.0;

        for (int i=0; i<n; ++i) {

            chordLength[i+1] = chordLength[i]
                             + modelPoint[i+1].distance( modelPoint[i] );
        }

        for (int i=1; i<n; ++i) {

            this.interpNode[p+i] = chordLength[i] / chordLength[n];
        }
    }

    /**
     * 向心参数法
     */
    private void cumulateSqrtChordLength( DataPoint[] modelPoint ) {

        int n = modelPoint.length - 1;
        int p = this.pth;

        double[] chordLength    = new double[ n + 1 ];
                 chordLength[0] = 0.0;

        for (int i=0; i<n; ++i) {

            chordLength[i+1] = chordLength[i]
                             + Math.sqrt( modelPoint[i+1].distance( modelPoint[i] ) );
        }

        for (int i=1; i<n; ++i) {

            this.interpNode[p+i] = chordLength[i] / chordLength[n];
        }
    }

    /**
     * 均匀参数法
     */
    private void defaultMethod( DataPoint[] modelPoint ) {

        int p = this.pth;
        int m = (modelPoint.length - 1) + 2*p;

        double h = 1.0 / modelPoint.length;

        for (int i=p+1; i<m-p; ++i) {

            // u[p+1], u[p+2], ..., u[m-p-1]
            this.interpNode[i] = this.interpNode[i-1] + h;
        }
    }

    private int      pth;
    private double[] interpNode;
}
