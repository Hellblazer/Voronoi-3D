/**
 * Copyright (C) 2010 Hal Hildebrand. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hellblazer.delaunay;

import java.util.LinkedList;
import java.util.Set;

/**
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class EarSet<T extends Vertex.Type> implements StarVisitor<T> {
    private static class Ear<T extends Vertex.Type> {
        final OrientedFace<T> face;
        final int             hashcode;

        Ear(OrientedFace<T> face) {
            this.face = face;
            int hash = 0;
            for (Vertex<T> v : face) {
                hash ^= v.hashCode();
            }
            hashcode = hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Ear) {
                Ear<?> ear = (Ear<?>) obj;
                if ((face.getIncident() == ear.face.getIncident() && face.getAdjacent() == ear.face.getAdjacent()) ||
                    (face.getAdjacent() == ear.face.getIncident() && face.getIncident() == ear.face.getAdjacent())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hashcode;
        }
    }

    private LinkedList<OrientedFace<T>> ears    = new LinkedList<>();
    private Set<Ear<T>>                 visited = new OaHashSet<>();

    public LinkedList<OrientedFace<T>> getEars() {
        return ears;
    }

    @Override
    public void visit(V vertex, Tetrahedron<T> t, Vertex<T> x, Vertex<T> y, Vertex<T> z) {
        OrientedFace<T> face = t.getFace(z);
        if (visited.add(new Ear<T>(face))) {
            ears.add(face);
        }
        face = t.getFace(x);
        if (visited.add(new Ear<T>(face))) {
            ears.add(face);
        }
        face = t.getFace(y);
        if (visited.add(new Ear<T>(face))) {
            ears.add(face);
        }
    }
}
