package Client.ui.localization;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Locale;

import Shared.commands.enums.CommandMessage;

public class loc_ru extends ListResourceBundle {
    private static final Object[][] contents = new Object[][] {
            // AuthPanel
            { UIField.AuthTitle, "Авторизуйтесь или зарегистрируйтесь"}, { UIField.Login, "Логин"}, { UIField.Password, "Пароль"},
            { UIField.Register, "Зарегистрироваться"}, { UIField.Authorize, "Авторизоваться"}, { CommandMessage.AuthorizationSuccess, "Авторизация успешна"},
            { CommandMessage.RegistrationSuccess, "Регистрация успешна"}, { CommandMessage.UserAlreadyExists, "Такой пользователь уже существует"},
            { CommandMessage.UserNotExists, "Такого пользователя не существует"}, { CommandMessage.UserNotAuthorized, "Пользователь не авторизован"},
            { CommandMessage.IncorrectPassword, "Неверный пароль"},
            // User Settings
            {UIField.LoggedAs, "Вы авторизованы как"}, {UIField.Logout, "Выйти из аккаунта"},
            // TablePanel
            { UIField.TableTitle, "Таблица объектов"}, { UIField.Visualization, "визуализация"}, { UIField.DataFormatError, "Дата в неверном формате"},
            { UIField.RemoveBiggerObjects, "Удалить\n объекты больше\n введённого"}, {UIField.RemoveByKey, "Удалить\n по ключу"},
            { UIField.RemoveWithBiggerKey, "Удалить с\n ключом больше\n заданного"}, { UIField.ReplaceIfLower, "Заменить, если\n новый объект\n меньше"},
            { UIField.AddFilter, "Добавить фильтр"}, { UIField.UniqueDistances, "Только\n уникальные\n расстояния"}, { UIField.Add, "Добавить"},
            { UIField.Clear, "Очистить"}, { UIField.Delete, "Удалить"}, { UIField.UpdateById, "Заменить\n по id"}, { UIField.Execute, "Выполнить"},
            { UIField.ExecuteFile, "Выполнить\n скрипт"}, { UIField.WriteFilterValue, "Введите значение фильтра"},
            // VisualisationPanel
            { UIField.Mass, "масса"}, { UIField.Save, "Сохранить"}, { UIField.VisualizationTitle, "Визуализация объектов"},{ UIField.Radius, "радиус"},
            { UIField.X, "х"}, { UIField.Y, "у"}, { UIField.Stop, "Остановить"}, {UIField.Start, "Запустить"},
            // ObjectFields
            { UIField.Id, "ид"}, { UIField.Key, "ключ"}, { UIField.Name, "имя"},  { UIField.CoordsX, "х"},
            { UIField.CoordsY, "у"}, { UIField.StartX, "старт_х"}, { UIField.StartY, "старт_у"}, { UIField.StartZ, "старт_z"},
            { UIField.StartName, "старт_имя"}, { UIField.FinishX, "финиш_х"}, { UIField.FinishY, "финиш_у"}, { UIField.FinishZ, "финиш_z"},
            { UIField.FinishName, "финиш_имя"}, { UIField.Distance, "расстояние"}, { UIField.CreationDate, "дата_создания"},
            { UIField.User, "пользователь"},
            // Other
            { UIField.NumberFormatError, "Это не похоже на число типа"},
            { UIField.CannotBeNull, "Данное поле не может быть пустым либо null"},
            { UIField.FileName, "Название файла"},
            { CommandMessage.UnexpectedError, "Непредвиденная ошибка"},
            { CommandMessage.InvalidArgument, "Введён неккоректный %s"},
            { CommandMessage.InvalidArgumentCount, "Неккоректное количество аргументов"},
            { CommandMessage.Success, "Успешно выполнено"},
            { CommandMessage.ServerNotResponded, "Сервер не ответил в течении ожидаемого времени"},
            { CommandMessage.ArgumentNotPresented, "Вы не ввели %s"},
            { CommandMessage.RecursionRestricted, "Рекурсия запрещена"},
            { UIField.ServerUnavailable, "Похоже, что сервер недоступен, симуляция невозможна"}
    };

    @Override
    public Locale getLocale(){
        return new Locale("ru");
    }

    @Override
    protected Object[][] getContents() {
        return Arrays.stream(contents).peek(e -> e[0] = e[0].toString()).toArray(Object[][]::new);
    }

    @Override
    public String toString() {
        return "Русский";
    }
}
