package Client.ui.localization;

import Shared.commands.enums.CommandArgument;

public enum UIField{
    Register("register"), Authorize("authorize"), AuthTitle("authTitle"), Login("login"), Password("password"),
    TableTitle("tableTitle"), Visualization("visualization"), Add("add"), Clear("clear"), Delete("delete"),
    Logout("logout"), LoggedAs("loggedAs"), NumberFormatError("numberFormatError"), CannotBeNull("cannotBeNull"),
    DataFormatError("dataFormatError"), Save("save"), X("x"), Y("y"), Mass("mass"), VisualizationTitle("visualTitle"),
    RemoveBiggerObjects("removeBiggerObject"), RemoveByKey("removeByKey"), RemoveWithBiggerKey("removeWithBiggerKey"), UpdateById("updateById"),
    UniqueDistances("uniqueDistances"), ReplaceIfLower("replaceIfLower"), AddFilter("addFilter"), Start("start"), Stop("stop"),
    Id(CommandArgument.ID.toString()), Key(CommandArgument.KEY.toString()), Name(CommandArgument.NAME.toString()), CoordsX("coords_x"), CoordsY("coords_y"), StartX("start_x"), StartY("start_y"),
    StartZ("start_z"), StartName("start_name"), FinishX("finish_x"), FinishY("finish_y"), FinishZ("finish_z"), FinishName("finish_name"),
    CreationDate("creation_date"), Distance("distance"), User("user"), Radius("radius"), Execute("execute"), FileName(CommandArgument.FILE_NAME.toString()),
    ExecuteFile("execute_file"), WriteFilterValue("write_filter_value"), ServerUnavailable("server_unavailable"),;
    private final String name;
    UIField(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
