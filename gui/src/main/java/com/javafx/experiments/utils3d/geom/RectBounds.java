/*
 * Copyright (c) 1998, 2015, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.javafx.experiments.utils3d.geom;

/**
 * A simple object which carries bounds information as floats, and has no Z
 * components.
 */
public final class RectBounds extends BaseBounds {
    // maximum x value of bounding box
    private float maxX;
    // maximum y value of bounding box
    private float maxY;
    // minimum x value of bounding box
    private float minX;
    // minimum y value of bounding box
    private float minY;

    /**
     * Create an axis aligned bounding rectangle object, with an empty bounds
     * where maxX < minX and maxY < minY.
     */
    public RectBounds() {
        minX = minY = 0.0f;
        maxX = maxY = -1.0f;
    }

    /**
     * Creates a RectBounds based on the minX, minY, maxX, and maxY values
     * specified.
     */
    public RectBounds(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RECTANGLE.
     */
    public RectBounds(Rectangle other) {
        setBounds(other.x, other.y, other.x + other.width,
                  other.y + other.height);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RectBounds object.
     */
    public RectBounds(RectBounds other) {
        setBounds(other);
    }

    public void add(float x, float y) {
        unionWith(x, y, x, y);
    }

    @Override
    public void add(float x, float y, float z) {
        if (z != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        unionWith(x, y, x, y);
    }

    @Override
    public void add(Point2D p) {
        add(p.x, p.y);
    }

    @Override
    public boolean contains(float x, float y) {
        if (isEmpty()) {
            return false;
        }
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    @Override
    public boolean contains(Point2D p) {
        if ((p == null) || isEmpty()) {
            return false;
        }
        return (p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY);
    }

    /**
     * Determines whether the given <code>other</code> RectBounds is completely
     * contained within this RectBounds. Equivalent RectBounds will return true.
     *
     * @param other
     *            The other rect bounds to check against.
     * @return Whether the other rect bounds is contained within this one, which
     *         also includes equivalence.
     */
    public boolean contains(RectBounds other) {
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        return minX <= other.minX && maxX >= other.maxX && minY <= other.minY
               && maxY >= other.maxY;
    }

    @Override
    public BaseBounds copy() {
        return new RectBounds(minX, minY, maxX, maxY);
    }

    @Override
    public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) {
            return makeEmpty();
        }
        if (other.getBoundsType() == BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            minX = rb.getMinX();
            minY = rb.getMinY();
            maxX = rb.getMaxX();
            maxY = rb.getMaxY();
        } else if (other.getBoundsType() == BoundsType.BOX) {
            return new BoxBounds((BoxBounds) other);
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(float minX, float minY, float minZ,
                                          float maxX, float maxY, float maxZ) {
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) {
            return makeEmpty();
        }
        if ((minZ == 0) && (maxZ == 0)) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public BaseBounds deriveWithNewBounds(Rectangle other) {
        if (other.width < 0 || other.height < 0) {
            return makeEmpty();
        }
        setBounds(other.x, other.y, other.x + other.width,
                  other.y + other.height);
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBoundsAndSort(float minX, float minY,
                                                 float minZ, float maxX,
                                                 float maxY, float maxZ) {
        if ((minZ == 0) && (maxZ == 0)) {
            setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
            return this;
        }

        BaseBounds bb = new BoxBounds();
        bb.setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return bb;
    }

    @Override
    public BaseBounds deriveWithPadding(float h, float v, float d) {
        if (d == 0) {
            grow(h, v);
            return this;
        }
        BoxBounds bb = new BoxBounds(minX, minY, 0, maxX, maxY, 0);
        bb.grow(h, v, d);
        return bb;
    }

    @Override
    public BaseBounds deriveWithUnion(BaseBounds other) {
        if (other.getBoundsType() == BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            unionWith(rb);
        } else if (other.getBoundsType() == BoundsType.BOX) {
            BoxBounds bb = new BoxBounds((BoxBounds) other);
            bb.unionWith(this);
            return bb;
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    @Override
    public boolean disjoint(float x, float y, float width, float height) {
        if (isEmpty()) {
            return true;
        }
        return (x + width < minX || y + height < minY || x > maxX || y > maxY);
    }

    public boolean disjoint(RectBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return true;
        }
        return (other.getMaxX() < minX || other.getMaxY() < minY
                || other.getMinX() > maxX || other.getMinY() > maxY);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final RectBounds other = (RectBounds) obj;
        if (minX != other.getMinX()) {
            return false;
        }
        if (minY != other.getMinY()) {
            return false;
        }
        if (maxX != other.getMaxX()) {
            return false;
        }
        if (maxY != other.getMaxY()) {
            return false;
        }
        return true;
    }

    // Note: this implementation is exactly the same as BoxBounds. I could put a default
    // implementation in BaseBounds which calls the getters, or I could move the minX, minY
    // etc up to BaseBounds, or I could (maybe?) have BoxBounds extend from RectBounds or
    // have both extend a common parent. In the end I wanted direct access to the fields
    // but this was the only way to get it without making a more major change.
    @Override
    public RectBounds flattenInto(RectBounds bounds) {
        // Create the bounds if we need to
        if (bounds == null) {
            bounds = new RectBounds();
        }
        // Make it empty if we need to
        if (isEmpty()) {
            return bounds.makeEmpty();
        }
        // Populate it with values otherwise
        bounds.setBounds(minX, minY, maxX, maxY);
        return bounds;
    }

    @Override
    public BoundsType getBoundsType() {
        return BoundsType.RECTANGLE;
    }

    /**
     * Convenience function for getting the depth of this RectBounds The
     * dimension along the Z-Axis, since this is a 2D bounds the return value is
     * always 0.0f.
     */
    @Override
    public float getDepth() {
        return 0.0f;
    }

    /**
     * Convenience function for getting the height of this RectBounds The
     * dimension along the Y-Axis.
     */
    @Override
    public float getHeight() {
        return maxY - minY;
    }

    @Override
    public Vec2f getMax(Vec2f max) {
        if (max == null) {
            max = new Vec2f();
        }
        max.x = maxX;
        max.y = maxY;
        return max;
    }

    @Override
    public Vec3f getMax(Vec3f max) {
        if (max == null) {
            max = new Vec3f();
        }
        max.x = maxX;
        max.y = maxY;
        max.z = 0.0f;
        return max;

    }

    @Override
    public float getMaxX() {
        return maxX;
    }

    @Override
    public float getMaxY() {
        return maxY;
    }

    @Override
    public float getMaxZ() {
        return 0.0f;
    }

    @Override
    public Vec2f getMin(Vec2f min) {
        if (min == null) {
            min = new Vec2f();
        }
        min.x = minX;
        min.y = minY;
        return min;
    }

    @Override
    public Vec3f getMin(Vec3f min) {
        if (min == null) {
            min = new Vec3f();
        }
        min.x = minX;
        min.y = minY;
        min.z = 0.0f;
        return min;

    }

    @Override
    public float getMinX() {
        return minX;
    }

    @Override
    public float getMinY() {
        return minY;
    }

    @Override
    public float getMinZ() {
        return 0.0f;
    }

    /**
     * Convenience function for getting the width of this RectBounds. The
     * dimension along the X-Axis.
     */
    @Override
    public float getWidth() {
        return maxX - minX;
    }

    public void grow(float h, float v) {
        minX -= h;
        maxX += h;
        minY -= v;
        maxY += v;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Float.floatToIntBits(minX);
        hash = 79 * hash + Float.floatToIntBits(minY);
        hash = 79 * hash + Float.floatToIntBits(maxX);
        hash = 79 * hash + Float.floatToIntBits(maxY);
        return hash;
    }

    public boolean intersects(BaseBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return false;
        }
        return (other.getMaxX() >= minX && other.getMaxY() >= minY
                && other.getMaxZ() >= getMinZ() && other.getMinX() <= maxX
                && other.getMinY() <= maxY && other.getMinZ() <= getMaxZ());
    }

    @Override
    public boolean intersects(float x, float y, float width, float height) {
        if (isEmpty()) {
            return false;
        }
        return (x + width >= minX && y + height >= minY && x <= maxX
                && y <= maxY);
    }

    @Override
    public void intersectWith(BaseBounds other) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) {
            return;
        }
        if (other.isEmpty()) {
            makeEmpty();
            return;
        }

        minX = Math.max(minX, other.getMinX());
        minY = Math.max(minY, other.getMinY());
        maxX = Math.min(maxX, other.getMaxX());
        maxY = Math.min(maxY, other.getMaxY());
    }

    public void intersectWith(float minX, float minY, float maxX, float maxY) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) {
            return;
        }
        if ((maxX < minX) || (maxY < minY)) {
            makeEmpty();
            return;
        }

        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override
    public void intersectWith(float minX, float minY, float minZ, float maxX,
                              float maxY, float maxZ) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) {
            return;
        }
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) {
            makeEmpty();
            return;
        }

        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override
    public void intersectWith(Rectangle other) {
        float x = other.x;
        float y = other.y;
        intersectWith(x, y, x + other.width, y + other.height);
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        // NaN values will cause the comparisons to fail and return "empty"
        return !(maxX >= minX && maxY >= minY);
    }

