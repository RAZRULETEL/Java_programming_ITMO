package Client.ui.localization;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Locale;

import Shared.commands.enums.CommandMessage;

public class loc_el extends ListResourceBundle {
    private static final Object[][] contents = new Object[][] {
            { UIField.AuthTitle, "Συνδεθείτε ή εγγραφείτε"}, { UIField.Login, "Είσοδος"}, { UIField.Password, "Κωδικός πρόσβασης"},
            { UIField.Register, "Εγγραφή"}, { UIField.Authorize, "Σύνδεση"}, {UIField.LoggedAs, "Είστε συνδεδεμένος ως"},
            {UIField.Logout, "Αποσύνδεση"}, { UIField.Visualization, "οραματισμός"}, { UIField.TableTitle, "Πίνακας αντικειμένων"},
            { UIField.NumberFormatError, "Δεν μοιάζει με αριθμό"}, { UIField.CannotBeNull, "Αυτό το πεδίο δεν μπορεί να είναι κενό ή μηδενικό"},
            { UIField.DataFormatError, "Ημερομηνία σε λάθος μορφή"}, { UIField.X, "χ" }, { UIField.Y, "ψ" }, { UIField.Mass, "μάζα" }, { UIField.Save, "Αποθήκευση" },
            { UIField.VisualizationTitle, "Οπτικοποίηση αντικειμένων" }, { UIField.RemoveBiggerObjects, "Αφαίρεση\n μεγαλύτερων\n αντικειμένων" },
            { UIField.RemoveByKey, "Αφαίρεση\n με βάση το\n κλειδί" }, { UIField.RemoveWithBiggerKey, "Αφαίρεση με\n κλειδί μεγαλύτερο\n από το καθορ\nισμένο" },
            { UIField.UpdateById, "Αντικατ\nάσταση με\n βάση το\n id" }, { UIField.UniqueDistances, "Μόνο\n μοναδικές\n αποστάσεις" }, { UIField.Add, "Προσθήκη" },
            { UIField.Clear, "Καθαρισμός" }, { UIField.Delete, "Διαγραφή" }, { UIField.ReplaceIfLower, "Αντικατάσταση,\n αν το νέο αντικ\nείμενο είναι\n μικρότερο" },
            { UIField.AddFilter, "Προσθήκη φίλτρου" }, { UIField.Start, "Έναρξη" }, { UIField.Stop, "Διακοπή" },
            { UIField.Id, "αναγνωριστικό" },
            { UIField.Key, "κλειδί" },
            { UIField.Name, "όνομα" },
            { UIField.CoordsX, "χ" },
            { UIField.CoordsY, "ψ" },
            { UIField.StartX, "αρχή_χ" },
            { UIField.StartY, "αρχή_ψ" },
            { UIField.StartZ, "αρχή_z" },
            { UIField.StartName, "αρχή_όνομα" },
            { UIField.FinishX, "τέλος_χ" },
            { UIField.FinishY, "τέλος_ψ" },
            { UIField.FinishZ, "τέλος_z" },
            { UIField.FinishName, "τέλος_όνομα" },
            { UIField.Distance, "απόσταση" },
            { UIField.CreationDate, "ημερομηνία_δημιουργίας" },
            { UIField.User, "χρήστης" },
            { UIField.Execute, "Εκτέλεση"},
            { UIField.ExecuteFile, "Εκτέλεση\n σεναρίου"},
            { UIField.Radius, "ακτίνα" },
            { CommandMessage.AuthorizationSuccess, "Επιτυχής εξουσιοδότηση" },
            { CommandMessage.RegistrationSuccess, "Επιτυχής εγγραφή" },
            { CommandMessage.UserAlreadyExists, "Αυτός ο χρήστης υπάρχει ήδη" },
            { CommandMessage.UserNotExists, "Αυτός ο χρήστης δεν υπάρχει" },
            { CommandMessage.UserNotAuthorized, "Ο χρήστης δεν είναι εξουσιοδοτημένος" },
            { CommandMessage.IncorrectPassword, "Λανθασμένος κωδικός πρόσβασης" },
            { UIField.FileName, "Όνομα αρχείου" },
            { CommandMessage.UnexpectedError, "Απρόσμενο σφάλμα" },
            { CommandMessage.InvalidArgument, "Μη έγκυρο %s" },
            { CommandMessage.InvalidArgumentCount, "Μη έγκυρος αριθμός ορισμάτων" },
            { CommandMessage.Success, "Επιτυχής εκτέλεση" },
            { UIField.WriteFilterValue, "Εισαγάγετε την τιμή του φίλτρου" },
            { CommandMessage.ServerNotResponded, "Ο διακομιστής δεν ανταποκρίθηκε στο αναμενόμενο χρονικό διάστημα" },
            { CommandMessage.ArgumentNotPresented, "Δεν εισαγάγατε %s" },
            { CommandMessage.RecursionRestricted, "Η αναδρομή είναι απαγορευμένη" },
            { UIField.ServerUnavailable, "Φαίνεται ότι ο διακομιστής δεν είναι διαθέσιμος, η προσομοίωση δεν είναι δυνατή" }
    };

    @Override
    public Locale getLocale(){
        return new Locale("el");
    }

    @Override
    protected Object[][] getContents() {
        return Arrays.stream(contents).peek(e -> e[0] = e[0].toString()).toArray(Object[][]::new);
    }
    @Override
    public String toString() {
        return "Ελληνικά";
    }
}
