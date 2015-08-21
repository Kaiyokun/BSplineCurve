public class Vector1D {

    private Sign   sign;
    private double module;

    public enum Sign { POSITIVE, NEGATIVE, ZERO };

    public Vector1D() {

        this( Sign.ZERO, 0.0 );
    }

    public Vector1D( Sign sign, double module ) {

        this.sign = sign;

        if (sign == Sign.ZERO) {

            this.module = 0.0;

        } else {

            this.module = Math.abs( module );
        }
    }

    public boolean sameDirection( Vector1D vector ) {

        if (this.sign == vector.sign) {

            return true;
        }

        return false;
    }

    public double module() {

        return this.module;
    }
}