    // for convenience, this function returns a reference to itself, so we can
    // change from using "bounds.makeEmpty(); return bounds;" to just
    // "return bounds.makeEmpty()"
    @Override
    public RectBounds makeEmpty() {
        minX = minY = 0.0f;
        maxX = maxY = -1.0f;
        return this;
    }

    /**
     * Adjusts the edges of this RectBounds "outward" toward integral
     * boundaries, such that the rounded bounding box will always full enclose
     * the original bounding box.
     */
    @Override
    public void roundOut() {
        minX = (float) Math.floor(minX);
        minY = (float) Math.floor(minY);
        maxX = (float) Math.ceil(maxX);
        maxY = (float) Math.ceil(maxY);
    }

    /**
     * Set the bounds to the given values.
     */
    public final void setBounds(float minX, float minY, float maxX,
                                float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Set the bounds to match that of the RectBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(RectBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
    }

    /**
     * Sets the bounds based on the given coords, and also ensures that after
     * having done so that this RectBounds instance is normalized.
     */
    public void setBoundsAndSort(float minX, float minY, float maxX,
                                 float maxY) {
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override
    public void setBoundsAndSort(float minX, float minY, float minZ, float maxX,
                                 float maxY, float maxZ) {
        if (minZ != 0 || maxZ != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override
    public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.x, p1.y, p2.x, p2.y);
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    @Override
    public String toString() {
        return "RectBounds { minX:" + minX + ", minY:" + minY + ", maxX:" + maxX
               + ", maxY:" + maxY + "} (w:" + (maxX - minX) + ", h:"
               + (maxY - minY) + ")";
    }

    @Override
    public void translate(float x, float y, float z) {
        setMinX(getMinX() + x);
        setMinY(getMinY() + y);
        setMaxX(getMaxX() + x);
        setMaxY(getMaxY() + y);
    }

    public void unionWith(float minX, float minY, float maxX, float maxY) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY)) {
            return;
        }
        if (this.isEmpty()) {
            setBounds(minX, minY, maxX, maxY);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
    }

    public void unionWith(RectBounds other) {
        // Short circuit union if either bounds is empty.
        if (other.isEmpty()) {
            return;
        }
        if (this.isEmpty()) {
            setBounds(other);
            return;
        }

        minX = Math.min(minX, other.getMinX());
        minY = Math.min(minY, other.getMinY());
        maxX = Math.max(maxX, other.getMaxX());
        maxY = Math.max(maxY, other.getMaxY());
    }

    @Override
    protected void sortMinMax() {
        if (minX > maxX) {
            float tmp = maxX;
            maxX = minX;
            minX = tmp;
        }
        if (minY > maxY) {
            float tmp = maxY;
            maxY = minY;
            minY = tmp;
        }
    }
}
