import java.util.Arrays;

public class NonstandardDoubleArray {

    private double[] standardDoubleArray;
    private int      begin;
    private int      end;

    public NonstandardDoubleArray( int begin, int end ) {

        this.begin               = begin;
        this.end                 = end;
        this.standardDoubleArray = new double[ this.length() ];
    }

    public double at( int index ) {

        return this.standardDoubleArray[index-this.begin];
    }

    public double set( int index, double value ) {

        return (this.standardDoubleArray[index-this.begin] = value);
    }

    public int begin() {

        return this.begin;
    }

    public int end() {

        return this.end;
    }

    public int length() {

        return this.end - this.begin + 1;
    }

    public double[] toArray() {

        return Arrays.copyOf( this.standardDoubleArray, this.length() );
    }
}
