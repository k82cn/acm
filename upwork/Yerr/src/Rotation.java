/**
 * Created by Yerr on 4/26/15.
 */
public enum Rotation {
    CW0 {
        @Override
        public Rotation next() {
            return CW90;
        }
    }, CW90 {
        @Override
        public Rotation next() {
            return CW180;
        }
    }, CW180 {
        @Override
        public Rotation next() {
            return CW270;
        }
    }, CW270 {
        @Override
        public Rotation next() {
            return CW0;
        }
    };

    // Calling rot.next() will return the next enumeration element
    // representing the next 90 degree clock-wise rotation after rot.
    public Rotation next() {
        throw new RuntimeException("Unknown next enum!");
    }
}
