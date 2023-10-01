package Client.ui.table;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.table.DefaultTableModel;

import Client.ui.interfaces.CellEditListener;
import Shared.resources.Coordinates;
import Shared.resources.Location;
import Shared.resources.Route;

public class CollectionTableModel extends DefaultTableModel {
    private static final String LocationFromPrefix = "start_", LocationToPrefix = "finish_";
    private final ArrayList<CellEditListener> listeners = new ArrayList<>(), errorListeners = new ArrayList<>();
    private CollectionField[] rawColumns, tableColumns = new CollectionField[0];
    private Class<?>[] columnsTypes;
    private ArrayList<Predicate<? super Vector>> filters = new ArrayList<>();
    private Vector<Vector> rawData;
    private String user;
    private ListResourceBundle currentLocale;

    public CollectionTableModel() {
    }


    public void setDataFromCollection(CollectionField[] columns, int[] keys, Route[] collection, ListResourceBundle locale) {
        currentLocale = locale;
        this.rawColumns = columns;
        try {
            ArrayList<String> fields = collectionFieldsToColumns(columns);
            fields.replaceAll(key -> currentLocale.getString(key));
            setDataVector(getRoutesFields(columns, keys, collection), fields.toArray());
            rawData = getDataVector();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        String newValue = aValue.toString();
        String oldValue = (String) getValueAt(row, column);
        Class<?> colType = columnsTypes[column];
        try {
            if (colType == Integer.class || colType == int.class)
                Integer.valueOf(newValue).intValue();
            if (colType == Float.class || colType == float.class)
                Float.valueOf(newValue.replace(",", ".")).floatValue();
            if (colType == Double.class || colType == double.class)
                Double.valueOf(newValue.replace(",", ".")).doubleValue();
            if (colType == ZonedDateTime.class)
                ZonedDateTime.parse(newValue, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).localizedBy(currentLocale.getLocale()));
            if (!Objects.equals(newValue, oldValue)) {
                Vector<Object> rowVector = dataVector.elementAt(row);
                rowVector.setElementAt(aValue, column);
                fireTableCellUpdated(row, column);
                for (CellEditListener listener : listeners)
                    listener.onCellEdit(new TableCellEditEvent(this, row, column, oldValue, colType));
            }
        } catch (NumberFormatException | NullPointerException | DateTimeParseException e) {
            for (CellEditListener listener : errorListeners)
                listener.onCellEdit(new TableCellEditEvent(this, row, column, e, colType));
        }

    }

