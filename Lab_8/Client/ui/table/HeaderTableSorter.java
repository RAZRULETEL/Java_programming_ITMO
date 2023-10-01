package Client.ui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.JTable;

public class HeaderTableSorter extends MouseAdapter {

    private final JTable table;

    public HeaderTableSorter(JTable table){
        this.table = table;
    }

    private int sortedColumn = -1;
    @Override
    public void mouseClicked(MouseEvent e) {
        int column = table.getTableHeader().columnAtPoint(e.getPoint());
        final int order;
        if(sortedColumn == column) {
            order = -1;
            sortedColumn = -1;
        }else {
            sortedColumn = column;
            order = 1;
        }
        try {
            if (List.of(new Class<?>[]{Integer.class, int.class, Double.class, double.class, Float.class, float.class}).contains(((CollectionTableModel)table.getModel()).getRealColumnClass(column))) {
                Vector sortedData = ((CollectionTableModel) table.getModel()).getDataVector().stream()
                        .sorted((vec1, vec2) -> Double.compare(Double.parseDouble(vec1.get(column).equals("") ? "0" : (String) vec1.get(column)), Double.parseDouble(vec2.get(column).equals("") ? "0" : (String) vec2.get(column)) * order)).collect(Collectors.toCollection(Vector::new));
                ((CollectionTableModel) table.getModel()).setDataVector(sortedData);
            } else if (((CollectionTableModel)table.getModel()).getRealColumnClass(column) == String.class) {
                Vector sortedData = ((CollectionTableModel) table.getModel()).getDataVector().stream()
                        .sorted((vec1, vec2) -> ((String) vec1.get(column)).compareTo((String) vec2.get(column)) * order).collect(Collectors.toCollection(Vector::new));
                ((CollectionTableModel) table.getModel()).setDataVector(sortedData);
            } else if (((CollectionTableModel)table.getModel()).getRealColumnClass(column) == ZonedDateTime.class) {
                Vector sortedData = ((CollectionTableModel) table.getModel()).getDataVector().stream()
                        .sorted((vec1, vec2) -> ZonedDateTime.parse((String) vec1.get(column), ((CollectionTableModel) table.getModel()).getFormatter()).compareTo(ZonedDateTime.parse((String) vec2.get(column), ((CollectionTableModel) table.getModel()).getFormatter())) * order).collect(Collectors.toCollection(Vector::new));
                ((CollectionTableModel) table.getModel()).setDataVector(sortedData);
            }
        }catch (ClassCastException ignored){}
    }
}
