package org.opennaas.extensions.gmpls.client.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class MultiLineCellRenderer extends JTextArea implements
        TableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 8255287692657938636L;

    /**
     * Constructor
     */
    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    /**
     *
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 2) {
            System.out.println((String) value);

            if (((String) value).toLowerCase().contains("active")) {
                System.out.println("green");
                setBackground(Color.green);
            } else if (((String) value).toLowerCase().contains("cancelled")) {
                System.out.println("red");
                setBackground(Color.red);
            } else if (((String) value).toLowerCase().contains("pending")) {
                System.out.println("yellow");
                setBackground(Color.yellow);
            } else {
                System.out.println("no info");
                setBackground(table.getBackground());
            }
        } else {

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
        }

        setFont(table.getFont());
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }
        String display = (value == null) ? "" : value.toString();
        setText(display);

        String[] parts = display.split("\\n");
        FontMetrics fm = getFontMetrics(table.getFont());
        int maxWidth = 0;
        for (String part : parts) {
            maxWidth = Math.max(maxWidth, fm.stringWidth(part));
        }

        setPreferredSize(new Dimension(maxWidth + 10, fm.getHeight()
                * parts.length));
        return this;
    }
}
