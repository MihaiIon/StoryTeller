package app.storyteller.database;

/**
 * Created by Mihai on 2017-03-08.
 */
public class SQLRequests {

    /**
     *
     */
    public static final String CREATE_ALL_TABLES =
            Database.ProfilesTable.getTableCreationStatement()
            + Database.StoriesTable.getTableCreationStatement()
            + Database.FavoritesTable.getTableCreationStatement()
            + Database.CollaboratorsTable.getTableCreationStatement()
            + Database.SentencesTable.getTableCreationStatement();

    public static final String DELETE_ALL_TABLES = "";
}
