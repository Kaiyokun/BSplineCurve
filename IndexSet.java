public class IndexSet<T> {

    public IndexSet( T[] array, int begin, int end ) {

        this.array = array;
        this.begin = begin;
        this.end   = end;
    }

    public T at( int index ) {

        return this.array[index-this.begin];
    }

    public T set( int index, T value ) {

        return (this.array[index-this.begin] = value);
    }

    public int begin() {

        return this.begin;
    }

    public int end() {

        return this.end;
    }

    public int length() {

        return this.array.length;
    }

    private T[] array;
    private int begin;
    private int end;
}
