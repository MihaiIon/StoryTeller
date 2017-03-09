package app.storyteller.database;

/**
 * Created by Mihai on 2017-03-08.
 */
public class SQLRequests {

    /**
     *
     */
    public static final String CREATE_ALL_TABLES =
            Database.ProfileTable.getTableCreationStatement()+
            Database.Lala.getTableCreationStatement();

    public static final String DELETE_ALL_TABLES = "";
}
