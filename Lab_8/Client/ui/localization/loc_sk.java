package Client.ui.localization;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Locale;

import Shared.commands.enums.CommandMessage;

public class loc_sk extends ListResourceBundle {
    private static final Object[][] contents = new Object[][] {
// AuthPanel
            { UIField.AuthTitle, "Avtorizirajte se ali se registrirajte" },
            { UIField.Login, "Uporabniško ime" },
            { UIField.Password, "Geslo" },
            { UIField.Register, "Registrirajte se" },
            { UIField.Authorize, "Avtorizirajte se" },
            { CommandMessage.AuthorizationSuccess, "Avtorizacija uspešna" },
            { CommandMessage.RegistrationSuccess, "Registracija uspešna" },
            { CommandMessage.UserAlreadyExists, "Uporabnik že obstaja" },
            { CommandMessage.UserNotExists, "Uporabnik ne obstaja" },
            { CommandMessage.UserNotAuthorized, "Uporabnik ni avtoriziran" },
            { CommandMessage.IncorrectPassword, "Napačno geslo" },
// User Settings
            { UIField.LoggedAs, "Prijavljeni ste kot" },
            { UIField.Logout, "Odjava" },
// TablePanel
            { UIField.TableTitle, "Tabela objektov" },
            { UIField.Visualization, "vizualizacija" },
            { UIField.DataFormatError, "Datum v napačnem formatu" },
            { UIField.RemoveBiggerObjects, "Odstrani\n večje objekte" },
            { UIField.RemoveByKey, "Odstrani\n po ključu" },
            { UIField.RemoveWithBiggerKey, "Odstrani s\n ključem večjim od\n določenega" },
            { UIField.UpdateById, "Zamenjaj\n po id" },
            { UIField.UniqueDistances, "Samo\n edinstvene\n razdalje" },
            { UIField.Add, "Dodaj" },
            { UIField.Clear, "Počisti" },
            { UIField.Delete, "Izbriši" },
            { UIField.ReplaceIfLower, "Zamenjaj, če\n je novi objekt\n manjši" },
            { UIField.AddFilter, "Dodaj filter" },
            { UIField.Execute, "Izvedi" },
            { UIField.ExecuteFile, "Izvedi\n skripto" },
            { UIField.WriteFilterValue, "Vnesite vrednost filtra" },
// VisualisationPanel
            { UIField.Mass, "masa" },
            { UIField.Save, "Shrani" },
            { UIField.VisualizationTitle, "Vizualizacija objektov" },
            { UIField.Radius, "radij" },
            { UIField.X, "x" },
            { UIField.Y, "y" },
            { UIField.Stop, "Ustavi" },
            { UIField.Start, "Zaženi" },
// ObjectFields
            { UIField.Id, "id" },
            { UIField.Key, "ključ" },
            { UIField.Name, "ime" },
            { UIField.CoordsX, "x koordinate" },
            { UIField.CoordsY, "y koordinate" },
            { UIField.StartX, "začetne x koordinate" },
            { UIField.StartY, "začetne y koordinate" },
            { UIField.StartZ, "začetne z koordinate" },
            { UIField.StartName, "začetno ime" },
            { UIField.FinishX, "končne x koordinate" },
            { UIField.FinishY, "končne y koordinate" },
            { UIField.FinishZ, "končne z koordinate" },
            { UIField.FinishName, "končno ime" },
            { UIField.Distance, "razdalja" },
            { UIField.CreationDate, "datum ustvarjanja" },
            { UIField.User, "uporabnik" },
// Other
            { UIField.NumberFormatError, "To ni številka tipa" },
            { UIField.CannotBeNull, "To polje ne sme biti prazno ali null" },
            { UIField.FileName, "Ime datoteke" },
            { CommandMessage.UnexpectedError, "Nepričakovana napaka" },
            { CommandMessage.InvalidArgument, "Vnesen je napačen %s" },
            { CommandMessage.InvalidArgumentCount, "Napačno število argumentov" },
            { CommandMessage.Success, "Uspešno izvedeno" },
            { CommandMessage.ServerNotResponded, "Server neodpovedal v očakávanom čase" },
            { CommandMessage.ArgumentNotPresented, "Nezadali ste %s" },
            { CommandMessage.RecursionRestricted, "Rekurzia je zakázaná" },
            { UIField.ServerUnavailable, "Zdá sa, že server nie je dostupný, simulácia nie je možná" }
    };

    @Override
    public Locale getLocale(){
        return new Locale("sk");
    }

    @Override
    protected Object[][] getContents() {
        return Arrays.stream(contents).peek(e -> e[0] = e[0].toString()).toArray(Object[][]::new);
    }

    @Override
    public String toString() {
        return "Slovenščina";
    }
}
