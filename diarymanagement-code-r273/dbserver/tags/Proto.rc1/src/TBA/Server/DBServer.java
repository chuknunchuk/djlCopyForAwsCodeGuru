package TBA.Server;

import TBA.Data.Diary;
import TBA.Exceptions.DBServerException;
import TBA.Exceptions.PasswordHashingException;
import java.sql.*;
import TBA.Security.PasswordHashing;
import TBA.Data.User;
import java.util.UUID;
import java.util.Vector;

/**
 * This abstract class handles the connection to SQLite
 *<p>
 * If you wish to talk to SQLite, use this class.
 * It is only thread-safe if you instantiation it as a static class
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 129           $ $Date:: 2009-09-15 #$
 *
 * @see dbserver.SQLite
 */
class DBServer extends SQLite
{
   PreparedStatement prepCheckUser;
   PreparedStatement prepSaltPassword;
   PreparedStatement prepSaveSession;
   PreparedStatement prepNewSession;
   PreparedStatement prepGetSession;
   PreparedStatement prepRemoveSession;
   PreparedStatement prepGetUserDiaries;
   PreparedStatement prepGetDiary;

   // Block the default constructor
   private DBServer() {}

   /**
    * This is the constructor for DBServer.
    *<p>
    * @throws DBServerException
    *<p>
    * @param user The username to log into the database as
    * @param pass The password to log into the database with
    */
   DBServer(String user, String pass) throws DBServerException
   {
      super(user, pass);
   }

   /**
    * This sets up all the prepared statements that will be possibly used by
    * DBServer
    *<p>
    * @throws DBServerException
    *<p>
    * @see java.sql.PreparedStatement
    */
   public void setup() throws DBServerException
   {
      // Setup prepared statements here
      LOGIT.fine("Setting up prepared statements");
      try
      {
         String sql = null;
         
         ///* Dummy tables + data!
         executeUpdate("drop table users");
         executeUpdate("drop table passwordSalts");
         executeUpdate("drop table userSessions");
         executeUpdate("drop table diaries");
         executeUpdate("drop table diary");
         executeUpdate("Create table users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH);");
         executeUpdate("Create table passwordSalts(USER, SALT);");
         executeUpdate("Create table userSessions(USER, SESSIONID);");
         executeUpdate("Create table diaries(ID, USER, DIARYID, PERMISSIONS);");
         executeUpdate("Create table diary(ID, NAME, OWNER, REVISION);");

         executeUpdate("insert INTO users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH) VALUES('dmcgra', 'Dan McG-Unit', 0, 'dd54024027f265410d2bc7a43e9eeff7ff1a4378c86e4f07d24b199926a5bbad');");
         executeUpdate("insert INTO passwordSalts(USER, SALT) values('dmcgra', 'pepper')");
         executeUpdate("insert INTO diary(ID, NAME, OWNER, REVISION) values(0, 'Dummy Diary','dmcgra', 15)");
         executeUpdate("insert INTO diary(ID, NAME, OWNER, REVISION) values(1, 'Dummy Diary 2','john', 35)");
         executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(0, 'dmcgra',0, 511)");
         executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(1, 'dmcgra',1, 511)");
         //*/


         sql = "select DISPLAYNAME, DEFAULTDIARYID from users where USER = ? and PWDHASH = ?;";
         prepCheckUser = connex.prepareStatement(sql);
         LOGIT.finest("Setup user PreparedStatement");

         sql = "select SALT from passwordSalts where USER = ?;";
         prepSaltPassword = connex.prepareStatement(sql);
         LOGIT.finest("Setup password PreparedStatement");

         sql = "select SESSIONID from userSessions where USER = ?;";
         prepGetSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup get session PreparedStatement");

         sql = "insert INTO userSessions(SESSIONID, USER) values (?, ?);";
         prepNewSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup new session PreparedStatement");

         sql = "update userSessions SET SESSIONID=? WHERE USER=?;";
         prepSaveSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup save session PreparedStatement");

         sql = "update userSessions SET SESSIONID='' WHERE SESSIONID=?;";
         prepRemoveSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup remove session PreparedStatement");

         sql = "select DIARYID, PERMISSIONS from diaries where USER = ?;";
         prepGetUserDiaries = connex.prepareStatement(sql);
         LOGIT.finest("Setup get user's diaries PreparedStatement");

         sql = "select NAME, OWNER, REVISION from diary where ID = ?;";
         prepGetDiary = connex.prepareStatement(sql);
         LOGIT.finest("Setup get diary PreparedStatement");
      }
      catch (Exception ex)
      {
         LOGIT.severe("Unable to create an SQL prepared statement");
         LOGIT.info(ex.getLocalizedMessage());
         throw new DBServerException(ex);
      }
   }

