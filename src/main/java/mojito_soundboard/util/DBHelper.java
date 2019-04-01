package mojito_soundboard.util;

import javafx.scene.paint.Color;
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
                    "`shortcut`	TEXT," +
                    "`color`	TEXT," +
                    "`idSoundboard` INTEGER NOT NULL," +
                    "PRIMARY KEY(`id`));";
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
     * Load all soundboards from database
     *
     * @return arraylist of soundboard
     */
    public static ArrayList<SoundBoard> loadSoundboards() {
        ArrayList<SoundBoard> soundBoards = new ArrayList<>();
        Connection c = null;

        try {
            c = getConnection();
            Statement stm = c.createStatement();
            int currentSoudnboard = 1;
            ResultSet rs = stm.executeQuery("SELECT * FROM Soundboard");
            while (rs.next()) { // Pour chaque soundboard
                SoundBoard soundBoard;
                ResultSet rsp;
                PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM AudioClip WHERE idSoundboard = ?");
                c.commit();
                currentSoudnboard = rs.getInt(1);
                soundBoard = new SoundBoard(currentSoudnboard, rs.getString(2), rs.getString(3));

                preparedStatement.setInt(1, currentSoudnboard);
                rsp = preparedStatement.executeQuery();

                while (rsp.next()) {
                    soundBoard.getAudioClips().add(new AudioClip(rsp.getInt(1), rsp.getString(2), new File(rsp.getString(3)), rsp.getString(4), Color.valueOf(rsp.getString(5)), rsp.getInt(6)));
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
     * Method to add soundboard to database
     *
     * @param name name of the soundboard
     * @return the added soundboard
     */
    public static SoundBoard addSounboard(String name) {
        Connection c = null;
        PreparedStatement statement = null;
        SoundBoard soundBoard = null;
        try {
            c = DBHelper.getConnection();
            statement = c.prepareStatement(
                    "INSERT INTO Soundboard (id,name) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

            statement.setNull(1, 0);
            statement.setString(2, name);
            statement.executeUpdate();
            c.commit();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                c.commit();
                System.out.println(generatedKeys.getInt(1));
                soundBoard = new SoundBoard(generatedKeys.getInt(1), name);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }

        return soundBoard;
    }

    /**
     * Delete the soundboard with all audio clips
     *
     * @param soundBoard
     * @return true if deleted
     */
    public static boolean deleteSoundboard(SoundBoard soundBoard) {

        Connection c = null;
        PreparedStatement statement = null;
        try {
            c = DBHelper.getConnection();

            for (AudioClip audioClip : soundBoard.getAudioClips()) {
                removeAudioClip(audioClip);
            }

            statement = c.prepareStatement("DELETE FROM Soundboard WHERE id = ? ");
            statement.setInt(1, soundBoard.getId());
            statement.executeUpdate();
            c.commit();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }
        return true;
    }

    /**
     * Method to add audioclip to the database
     *
     * @param currentSoundboardID
     * @param audioClip
     * @return return the audio clip with the generated ID
     */
    public static AudioClip addAudioClip(int currentSoundboardID, AudioClip audioClip) {
        Connection c = null;
        PreparedStatement statement = null;
        AudioClip audioClip1 = null;
        try {
            c = DBHelper.getConnection();
            statement = c.prepareStatement(
                    "INSERT INTO AudioClip (id,name,path,shortcut,color,idSoundboard)" +
                            "VALUES (?,?,?,?,?,?)"
                    , Statement.RETURN_GENERATED_KEYS);

            statement.setNull(1, 0);
            statement.setString(2, audioClip.getName());
            statement.setString(3, audioClip.getFile().getPath());
            statement.setString(4, audioClip.getShortcut());
            statement.setString(5, audioClip.getColor().toString());
            statement.setInt(6, currentSoundboardID);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                c.commit();
                audioClip1 = new AudioClip(generatedKeys.getInt(1), audioClip.getName(), audioClip.getFile(), audioClip.getShortcut(), audioClip.getColor(), currentSoundboardID);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }
        return audioClip1;
    }

    /**
     * Method to add audioclip to the database
     *
     * @param audioClip
     * @return return the audio clip with the generated ID
     */
    public static boolean removeAudioClip(AudioClip audioClip) {
        Connection c = null;
        PreparedStatement statement = null;
        try {
            c = DBHelper.getConnection();
            statement = c.prepareStatement("DELETE FROM AudioClip WHERE id = ? AND idSoundBoard = ?");
            statement.setInt(1, audioClip.getId());
            statement.setInt(2, audioClip.getIdSoundboard());
            statement.executeUpdate();
            c.commit();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }
        return true;
    }

    /**
     * Method to add audioclip to the database
     *
     * @param audioClip
     * @return true if the audio clip is successfully inserted
     */
    public static boolean editAudioClip(AudioClip audioClip) {
        Connection c = null;
        PreparedStatement statement = null;
        try {
            c = DBHelper.getConnection();
            statement = c.prepareStatement(
                    "UPDATE AudioClip SET name = ?, path = ?, shortcut = ?, color = ?, idSoundboard = ? " +
                            "WHERE id = ?");

            statement.setString(1, audioClip.getName());
            statement.setString(2, audioClip.getFile().getPath());
            statement.setString(3, audioClip.getShortcut());
            statement.setString(4, audioClip.getColor().toString());
            statement.setInt(5, audioClip.getIdSoundboard());
            statement.setInt(6, audioClip.getId());
            statement.executeUpdate();
            c.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }


        return true;
    }

    /**
     * Method to edit a soundboard in the database
     *
     * @param soundBoard the edited soudnboard
     * @return true if the soundboard is successfully updated
     */
    public static boolean editSoundBoard(SoundBoard soundBoard) {
        Connection c = null;
        PreparedStatement statement = null;
        try {
            c = DBHelper.getConnection();
            statement = c.prepareStatement(
                    "UPDATE Soundboard SET name = ? WHERE id = ?");

            statement.setString(1, soundBoard.getName());
            statement.setInt(2, soundBoard.getId());
            statement.executeUpdate();
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            cleanConnection(c, statement);
        }
        return true;
    }

    private static void cleanConnection(Connection c, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
