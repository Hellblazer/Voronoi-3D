/**
 * Copyright (c) 2016 Chiral Behaviors, LLC, all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mesh;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * @author halhildebrand
 *
 */
public class Line extends Cylinder {

    public static TriangleMesh createLine(double h, double r) {
        int div = 3;
        // NOTE: still create mesh for degenerated cylinder
        final int nPonits = div * 2 + 2;
        final int tcCount = (div + 1) * 4 + 1; // 2 cap tex
        final int faceCount = div * 4;

        float textureDelta = 1.f / 256;

        float dA = 1.f / div;
        h *= .5f;

        float points[] = new float[nPonits * 3];
        float tPoints[] = new float[tcCount * 2];
        int faces[] = new int[faceCount * 6];
        int smoothing[] = new int[faceCount];

        int pPos = 0, tPos = 0;

        for (int i = 0; i < div; ++i) {
            double a = dA * i * 2 * Math.PI;

            points[pPos + 0] = (float) (Math.sin(a) * r);
            points[pPos + 2] = (float) (Math.cos(a) * r);
            points[pPos + 1] = (float) h;
            tPoints[tPos + 0] = 1 - dA * i;
            tPoints[tPos + 1] = 1 - textureDelta;
            pPos += 3;
            tPos += 2;
        }

        // top edge
        tPoints[tPos + 0] = 0;
        tPoints[tPos + 1] = 1 - textureDelta;
        tPos += 2;

        for (int i = 0; i < div; ++i) {
            double a = dA * i * 2 * Math.PI;
            points[pPos + 0] = (float) (Math.sin(a) * r);
            points[pPos + 2] = (float) (Math.cos(a) * r);
            points[pPos + 1] = (float) -h;
            tPoints[tPos + 0] = 1 - dA * i;
            tPoints[tPos + 1] = textureDelta;
            pPos += 3;
            tPos += 2;
        }

        // bottom edge
        tPoints[tPos + 0] = 0;
        tPoints[tPos + 1] = textureDelta;
        tPos += 2;

        // add cap central points
        points[pPos + 0] = 0;
        points[pPos + 1] = (float) h;
        points[pPos + 2] = 0;
        points[pPos + 3] = 0;
        points[pPos + 4] = (float) -h;
        points[pPos + 5] = 0;
        pPos += 6;

        // add cap central points
        // bottom cap
        for (int i = 0; i <= div; ++i) {
            double a = (i < div) ? (dA * i * 2) * Math.PI : 0;
            tPoints[tPos + 0] = (float) (Math.sin(a) * 0.5f) + 0.5f;
            tPoints[tPos + 1] = (float) (Math.cos(a) * 0.5f) + 0.5f;
            tPos += 2;
        }

        // top cap
        for (int i = 0; i <= div; ++i) {
            double a = (i < div) ? (dA * i * 2) * Math.PI : 0;
            tPoints[tPos + 0] = 0.5f + (float) (Math.sin(a) * 0.5f);
            tPoints[tPos + 1] = 0.5f - (float) (Math.cos(a) * 0.5f);
            tPos += 2;
        }

        tPoints[tPos + 0] = .5f;
        tPoints[tPos + 1] = .5f;
        tPos += 2;

        int fIndex = 0;

        // build body faces
        for (int p0 = 0; p0 < div; ++p0) {
            int p1 = p0 + 1;
            int p2 = p0 + div;
            int p3 = p1 + div;

            // add p0, p1, p2
            faces[fIndex + 0] = p0;
            faces[fIndex + 1] = p0;
            faces[fIndex + 2] = p2;
            faces[fIndex + 3] = p2 + 1;
            faces[fIndex + 4] = p1 == div ? 0 : p1;
            faces[fIndex + 5] = p1;
            fIndex += 6;

            // add p3, p2, p1
            // *faces++ = SmFace(p3,p1,p2, p3,p1,p2, 1);
            faces[fIndex + 0] = p3 % div == 0 ? p3 - div : p3;
            faces[fIndex + 1] = p3 + 1;
            faces[fIndex + 2] = p1 == div ? 0 : p1;
            faces[fIndex + 3] = p1;
            faces[fIndex + 4] = p2;
            faces[fIndex + 5] = p2 + 1;
            fIndex += 6;

        }
        // build cap faces
        int tStart = (div + 1) * 2;
        int t1 = (div + 1) * 4;
        int p1 = div * 2;

        // bottom cap
        for (int p0 = 0; p0 < div; ++p0) {
            int p2 = p0 + 1;
            int t0 = tStart + p0;
            int t2 = t0 + 1;

            // add p0, p1, p2
            faces[fIndex + 0] = p0;
            faces[fIndex + 1] = t0;
            faces[fIndex + 2] = p2 == div ? 0 : p2;
            faces[fIndex + 3] = t2;
            faces[fIndex + 4] = p1;
            faces[fIndex + 5] = t1;
            fIndex += 6;
        }

        p1 = div * 2 + 1;
        tStart = (div + 1) * 3;

        // top cap
        for (int p0 = 0; p0 < div; ++p0) {
            int p2 = p0 + 1 + div;
            int t0 = tStart + p0;
            int t2 = t0 + 1;

            //*faces++ = SmFace(p0+div+1,p1,p2, t0,t1,t2, 2);
            faces[fIndex + 0] = p0 + div;
            faces[fIndex + 1] = t0;
            faces[fIndex + 2] = p1;
            faces[fIndex + 3] = t1;
            faces[fIndex + 4] = p2 % div == 0 ? p2 - div : p2;
            faces[fIndex + 5] = t2;
            fIndex += 6;
        }

        for (int i = 0; i < div * 2; ++i) {
            smoothing[i] = 1;
        }
        for (int i = div * 2; i < div * 4; ++i) {
            smoothing[i] = 2;
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints()
            .setAll(points);
        mesh.getTexCoords()
            .setAll(tPoints);
        mesh.getFaces()
            .setAll(faces);
        mesh.getFaceSmoothingGroups()
            .setAll(smoothing);

        return mesh;
    }

    public Line(double radius, Point3D a, Point3D b) {
        super(radius, b.subtract(a)
                       .magnitude(),
              3);
        Point3D diff = b.subtract(a);

        Point3D mid = b.midpoint(a);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(),
                                                 mid.getZ());

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize()
                                     .dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle),
                                               axisOfRotation);

        getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
    }
}