   /**
    * This checks a user's credentials and returns a User object which contains
    * the details of the said user.
    *<p>
    * @param username The user name
    * @param pwdhash The password hash sent from the client
    *<p>
    * @throws DBServerException
    *<p>
    * @return User object if the credentials match, null if they don't
    */
   public User CheckUser(String username, String pwdhash) throws DBServerException
   {
      User userData = new User();

      try
      {
         ResultSet res;
         String salt;

         // Get details needed for password hash matching
         prepSaltPassword.setString(1, username);
         res = executeQuery(prepSaltPassword);
         if (res.next())
         {
            salt = res.getString("SALT");
         }
         else
         {
            return null;
         }

         // Convert password hash from user to password hash stored on server.
         PasswordHashing pHash = new PasswordHashing();
         pwdhash = pHash.getServerHash(pwdhash, salt);

         // Retrieve the user, if the password hash matches
         prepCheckUser.setString(1, username);
         prepCheckUser.setString(2, pwdhash);
         res = executeQuery(prepCheckUser);

         if (res.next())
         {
            userData.setDisplayName(res.getString("DISPLAYNAME"));
            userData.setDefaultDiaryID(res.getInt("DEFAULTDIARYID"));
         }
         else
         {
            return null;
         }

         // Update or setup a session for the user
         PreparedStatement giveSession;
         prepGetSession.setString(1, username);
         res = executeQuery(prepGetSession);

         if (res.next())
         {
            giveSession = prepSaveSession;
         }
         else
         {
            giveSession = prepNewSession;
         }
         
         String sessionID = UUID.randomUUID().toString();
         LOGIT.severe(sessionID); // debug testing
         LOGIT.severe(Integer.toString(sessionID.length())); // debug testing
         
         giveSession.setString(1, sessionID);
         giveSession.setString(2, username);
         if(executeUpdate(prepGetSession) != -1)
         {
            throw new SQLException("Give the user a session failed!");
         }
         
         userData.setSessionID(sessionID);

         return userData;
      }
      catch (SQLException ex)
      {
         LOGIT.severe("Unable to set values in an SQL prepared statement");
         LOGIT.info(ex.getLocalizedMessage());
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (NullPointerException ex)
      {
         LOGIT.severe("DB Instance not active...");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (PasswordHashingException ex)
      {
         LOGIT.severe("Password hashing services unavailable");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * This gets all the diary information for a particular user, excluding the
    * actual diary entries.
    *<p>
    * @param username The user name
    *<p>
    * @throws DBServerException
    *<p>
    * @return User object if the credentials match, null if they don't
    */
   public Vector<Diary> GetDiaries(String username) throws DBServerException
   {
      Vector<Diary> diaries = new Vector<Diary>();
      Diary diaryData;

      try
      {
         ResultSet res;
         int diaryID = -1;

         // Get all the diary ID's for this user
         prepGetUserDiaries.setString(1, username);
         res = executeQuery(prepGetUserDiaries);
         while (res.next())
         {
            diaryID = res.getInt("DIARYID");

            diaryData = new Diary();
            diaryData.setID(diaryID);
            diaryData.setPermissions(res.getInt("PERMISSIONS"));

            diaries.add(diaryData);
         }
         if(diaryID == -1)
         {
            return null;
         }

         // Get the details (excluding entries) of all the diaries.
         for(Diary aDiary: diaries)
         {
            diaryID = aDiary.getID();

            prepGetDiary.setInt(1, diaryID);
            res = executeQuery(prepGetDiary);
            if(res.next())
            {
               aDiary.setName(res.getString("NAME"));
               aDiary.setOwnerName(res.getString("OWNER"));
               aDiary.setRevision(res.getInt("REVISION"));
            }
         }

         return diaries;
      }
      catch (SQLException ex)
      {
         LOGIT.severe("Unable to set values in an SQL prepared statement");
         LOGIT.info(ex.getLocalizedMessage());
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (NullPointerException ex)
      {
         LOGIT.severe("DB Instance not active...");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * This logs out a session
    *<p>
    * @param sessionID The sessionID to sign out
    *<p>
    * @throws DBServerException
    *<p>
    */
   public void Logout(String sessionID) throws DBServerException
   {
      try
      {
         prepGetUserDiaries.setString(1, sessionID);
         executeUpdate(prepGetUserDiaries);
      } catch (SQLException ex)
      {
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }
}