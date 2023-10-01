package Client.ui.localization;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Locale;

import Shared.commands.enums.CommandMessage;

public class loc_es extends ListResourceBundle {
    private static final Object[][] contents = new Object[][] {
            { UIField.AuthTitle, "Inicie sesión o regístrese"}, { UIField.Login, "Acceso"}, { UIField.Password, "Contraseña"},
            { UIField.Register, "Registro"}, { UIField.Authorize, "Acceso"}, {UIField.LoggedAs, "¿Estás registrado como"},
            { UIField.Logout, "Desconectar"}, { UIField.Visualization, "visualización"}, { UIField.TableTitle, "Tabla de objetos"},
            { UIField.NumberFormatError, "No parece un número como"}, { UIField.CannotBeNull, "Este campo no puede estar vacío o nulo"},
            { UIField.DataFormatError, "Fecha en el formato incorrecto"}, { UIField.X, "x" }, { UIField.Y, "y" }, { UIField.Mass, "masa" },
            { UIField.Save, "Guardar" }, { UIField.VisualizationTitle, "Visualización de objetos" }, { UIField.RemoveBiggerObjects, "Eliminar\n objetos más\n grandes" },
            { UIField.RemoveByKey, "Eliminar\n por clave" }, { UIField.RemoveWithBiggerKey, "Eliminar con\n clave más grande\n que la espec\nificada" },
            { UIField.UpdateById, "Reemplazar\n por id" }, { UIField.UniqueDistances, "Solo\n distancias\n únicas" }, { UIField.Add, "Agregar" },
            { UIField.Clear, "Limpiar" }, { UIField.Delete, "Eliminar" }, { UIField.ReplaceIfLower, "Reemplazar si\n el nuevo objeto\n es más\n pequeño" },
            { UIField.AddFilter, "Agregar filtro"}, { UIField.Start, "Iniciar" }, { UIField.Stop, "Detener" },
            { UIField.Id, "identificación" },
            { UIField.Key, "clave" },
            { UIField.Name, "nombre" },
            { UIField.CoordsX, "x" },
            { UIField.CoordsY, "y" },
            { UIField.StartX, "inicio_x" },
            { UIField.StartY, "inicio_y" },
            { UIField.StartZ, "inicio_z" },
            { UIField.StartName, "inicio_nombre" },
            { UIField.FinishX, "fin_x" },
            { UIField.FinishY, "fin_y" },
            { UIField.FinishZ, "fin_z" },
            { UIField.FinishName, "fin_nombre" },
            { UIField.Distance, "distancia" },
            { UIField.CreationDate, "fecha_creación" },
            { UIField.User, "usuario" },
            { UIField.Execute, "Ejecutar"},
            { UIField.ExecuteFile, "Ejecutar\n script"},
            { UIField.Radius, "radio" },
            { CommandMessage.AuthorizationSuccess, "Autorización exitosa" },
            { CommandMessage.RegistrationSuccess, "Registro exitoso" },
            { CommandMessage.UserAlreadyExists, "Este usuario ya existe" },
            { CommandMessage.UserNotExists, "Este usuario no existe" },
            { CommandMessage.UserNotAuthorized, "Usuario no autorizado" },
            { CommandMessage.IncorrectPassword, "Contraseña incorrecta" },
            { UIField.FileName, "Nombre del archivo" },
            { CommandMessage.UnexpectedError, "Error inesperado" },
            { CommandMessage.InvalidArgument, "Argumento %s inválido" },
            { CommandMessage.InvalidArgumentCount, "Cantidad de argumentos inválida" },
            { CommandMessage.Success, "Operación exitosa" },
            { UIField.WriteFilterValue, "Ingrese el valor del filtro" },
            { CommandMessage.ServerNotResponded, "El servidor no respondió en el tiempo esperado" },
            { CommandMessage.ArgumentNotPresented, "No ha ingresado %s" },
            { CommandMessage.RecursionRestricted, "La recursión está restringida" },
            { UIField.ServerUnavailable, "Parece que el servidor no está disponible, la simulación no es posible" }
    };

    @Override
    public Locale getLocale(){
        return new Locale("es", "DO");
    }

    @Override
    protected Object[][] getContents() {
        return Arrays.stream(contents).peek(e -> e[0] = e[0].toString()).toArray(Object[][]::new);
    }
    @Override
    public String toString() {
        return "Español";
    }
}
