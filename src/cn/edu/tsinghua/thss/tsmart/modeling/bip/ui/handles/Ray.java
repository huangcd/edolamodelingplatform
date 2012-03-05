package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import org.eclipse.draw2d.geometry.Point;


/**
 * Represents a 2-dimensional directional Vector, or Vector.
 * {@link java.util.Vector} is commonly imported, so the name Ray was chosen.
 * 
 */
public final class Ray {

    /** the X value */
    public int x;
    /** the Y value */
    public int y;

    /**
     * Constructs a Vector pointed in the specified direction.
     * 
     * @param x
     *            X value.
     * @param y
     *            Y value.
     * @since 2.0
     */
    public Ray(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Vector pointed in the direction specified by a Point.
     * 
     * @param p
     *            the Point
     * @since 2.0
     */
    public Ray(Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Constructs a Vector representing the difference between two provided Vectors.
     * 
     * @param start
     *            The start Vector
     * @param end
     *            The end Vector
     * @since 2.0
     */
    public Ray(Ray start, Ray end) {
        x = end.x - start.x;
        y = end.y - start.y;
    }

    /**
     * Calculates the magnitude of the cross product of this Vector with another.
     * Represents the amount by which two Vectors are directionally different.
     * Parallel Vectors return a value of 0.
     * 
     * @param r
     *            Vector being compared
     * @return The assimilarity
     * @see #similarity(Ray)
     * @since 2.0
     */
    public int assimilarity(Ray r) {
        return Math.abs(x * r.y - y * r.x);
    }

    /**
     * Calculates the dot product of this Vector with another.
     * 
     * @param r
     *            the Vector used to perform the dot product
     * @return The dot product
     * @since 2.0
     */
    public int dotProduct(Ray r) {
        return x * r.x + y * r.y;
    }

    /**
     * Calculates the dot product of this Vector with another.
     * 
     * @param r
     *            the Vector used to perform the dot product
     * @return The dot product as <code>long</code> to avoid possible integer
     *         overflow
     * @since 3.4.1
     */
    long dotProductL(Ray r) {
        return (long) x * r.x + (long) y * r.y;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Ray) {
            Ray r = (Ray) obj;
            return x == r.x && y == r.y;
        }
        return false;
    }

    /**
     * Creates a new Vector which is the sum of this Vector with another.
     * 
     * @param r
     *            Vector to be added with this Vector
     * @return a new Vector
     * @since 2.0
     */
    public Ray getAdded(Ray r) {
        return new Ray(r.x + x, r.y + y);
    }

    /**
     * Creates a new Vector which represents the average of this Vector with another.
     * 
     * @param r
     *            Vector to calculate the average.
     * @return a new Vector
     * @since 2.0
     */
    public Ray getAveraged(Ray r) {
        return new Ray((x + r.x) / 2, (y + r.y) / 2);
    }

    /**
     * Creates a new Vector which represents this Vector scaled by the amount
     * provided.
     * 
     * @param s
     *            Value providing the amount to scale.
     * @return a new Vector
     * @since 2.0
     */
    public Ray getScaled(int s) {
        return new Ray(x * s, y * s);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (x * y) ^ (x + y);
    }

    /**
     * Returns true if this Vector has a non-zero horizontal comonent.
     * 
     * @return true if this Vector has a non-zero horizontal comonent
     * @since 2.0
     */
    public boolean isHorizontal() {
        return x != 0;
    }

    /**
     * Calculates the similarity of this Vector with another. Similarity is defined
     * as the absolute value of the dotProduct()
     * 
     * @param r
     *            Vector being tested for similarity
     * @return the Similarity
     * @see #assimilarity(Ray)
     * @since 2.0
     */
    public int similarity(Ray r) {
        return Math.abs(dotProduct(r));
    }
}

