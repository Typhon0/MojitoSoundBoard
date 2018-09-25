package mojito_soundboard.util;

import mojito_soundboard.model.AudioClip;
import mojito_soundboard.model.SoundBoard;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteException;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private static String dbPath;

    /**
     * Initialize the database
     *
     * @param appfolder
     */
    public static void initDB(String appfolder) {
        dbPath = appfolder;
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "MEMORY");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "SoundboardDB.db", config.toProperties());
            connection.setAutoCommit(false);

            System.out.println("Opened database successfully");

            statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `Soundboard` " +
                    "(`id`	INTEGER NOT NULL," +
                    "`name`	TEXT NOT NULL," +
                    "`mixer`	TEXT, " +
                    "PRIMARY KEY(`id`));";
            statement.execute(sql);

            sql = " CREATE TABLE IF NOT EXISTS `AudioClip` (" +
                    "`id`	INTEGER NOT NULL," +
                    "`name`	TEXT," +
                    "`path`	TEXT NOT NULL," +
                    "`shortcut`	TEXT NOT NULL," +
                    "PRIMARY KEY(`id`));";
            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS `AudioClipSoundboard` (" +
                    "`idSoundboard`	INTEGER NOT NULL," +
                    "`idAudioClip`	INTEGER NOT NULL, " +
                    "FOREIGN KEY(`idSoundboard`) REFERENCES `Soundboard`(`id`), " +
                    "FOREIGN KEY(`idAudioClip`) REFERENCES `AudioClip`(`id`), " +
                    "PRIMARY KEY(`idSoundboard`,`idAudioClip`));";

            statement.execute(sql);
            connection.commit();

        } catch (SQLiteException e) {
            System.out.println(e.getMessage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * Get the database connection
     *
     * @return database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        SQLiteConfig config = new SQLiteConfig();
        config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "MEMORY");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:SoundboardDB.db", config.toProperties());
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Load all soundboard from database
     *
     * @return arraylist of soundboard
     */
    public static ArrayList<SoundBoard> loadSoundboards() {
        ArrayList<SoundBoard> soundBoards = new ArrayList<>();
        Connection c = null;

        try {
            c = getConnection();
            Statement stm = c.createStatement();
            int currentSoudnboard = 0;
            ResultSet rs = stm.executeQuery("SELECT * FROM Soundboard");

            while (rs.next()) { // Pour chaque soundboard
                SoundBoard soundBoard;
                ResultSet rsp;
                PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM AudioClipSoundboard WHERE idSoundboard = ?");
                c.commit();
                currentSoudnboard = rs.getInt(1);
                soundBoard = new SoundBoard(currentSoudnboard, rs.getString(2), rs.getString(3));

                preparedStatement.setInt(1, currentSoudnboard);
                rsp = preparedStatement.executeQuery();

                while (rsp.next()) {
                    soundBoard.getAudioClips().add(getAudioClipbyID(rsp.getInt(1)));
                    c.commit();
                }
                soundBoards.add(soundBoard);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return soundBoards;
    }

    /**
     * Get the audio clip from database by ID
     *
     * @param id of the audio clip
     * @return The audio clip
     */
    public static AudioClip getAudioClipbyID(int id) {

        Connection c = null;

        try {
            c = getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM AudioClip WHERE id = ?");
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    return new AudioClip(rs.getInt(1), rs.getString(2), new File(rs.getString(3)), rs.getString(4));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Function to add soundboard to database
     *
     * @param name of the soundboard
     * @return true if the soundboard is successfully inserted
     */
    public static boolean addSounboard(String name) {
        Connection c = null;

        try {
            c = DBHelper.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO Soundboard (id,name) VALUES (?,?)");

            statement.setNull(1, 0);
            statement.setString(2, name);
            statement.executeUpdate();
            c.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Function to add audioclip to the database
     *
     * @param currentSoundboardID current id of selected soundboard
     * @param name                name of the soundboard
     * @param file                audio file
     * @param shortcut            keyboard shortcut
     * @return true if the soundboard is successfully inserted
     */
    public static boolean addAudioClip(int currentSoundboardID, String name, File file, String shortcut) {
        Connection c = null;

        try {
            c = DBHelper.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO AudioClip (id,name,path,shortcut)" +
                            "VALUES (?,?,?,?)"
                    , Statement.RETURN_GENERATED_KEYS);

            statement.setNull(1, 0);
            statement.setString(2, name);
            statement.setString(3, file.getPath());
            statement.setString(4, shortcut);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                c.commit();
                insertAudioClipSoundboard(currentSoundboardID, generatedKeys.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return true;
    }

    /**
     * Function to link Audio Clip and soundboard in the database
     *
     * @param idSoundboard id of the soundboard to link
     * @param idAudioClip  id of the audio clip to link
     * @return true if the soundboard is successfully inserted
     */
    private static boolean insertAudioClipSoundboard(int idSoundboard, int idAudioClip) {
        Connection c = null;

        try {
            c = DBHelper.getConnection();

            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO AudioClipSoundboard (idSoundboard, idAudioClip)" +
                            "VALUES (?,?)");

            statement.setInt(1, idSoundboard + 1);
            statement.setInt(2, idAudioClip);
            statement.executeUpdate();
            c.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    /**
     * Set the path of the Database
     *
     * @param dbPath database path
     */
    public static void setDbPath(String dbPath) {
        DBHelper.dbPath = dbPath;
    }
}
