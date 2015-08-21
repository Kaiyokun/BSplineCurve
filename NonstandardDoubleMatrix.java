import java.util.Arrays;

public class NonstandardDoubleMatrix {

    private double[][] standardDoubleMatrix;
    private int        beginRow;
    private int        endRow;
    private int        beginColumn;
    private int        endColumn;

    public NonstandardDoubleMatrix(  int beginRo
                                   , int endRow
                                   , int beginColumn
                                   , int endColumn ) {

        this.beginRow    = beginRow;
        this.endRow      = endRow;
        this.beginColumn = beginColumn;
        this.endColumn   = endColumn;

        this.doubleArray = new double[ this.length() ];
    }

    public double at( int index ) {

        return this.doubleArray[index-this.begin];
    }

    public double set( int index, double value ) {

        return (this.doubleArray[index-this.begin] = value);
    }

    public int beginRow() {

        return this.beginRow;
    }

    public int endRow() {

        return this.endRow;
    }

    public int beginColumn() {

        return this.beginColumn;
    }

    public int endColumn() {

        return this.endColumn;
    }

    public int rowCount() {

        return this.endRow - this.beginRow + 1;
    }

    public int columnCount() {

        return this.endColumn - this.beginColumn + 1;
    }

    public double[][] toMatrix() {

        double[][] standardDoubleMatrix
            = Arrays.copyOf( this.standardDoubleMatrix, this.length() );
    }
}
