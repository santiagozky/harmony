/**
 *
 */
package org.opennaas.extensions.gmpls.client.utils;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class TableFitter {

    /**
     * Fit width of all columns to content.
     *
     * @param table
     *                to work on
     */
    public static void fitColumnToContent(final JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableFitter.fitColumnToContent(table, i);
        }
    }

    /**
     * fit cell size to table content
     * @param table
     */
    public static void fitToContent(final JTable table) {
        TableFitter.fitColumnToContent(table);
        TableFitter.packRows(table, 1);

    }

    /**
     * Fit width specified column to content.
     *
     * @param table
     *                to work on
     * @param colIndex
     *                the column
     */
    public static void fitColumnToContent(final JTable table, final int colIndex) {
        final TableColumn column = table.getColumnModel().getColumn(colIndex);
        if (column == null) {
            return;
        }

        final int modelIndex = column.getModelIndex();
        TableCellRenderer renderer, headerRenderer;
        Component component;
        int colContentWidth = 0;
        int headerWidth = 0;
        final int rows = table.getRowCount();

        // Get width of column header
        headerRenderer = column.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = table.getTableHeader().getDefaultRenderer();
        }

        final Component comp =
                headerRenderer.getTableCellRendererComponent(table, column
                        .getHeaderValue(), false, false, 0, 0);
        headerWidth =
                comp.getPreferredSize().width
                        + table.getIntercellSpacing().width;

        // Get max width of column content
        for (int i = 0; i < rows; i++) {
            renderer = table.getCellRenderer(i, modelIndex);
            final Object valueAt = table.getValueAt(i, modelIndex);
            component =
                    renderer.getTableCellRendererComponent(table, valueAt,
                            false, false, i, modelIndex);
            colContentWidth =
                    Math.max(colContentWidth,
                            component.getPreferredSize().width
                                    + table.getIntercellSpacing().width);
        }
        final int colWidth = Math.max(colContentWidth + 4, headerWidth + 4);
        column.setPreferredWidth(colWidth);
        if(colIndex<table.getColumnCount()-1) {
            column.setMaxWidth(colWidth);
            column.setMinWidth(colWidth);
        }
    }

    // Returns the preferred height of a row.
    // The result is equal to the tallest cell in the row.
    private static int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
        // Get the current default height for all rows
        int height = table.getRowHeight();

        // Determine highest cell in the row
        for (int c = 0; c < table.getColumnCount(); c++) {
            TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
            Component comp = table.prepareRenderer(renderer, rowIndex, c);
            int h = comp.getPreferredSize().height + 2 * margin;
            height = Math.max(height, h);
        }
        return height;
    }

    // The height of each row is set to the preferred height of the
    // tallest cell in that row.
    public static void packRows(JTable table, int margin) {
        packRows(table, 0, table.getRowCount(), margin);
    }

    // For each row >= start and < end, the height of a
    // row is set to the preferred height of the tallest cell
    // in that row.
    private static void packRows(JTable table, int start, int end, int margin) {
        for (int r = start; r < end; r++) {
            // Get the preferred height
            int h = getPreferredRowHeight(table, r, margin);

            // Now set the row height using the preferred height
            if (table.getRowHeight(r) != h) {
                table.setRowHeight(r, h);
            }
        }
    }
}
