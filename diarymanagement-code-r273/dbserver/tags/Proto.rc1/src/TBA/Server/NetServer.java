package TBA.Server;

import TBA.Data.Diary;
import TBA.Data.User;
import TBA.Exceptions.*;
import TBA.Messages.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.util.*;
import java.util.logging.Logger;
import java.io.*;
import java.net.SocketTimeoutException;


/**
 * This class runs the network section of the server
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
public class NetServer implements Runnable
{
   Vector<Thread> threads = new Vector<Thread>();
   Vector<HandleThread> handles = new Vector<HandleThread>();
   private SSLServerSocket serv;
   private DBServer dbInstance;
   private volatile boolean finished = false;
   private final static Logger LOGIT = Logger.getLogger(Main.class.getName());


   /**
    * This is the constructor, starts the DBServer and initialises the
    * Network Server section
    *<p>
    * @throws HandleThreadException
    *<p>
    * @param port The port number to open the server on
    *<p>
    * @see Socket
    * @see DBServer
    */
   NetServer(int port, DBServer db) throws NetServerException
   {
      dbInstance = db;
      try
      {
         SSLServerSocketFactory sslSSockFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
         serv = (SSLServerSocket)sslSSockFactory.createServerSocket(port);
      }
      catch (Exception ex)
      {
         LOGIT.severe("Unable to open the ssl server socket");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * Run the thread. This will communicate with the client and service its
    * requests
    *<p>
    */
   public void run()
   {
      waitForClient();
   }

   /**
    * Wait for a client to connect, then send them to be handled by another thread
    *<p>
    * @see HandleThread
    * @see Thread
    * @see Socket
    */
   private void waitForClient()
   {
      LOGIT.info("Waiting for client's requests");
      while (!finished)
      {
         try
         {
            serv.setSoTimeout(5000);
            SSLSocket s = (SSLSocket)serv.accept();
            HandleThread ht = new HandleThread(s, dbInstance);
            handles.add(ht);
            Thread t = new Thread(ht);

            LOGIT.finest("Start new thread to handle client");
            t.start();
            threads.add(t);
         }
         catch (SocketTimeoutException ex) {}
         catch (IOException ex)
         {
            LOGIT.severe("Unable to accept connections on the server socket");
            LOGIT.info(ex.getLocalizedMessage());
         }
         catch (HandleThreadException ex)
         {
            LOGIT.severe("Unable start new thread for client");
            LOGIT.info(ex.getLocalizedMessage());
         }
      }
   }

   public void setFinished(boolean finished)
   {
      this.finished = finished;
      if(finished)
      {
         for(HandleThread ht : handles)
         {
            ht.setFinished(finished);
         }
      }
      
      if (Thread.currentThread() != null)
      {
         Thread.currentThread().interrupt();
      }
   }
}
/**
 * This class is a that handles 1 client per instance and runs as a thread
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
class HandleThread implements Runnable
{
   private SSLSocket con;
   private DataInputStream in;
   private DataOutputStream out;
   private static DBServer dbInstance = null;
   private volatile boolean finished = false;
   private final static Logger LOGIT = Logger.getLogger(Main.class.getName());
   private final static String MESSAGENOTUNDERSTOOD = "The Server does not understand your CLIENT. Please confirm with your System Administrator that you are using the correct version.";

   /**
    * This is the constructor, which accepts a socket to communicate with.
    *<p>
    * If the database has not been instantiated yet, it will do so
    *<p>
    * @throws HandleThreadException
    *<p>
    * @param s The socket from the client
    *<p>
    * @see Socket
    * @see DBServer
    */
   public HandleThread(SSLSocket s, DBServer db) throws HandleThreadException
   {
      con = s;
      dbInstance = db;
   }

   /**
    * Run the thread. This will communicate with the client and service its
    * requests
    *<p>
    */
   public void run()
   {
      try
      {
         in = new DataInputStream(con.getInputStream());
         out = new DataOutputStream(con.getOutputStream());
      }
      catch(Exception ex)
      {
         LOGIT.severe("No input/output stream can be created");
         LOGIT.info(ex.getLocalizedMessage());
         return; // Kill this thread.
      }

      try
      {
         // Really unhappy with this bit. Not sure how to refactor Messages.*
         // to fix this however
         while(!finished)
         {
            Messages header = new Messages();

            String headerData = getHeaderData();
            header.Set(headerData);

            String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

            HandleCommands(headerData + bodyData);
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.severe("Thread ending because of network data read/write issues");
         LOGIT.info(ex.getLocalizedMessage());
         return; // Kill this thread
      }
      catch (MessagesException ex)
      {
         LOGIT.severe("Unable to parse message");
         LOGIT.fine(ex.getLocalizedMessage());
         try
         {
            ResponseMessage response = new ResponseMessage();
            response.SetErrorCode(15);
            response.SetErrorMessage(MESSAGENOTUNDERSTOOD);
            sendData(response.Get(""));
         }
         catch (Exception ex1)
         {
            LOGIT.fine(ex1.getLocalizedMessage());
            LOGIT.severe("Unable to send error response");
         }
      }

      try
      {
         out.close();
         in.close();
         con.close();
      }
      catch (IOException ex)
      {
         LOGIT.severe("Unable to close input/output streams");
         LOGIT.info(ex.getLocalizedMessage());
         return;
      }
   }

   /**
    * Deal with requests sent across from the client
    *<p>
    * @throws NetworkDataException, MessagesException
    */
   private void HandleCommands(String queryData) throws NetworkDataException, MessagesException
   {
      QueryMessage query = new QueryMessage();

      query.Set(queryData);

      if(query.GetQueryType().equals("LOGIN"))
      {
         HandleLoginCommand(queryData);
      }
      else if(query.GetQueryType().equals("LOGOUT"))
      {
         HandleLogoutCommand(queryData);
      }
   }

   /**
    * Specifically deals with the LOGIN request sent across from the client
    *<p>
    * @throws NetworkDataException, MessagesException
    */
   private void HandleLoginCommand(String queryData) throws MessagesException, NetworkDataException
   {
      int errNum = 0;
      String errMsg = "";
      LoginQuery login = new LoginQuery();
      User userData;

      try
      {
         // Handle the actual LOGIN query that was sent
         // Pass off to the DB to check the user, which will log them in
         // if they authenticated correctly
         login.Set(queryData);
         String username = login.GetUserName();
         String pwdHash = login.GetPasswordHash();
         userData = dbInstance.CheckUser(username, pwdHash);

         if (userData != null)
         {
            // User successfully logged in, lets prepare our response!
            Vector<Diary> diaries = new Vector<Diary>();
            Vector<DiaryHeader> diaryHeaders = new Vector<DiaryHeader>();
            DiaryHeader aDiaryHeader;
            LoginResponse loginResp = new LoginResponse();

            // Set the user information
            loginResp.SetDisplayName(userData.getDisplayName());
            loginResp.SetSessionID(userData.getSessionID());
            loginResp.SetDefaultDiary(userData.getDefaultDiaryID());

            // Get the diary header information from the db
            diaries = dbInstance.GetDiaries(username);
            for(Diary aDiary : diaries)
            {
               aDiaryHeader = new DiaryHeader();
               aDiaryHeader.SetDiaryID(aDiary.getID());
               aDiaryHeader.SetDiaryName(aDiary.getName());
               aDiaryHeader.SetPermissions(aDiary.getPermissions());
               if(aDiary.getOwnerName().compareTo(username) == 0)
               {
                  aDiaryHeader.SetOwnerFlag('Y');
               }
               else
               {
                  aDiaryHeader.SetOwnerFlag('N');
               }
               aDiaryHeader.SetRevNum(aDiary.getRevision());

               diaryHeaders.add(aDiaryHeader);
            }

            // Set the diary headers in the response
            loginResp.SetDiaryHeaders(diaryHeaders);

            // Tada!
            LOGIT.finest("Login successful, sending response");
            sendData(loginResp.Get(""));
            loginResp.Set(loginResp.Get(""));
         }
         else
         {
            errNum = 1;
            errMsg = "Incorrect username or password";
         }
      }
      catch (DBServerException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Error in checking user");
         errMsg = "Server error when attempting to log in";
         errNum = 15;
      }
      finally
      {
         if(errNum != 0)
         {
            ResponseMessage error = new ResponseMessage();

            error.SetErrorCode(errNum);
            error.SetErrorMessage(errMsg);
            error.SetSessionID(login.GetSessionID());

            sendData(error.Get(""));
         }
      }

      return;
   }

   /**
    * Specifically deals with the LOGOUT request sent across from the client
    *<p>
    * @throws NetworkDataException, MessagesException
    */
   public void HandleLogoutCommand(String queryData)
   {
      int errNum = 0;
      String errMsg = "";
      LogoutQuery logout = new LogoutQuery();

      try
      {
         logout.Set(queryData);
         String sessionID = logout.GetSessionID();
         dbInstance.Logout(sessionID);
      }
      catch(MessagesException ex)
      { /* We don't care about this really*/ }
      catch(DBServerException ex)
      { /* We don't care about this really*/ }
      finally
      {
         try
         {
            LogoutResponse lR = new LogoutResponse();
            lR.SetErrorCode(0);
            sendData(lR.Get(""));
         }
         catch (NetworkDataException ex)
         {
            LOGIT.severe("Network issues while attempting to send logout response");
            LOGIT.info(ex.getLocalizedMessage());
         }
         catch (MessagesException ex)
         {
            LOGIT.severe("Errors with logout message, response not sent");
            LOGIT.info(ex.getLocalizedMessage());
         }

      }
   }

   /**
    * Send data to the client
    *<p>
    * @param s The data to send to the client
    *<p>
    * @throws NetworkDataException
    */
   public void sendData(String s) throws NetworkDataException
   {
      try
      {
         out.writeBytes(s);
      }
      catch(IOException ex)
      {
         LOGIT.severe("Unable to send data to client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }
   }

/**
    * Receive the header data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   public String getHeaderData() throws NetworkDataException
   {
      byte[] s=new byte[Messages.messageHeaderLen];

      try
      {
         // Confirming size of message is correct is
         // handled later by the Messages.* classes themselves
         in.readFully(s, 0, Messages.messageHeaderLen);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, Messages.messageHeaderLen);
   }

   /**
    * Receive data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   public String getData(int size) throws NetworkDataException
   {
      byte[] s=new byte[size];
      try
      {
         // Confirming size of messages is correct is
         // handled later by the Messages.* classes themselves
         in.readFully(s, 0, size);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, size);
   }

   /**
    * Prepare the thread to finish
    */
   public void setFinished(boolean finished)
   {
      this.finished = finished;

      if (Thread.currentThread() != null)
      {
         Thread.currentThread().interrupt();
      }
   }
}