    public String[][] getRoutesFields(CollectionField[] fields, int[] keys, Route[] routes) throws NoSuchFieldException, IllegalAccessException {

        List<String[]> result = new ArrayList<>();

        for (int l = 0; l < keys.length; l++) {

            Route route = routes[l];

            if (route != null) {

                ArrayList<String> routeFields = new ArrayList<>();

                for (CollectionField field : fields) {

                    Field f;
                    Object value;

                    if (field.getClassContainig() == Route.class) {
                        f = Route.class.getDeclaredField(field.getNameInClass());
                        f.setAccessible(true);
                        value = f.get(route);
                        if (value == null) {
                            for (int i = 0; i < Arrays.stream(fields).filter(e -> e.getClassContainig() == Location.class).count(); i++)
                                routeFields.add("");
                        } else if (value.getClass() == Location.class) {
                            Location location = (Location) value;
                            for (Object locField : Arrays.stream(fields).filter(e -> e.getClassContainig() == Location.class).toArray()) {
                                Field fl = Location.class.getDeclaredField(((CollectionField) locField).getNameInClass());
                                fl.setAccessible(true);
                                value = fl.get(location);
                                routeFields.add(value != null ? value.toString() : null);
                            }
                        } else if (value.getClass() == Coordinates.class) {
                            Coordinates coordinates = (Coordinates) value;
                            for (Object coordField : Arrays.stream(fields).filter(e -> e.getClassContainig() == Coordinates.class).toArray()) {
                                Field fc = Coordinates.class.getDeclaredField(((CollectionField) coordField).getNameInClass());
                                fc.setAccessible(true);
                                value = fc.get(coordinates);
                                routeFields.add(value != null ? value.toString() : null);
                            }
                        } else if (value.getClass() == ZonedDateTime.class)
                            routeFields.add(((ZonedDateTime) value).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).localizedBy(currentLocale.getLocale())));
                        else
                            routeFields.add(value.toString());
                    } else if (field.getClassContainig() == HashMap.class) {
                        if (field == CollectionField.KEY)
                            value = keys[l];
                        else
                            value = null;
                        routeFields.add((value != null) ? value.toString() : null);
                    }
                }
                result.add(routeFields.toArray(new String[0]));
            }
        }
        return result.toArray(new String[0][0]);
    }

    private ArrayList<String> collectionFieldsToColumns(CollectionField[] fields) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Class<?>> columnTypes = new ArrayList<>();
        ArrayList<CollectionField> columns = new ArrayList<>();
        for (CollectionField field : fields) {
            if (field == CollectionField.LOCATION_FROM || field == CollectionField.LOCATION_TO) {
                for (Object coordField : Arrays.stream(fields).filter(e -> e.getClassContainig() == Location.class).toArray()) {
                    try {
                        columnTypes.add(((CollectionField) coordField).getClassContainig().getDeclaredField(((CollectionField) coordField).getNameInClass()).getType());
                    } catch (NoSuchFieldException ignored) {
                    }
                    columns.add((CollectionField) coordField);
                    result.add((field == CollectionField.LOCATION_FROM ? LocationFromPrefix : LocationToPrefix) + ((CollectionField) coordField).getNameInTable());
                }
            } else if (field == CollectionField.COORDINATES) {
                for (Object locField : Arrays.stream(fields).filter(e -> e.getClassContainig() == Coordinates.class).toArray()) {
                    try {
                        columnTypes.add(((CollectionField) locField).getClassContainig().getDeclaredField(((CollectionField) locField).getNameInClass()).getType());
                    } catch (NoSuchFieldException ignored) {
                    }
                    columns.add((CollectionField) locField);
                    result.add(((CollectionField) locField).getNameInTable());
                }
            } else if (field.getClassContainig() == Route.class || field.getClassContainig() == HashMap.class) {
                columns.add(field);
                try {
                    columnTypes.add(field.getClassContainig().getDeclaredField(field.getNameInClass()).getType());
                } catch (NoSuchFieldException e) {
                    if (field == CollectionField.KEY)
                        columnTypes.add(int.class);
                    else
                        columnTypes.add(String.class);
                }
                result.add(field.getNameInTable());
            }
        }
        this.tableColumns = columns.toArray(new CollectionField[0]);
        this.columnsTypes = columnTypes.toArray(new Class<?>[0]);
        return result;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    public Class<?> getRealColumnClass(int column) {//if use getColumnClass it will break table edit on double click, because it can not process primitives and Double, Float
        return columnsTypes[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return tableColumns[column].isUserCanEdit() && Objects.equals(getValueAt(row, List.of(tableColumns).indexOf(CollectionField.USER)), user);
    }

    public void setDataVector(Vector<? extends Vector> dataVector) {
        this.setDataVector(dataVector, this.columnIdentifiers);
    }

    public void addCellEditListener(CellEditListener listener) {
        listeners.add(listener);
    }

    public void addCellEditErrorListener(CellEditListener listener) {
        errorListeners.add(listener);
    }

    synchronized public void addRoute(int key, Route route, String user) {
        if (tableColumns != null && rawColumns != null)
            try {
                int userColumn = List.of(tableColumns).indexOf(CollectionField.USER);
                Object[][] values = getRoutesFields(rawColumns, new int[]{key}, new Route[]{route});
                Vector<Object> newRow = convertToVector(values[0]);
                newRow.set(userColumn, user);
                removeRoute(route.getId());
                Vector<Vector> box = new Vector<>();
                box.add(newRow);
//                if(filterData(box).size() == 1)
//                    addRow(newRow);
                rawData.add(newRow);
                setDataVector(rawData);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
    }

    public void removeRoute(int id) {
        int idColumn = List.of(tableColumns).indexOf(CollectionField.ID);
        for (Vector vec : getDataVector())
            if (Integer.parseInt((String) vec.get(idColumn)) == id) {
                removeRow(getDataVector().indexOf(vec));
                break;
            }
        AtomicReference<Vector> routeToRemove = new AtomicReference<>();
        rawData.forEach(vec -> {
            if (Integer.parseInt((String) vec.get(idColumn)) == id)
                routeToRemove.set(vec);
        });
        if (routeToRemove.get() != null)
            rawData.remove(routeToRemove.get());
    }

    public void addFilter(Predicate<? super Vector> filter) {
        filters.add(filter);
        setDataVector(rawData);
    }

    public void removeFilter(Predicate<? super Vector> filter) {
        filters.remove(filter);
        setDataVector(rawData);
    }

    public void clearFilters() {
        filters.clear();
        setDataVector(rawData);
    }

    private Vector<? extends Vector> filterData(Vector<? extends Vector> data) {
        if (filters == null || filters.size() == 0)
            return data;
        Predicate<Vector> joinedFilter = vector -> true;
        for (Predicate<? super Vector> filter : filters) joinedFilter = joinedFilter.and(filter);
        Predicate<Vector> finalJoinedFilter = joinedFilter;
        return data.stream().filter(finalJoinedFilter).collect(Collectors.toCollection(Vector::new));
    }

    @Override
    synchronized public void setDataVector(Vector<? extends Vector> dataVector, Vector<?> columns) {
        rawData = (Vector<Vector>) dataVector;
        super.setDataVector(filterData(dataVector), columns);
    }

    synchronized public void setUsers(HashMap<String, ArrayList<Integer>> usersObjects) {
        int user_column = List.of(tableColumns).indexOf(CollectionField.USER);
        int id_column = List.of(tableColumns).indexOf(CollectionField.ID);
        if (user_column == -1 || id_column == -1)
            return;
        for (String user : usersObjects.keySet()) {
            ArrayList<Integer> ids = usersObjects.get(user);
            rawData.forEach(vec -> {
                if (ids.contains(Integer.parseInt((String) vec.get(id_column))))
                    vec.set(user_column, user);
            });
        }
        setDataVector(rawData);
    }

    public void setCurrentUser(String user) {
        this.user = user;
    }

    public int getColumnIndex(CollectionField field) {
        return List.of(tableColumns).indexOf(field);
    }

    public Route getCollectionObject(int row) throws IllegalArgumentException {
        Route route = new Route();
        route.setName((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.NAME)));
        route.setCoordinates(new Coordinates());
        route.getCoordinates().setX(Double.parseDouble((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.COORDINATES_X))));
        route.getCoordinates().setY(Integer.parseInt((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.COORDINATES_Y))));
        Location from = new Location();
        from.setX(Double.parseDouble((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.LOCATION_X))));
        from.setY(Float.parseFloat((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.LOCATION_Y))));
        from.setZ(Double.parseDouble((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.LOCATION_Z))));
        from.setName((String) getValueAt(row, List.of(tableColumns).indexOf(CollectionField.LOCATION_NAME)));
        route.setFrom(from);
        if (route.getTo() != null || !((String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.LOCATION_X))).equals("")) {
            Location to = new Location();
            to.setX(Double.parseDouble((String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.LOCATION_X))));
            String y = (String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.LOCATION_Y));
            to.setY(Float.parseFloat(y.equals("") ? "0" : y));
            String z = (String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.LOCATION_Z));
            to.setZ(Double.parseDouble(z.equals("") ? "0" : z));
            to.setName((String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.LOCATION_NAME)));
            route.setTo(to);
        } else
            route.setTo(null);
        route.setDistance(((String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.DISTANCE))).equals("") ? null : Integer.valueOf((String) getValueAt(row, List.of(tableColumns).lastIndexOf(CollectionField.DISTANCE))));
        return route;
    }

    synchronized public void setLocale(ListResourceBundle newLocale) {
        int dateIndx = List.of(tableColumns).indexOf(CollectionField.CREATION_DATE);
        if (dateIndx != -1) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).localizedBy(currentLocale.getLocale());
            rawData = rawData.stream().peek(vec -> vec.set(dateIndx, ZonedDateTime.parse((String) vec.get(dateIndx), formatter).format(formatter.localizedBy(newLocale.getLocale())))).collect(Collectors.toCollection(Vector::new));
            ArrayList<String> fields = collectionFieldsToColumns(rawColumns);
            fields.replaceAll(newLocale::getString);
            setDataVector(rawData, convertToVector(fields.toArray()));
        }
        currentLocale = newLocale;
    }

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).localizedBy(currentLocale.getLocale());
    }
}
