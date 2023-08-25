/**
 * Copyright (C) 2008 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3D Incremental Voronoi GUI
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

package com.hellblazer.delaunay.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.hellblazer.delaunay.VertexD;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class PointModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private ArrayList<VertexD> data;
    private NumberFormat      numberFormatter;

    PointModel() {
        data = null;
        numberFormatter = new DecimalFormat("0.000");
        data = new ArrayList<>();
    }

    PointModel(List<VertexD> somePoints) {
        data = null;
        numberFormatter = new DecimalFormat("0.000");
        if (somePoints != null && somePoints.size() != 0) {
            data = new ArrayList<>(somePoints.size());
            for (VertexD somePoint : somePoints) {
                data.add(somePoint);
            }

        } else {
            data = new ArrayList<>();
        }
    }

    public synchronized void clear() {
        int oldNumRows = data.size();
        data.clear();
        fireTableRowsUpdated(0, oldNumRows);
    }

    @Override
    public synchronized int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex > 3) {
            return null;
        }
        switch (columnIndex) {
        case 0:
            return "x";

        case 1:
            return "y";

        case 2:
            return "z";
        }
        return null;
    }

    @Override
    public synchronized int getRowCount() {
        return data.size();
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex > getRowCount()) {
            return null;
        }
        if (data != null && data.size() > 0) {
            switch (columnIndex) {
            case 0:
                return numberFormatter.format(data.get(rowIndex).asPoint3f().x);

            case 1:
                return numberFormatter.format(data.get(rowIndex).asPoint3f().y);

            case 2:
                return numberFormatter.format(data.get(rowIndex).asPoint3f().z);
            }
            return null;
        } else {
            return null;
        }
    }

}
