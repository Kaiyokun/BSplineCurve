public class DataPoint {

    private double x;
    private double t;

    public DataPoint() {

        this( 0.0, 0.0 );
    }

    public DataPoint( double x, double t ) {

        this.x = x;
        this.t = t;
    }

    public DataPoint( DataPoint point ) {

        this( point.x, point.t );
    }

    public static DataPoint add( DataPoint left, DataPoint right ) {

        DataPoint result = new DataPoint();

        result.x = left.x + right.x;
        result.t = left.t + right.t;

        return result;
    }

    public static DataPoint sub( DataPoint left, DataPoint right ) {

        DataPoint result = new DataPoint();

        result.x = left.x - right.x;
        result.t = left.t - right.t;

        return result;
    }

    public static DataPoint mul( double factor, DataPoint point ) {

        DataPoint result = new DataPoint();

        result.x = factor * point.x;
        result.t = factor * point.t;

        return result;
    }

    public DataPoint add( DataPoint point ) {

        this.x += point.x;
        this.t += point.t;

        return this;
    }

    public DataPoint sub( DataPoint point ) {

        this.x -= point.x;
        this.t -= point.t;

        return this;
    }

    public DataPoint mul( double factor ) {

        this.x *= factor;
        this.t *= factor;

        return this;
    }

    public static Vector1D crossProduct( DataPoint left, DataPoint right ) {

        Vector1D.Sign sign   = Vector1D.Sign.ZERO;
        double        module = left.t*right.x - left.x*right.t;

        if (module == 0) {

            sign = Vector1D.Sign.ZERO;

        } else {

            sign = (module > 0) ? Vector1D.Sign.POSITIVE : Vector1D.Sign.NEGATIVE;
        }

        return new Vector1D( sign, module );
    }

    public double x() {

        return this.x;
    }

    public double t() {

        return this.t;
    }

    public double module() {

        return Math.sqrt( this.x*this.x + this.t*this.t );
    }
}
