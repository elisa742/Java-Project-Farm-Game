package edu.kit.informatik.queensFarm.module.game;

import java.util.Objects;

/**
 * This class describes the location coordinates.
 *
 * @author uyjad
 * @version 1.0
 */

public class Vector2D {
    private final int x;
    private final int y;

    /**
     * Constructs a vector of coordinates.
     *
     * @param x  x coordinate
     * @param y  x coordinate
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate.
     *
     * @return x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets y coordinate.
     *
     * @return y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Checks whether the given location is upward, left side or right side of the current location.
     * Also checks if the y coordinate is greater than 0.
     *
     * @param vector2DToCheck the location to be checked
     * @return true if the given location is upward, left side or right side of the current location
     * and y coordinate of given location is greater than 0, otherwise false
     */
    public boolean isValidLocation(Vector2D vector2DToCheck) {
        if (vector2DToCheck.getY() < 0) {
            return false;
        }
        if (this.y == vector2DToCheck.getY()) {
            return Math.abs(vector2DToCheck.getX() - this.x) == 1;
        }
        if (this.x == vector2DToCheck.getX()) {
            return vector2DToCheck.getY() - this.y == 1;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Vector2D other = (Vector2D) obj;
        return this.x == other.x && this.y == other.y;
    }
}
