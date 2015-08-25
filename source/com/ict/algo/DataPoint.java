package com.ict.algo;

/**
 * 用于描述二维点P(t,x)的类
 */
public class DataPoint {

    public DataPoint( double t, double x ) {

        this.t = t;
        this.x = x;
    }

    public DataPoint() {

        this( 0.0, 0.0 );
    }

    public DataPoint( DataPoint point ) {

        this( point.t, point.x );
    }

    public DataPoint( String text ) {

        String[] point = text.split("\t");

        this.t = Double.parseDouble( point[0] );
        this.x = Double.parseDouble( point[1] );
    }

    public DataPoint neg() {

        DataPoint result = new DataPoint();

        result.t = -this.t;
        result.x = -this.x;

        return result;
    }

    public DataPoint add( DataPoint point ) {

        this.t += point.t;
        this.x += point.x;

        return this;
    }

    public DataPoint sub( DataPoint point ) {

        return this.add( point.neg() );
    }

    public DataPoint mul( double factor ) {

        this.t *= factor;
        this.x *= factor;

        return this;
    }

    public static DataPoint add( DataPoint left, DataPoint right ) {

        DataPoint result = new DataPoint( left );

        return result.add( right );
    }

    public static DataPoint sub( DataPoint left, DataPoint right ) {

        return right.neg().add( left );
    }

    public static DataPoint mul( double factor, DataPoint point ) {

        DataPoint result = new DataPoint( point );

        return result.mul( factor );
    }

    public double t() {

        return this.t;
    }

    public double x() {

        return this.x;
    }

    public double module() {

        return Math.sqrt( this.t*this.t + this.x*this.x );
    }

    public double distance( DataPoint point ) {

        return sub( point, this ).module();
    }

    public double slope( DataPoint point ) {

        DataPoint result = new DataPoint( point );

        result.sub( this );

        return result.x / result.t;
    }

    public boolean equals( Object otherObject ) {

        if (this == otherObject) {

            return true;
        }

        if (null == otherObject) {

            return false;
        }

        if ( !(otherObject instanceof DataPoint) ) {

            return false;
        }

        DataPoint other = (DataPoint) otherObject;

        return (this.t == other.t) && (this.x == other.x);
    }

    public int hashCode() {

        return new Double( this.t ).hashCode()
            + 3*new Double( this.x ).hashCode();
    }

    public String toString() {

        return this.t + "\t" + this.x;
    }

    public static final DataPoint ZERO = new DataPoint();

    private double t;
    private double x;
}
