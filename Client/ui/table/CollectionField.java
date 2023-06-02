package Client.ui.table;

import java.util.HashMap;

import Shared.resources.Coordinates;
import Shared.resources.Location;
import Shared.resources.Route;

public enum CollectionField {
    ID("id", Route.class), KEY("key", HashMap.class), NAME("name", Route.class, true), COORDINATES("coordinates", Route.class), CREATION_DATE("creation_date", "creationDate", Route.class),
    LOCATION_FROM(null, "from", Route.class), LOCATION_TO(null, "to", Route.class), DISTANCE("distance", Route.class, true),
    LOCATION_X("x", Location.class, true), LOCATION_Y("y", Location.class, true), LOCATION_Z("z", Location.class, true), LOCATION_NAME("name", Location.class, true),
    COORDINATES_X("coords_x","x", Coordinates.class, true), COORDINATES_Y("coords_y","y", Coordinates.class, true), USER("user", HashMap.class);
    private final String column, field;
    private final Class<?> fieldClass;
    private final boolean userCanEdit;

    CollectionField(String fieldName, Class<?> fieldClass) {
        this(fieldName, fieldName, fieldClass, false);
    }

    CollectionField(String fieldName, Class<?> fieldClass, boolean userCanEdit) {
        this(fieldName, fieldName, fieldClass, userCanEdit);
    }

    CollectionField(String columnName, String fieldName, Class<?> fieldClass) {
        this(columnName, fieldName, fieldClass, false);
    }

    CollectionField(String columnName, String fieldName, Class<?> fieldClass, boolean userCanEdit) {
        column = columnName;
        field = fieldName;
        this.fieldClass = fieldClass;
        this.userCanEdit = userCanEdit;
    }
    public String getNameInClass(){return field;}
    public String getNameInTable(){return column;}
    public Class<?> getClassContainig(){return fieldClass;}
    public boolean isUserCanEdit(){return userCanEdit;}

    public static CollectionField getByNameInClass(String name){
        for (CollectionField field : CollectionField.values()) {
            if (field.getNameInClass().equals(name)) {
                return field;
            }
        }
        return null;
    }
}
