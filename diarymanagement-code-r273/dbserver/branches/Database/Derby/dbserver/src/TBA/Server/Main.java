package TBA.Server;

import TBA.Data.Diary;
import TBA.Data.User;
import TBA.Exceptions.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import TBA.Logging.TBALogger;
import TBA.Messages.*;
import TBA.Security.PasswordHashing;

/**
 * This is the main class of the DBServer
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 11   $ $Date:: 2009-07-21 #$
 */
public class Main
{
   private static int port = 8888;
   private static boolean console = false;
   private ServerSocket serv;
   private final static Logger LOGIT = Logger.getLogger(Main.class.getName());


   public void Main()
   {

   }

   /**
    * Entry point
    *<p>
    * @param args Command line arguments. Currently it supports a handful of
    * switches.
    * <p>
    * Firstly, '-p x' or '--port x', where x is the port number you
    * wish the server to listen on.
    * <p>
    * Next is '-l level' or '--log-level level'
    * where 'level' is one of SEVERE, WARNING, INFO, FINE, FINER or FINEST
    * <p>
    * Lastly is '-c' or '--console' which enables logging to the console
    */
   public static void main(String[] args) throws PasswordHashingException
   {
      PasswordHashing ph = new PasswordHashing();
      LOGIT.severe(ph.getClientHash("password"));
      LOGIT.severe(ph.getServerHash(ph.getClientHash("password"), "pepper"));
      LOGIT.info("Starting Server");
      Main server = new Main();
      try
      {
         TBALogger.setup("TBAServer.log");
         parseArgs(args);
         server.createConnection();
         LOGIT.finer("Server socket opened");

         server.waitForClient();
		}
      catch (IOException ex)
      {
         // Don't let logging stop our server...
         // Just log it to console if possible.
			LOGIT.warning("Error: Could not create the log file");
         LOGIT.info(ex.getLocalizedMessage());
		}
   }

   /**
    * Parse the command line arguments
    *<p>
    * @param args Command line arguments. Currently it supports a handful of
    * switches.
    * <p>
    * Firstly, '-p x' or '--port x', where x is the port number you
    * wish the server to listen on.
    * <p>
    * Next is '-l level' or '--log-level level'
    * where 'level' is one of SEVERE, WARNING, INFO, FINE, FINER or FINEST
    * <p>
    * Lastly is '-c' or '--console' which enables logging to the console
    */
   private static void parseArgs(String args[])
   {
      String arg = null;
      for (int argnum = 0; argnum < args.length; argnum++)
      {
         arg = args[argnum];
         if (arg.compareTo("-l") == 0 || arg.compareTo("--log-level") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  Level loglevel = Level.parse(arg);
                  TBALogger.setLevel(loglevel);
                  LOGIT.setLevel(loglevel);

                  // Setup this classes logger to write to the console
                  for(Handler logHandler : LOGIT.getHandlers())
                  {
                     logHandler.setLevel(loglevel);
                  }

                  LOGIT.info("Change Log level to: " + arg);
               }
               catch (IllegalArgumentException ex)
               {
                  LOGIT.warning("Invalid Log Level set via parameter: " + arg);
               }
               catch (IOException ex)
               {
                  LOGIT.warning("Error setting log level");
                  LOGIT.info(ex.getLocalizedMessage());
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Log Level argument");
            }
         }
         else if (arg.compareTo("-p") == 0 || arg.compareTo("--port") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  int portnum = Integer.parseInt(arg);
                  if(portnum > 1024 && portnum < 65536)
                  {
                     port = portnum;
                  }
                  else
                  {
                     LOGIT.warning("Out of range Port number set via parameter: " + arg);
                  }
               }
               catch (NumberFormatException ex)
               {
                  LOGIT.warning("Invalid Port number set via parameter: " + arg);
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Port Number argument");
            }
         }
         else if (arg.compareTo("-c") == 0 || arg.compareTo("--console") == 0)
         {
            console = true;
         }
      }

      // This is a hack. The first handler *should* be a console, at least if
      // only call this once. This code should be changed to look through the
      // handles and determine if they are a console handler, then remove it
      // when found.
      if(!console)
      {
         TBALogger.removeConsole();
         LOGIT.removeHandler(LOGIT.getHandlers()[0]);
      }
   }

   /**
    * Wait for a client to connect, then send them to be handled by a thread
    *<p>
    * @see HandleThread
    * @see Thread
    * @see Socket
    */
   public void waitForClient()
   {
      Vector<Thread> threads = new Vector<Thread>();
      while (true)
      {
         LOGIT.info("Waiting for client's requests");
         try
         {
            Socket s = serv.accept();
            HandleThread ht = new HandleThread(s);
            Thread t = new Thread(ht);

            LOGIT.finer("Start new thread to handle client");
            t.start();
            threads.add(t);
         }
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

   /**
    * Opens the server socket for listening
    *<p>
    * @see ServerSocket
    */
   public void createConnection()
   {
      try
      {
         serv = new ServerSocket(port);
      }
      catch (Exception ex)
      {
         LOGIT.severe("Unable to open the server socket");
         LOGIT.info(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
}

/**
 * This class is a that handles 1 client per instance and runs as a thread
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 11   $ $Date:: 2009-07-21 #$
 */
class HandleThread implements Runnable
{
   private Socket con;
   private DataInputStream in;
   private DataOutputStream out;
   private static DBServer dbInstance = null;
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
   public HandleThread(Socket s) throws HandleThreadException
   {
      con = s;
      if(dbInstance == null)
      {
         LOGIT.fine("Creating Database connection");
         try
         {
            dbInstance = new DBServer("", "");
            dbInstance.setup();
         }
         catch (DBServerException ex)
         {
            LOGIT.severe("Unable to setup Database");
            LOGIT.info(ex.getLocalizedMessage());
            throw new HandleThreadException(ex);
         }
      }
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
         Messages header = new Messages();

         String headerData = getHeaderData();
         header.Set(headerData);

         String bodyData = getData(header.GetMessageLen());

         HandleCommands(headerData + bodyData);
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
   public void HandleCommands(String queryData) throws NetworkDataException, MessagesException
   {
      QueryMessage query = new QueryMessage();

      query.Set(queryData);

      if(query.GetQueryType().equals("LOGIN"))
      {
         HandleLoginCommand(queryData);
      }
      else if(query.GetQueryType().equals("LOGOUT"))
      {
         LogoutQuery logout = new LogoutQuery();

         logout.Set(queryData);

         HandleLogoutCommand(logout);
      }
   }

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
            sendData(loginResp.Get(""));
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

   public void HandleLogoutCommand(LogoutQuery logout)
   {

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
      int cnt = 0;

      try
      {
         // Confirming size of message is correct is
         // handled later by the Messages.* classes themselves
         cnt = in.read(s, 0, Messages.messageHeaderLen);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, cnt);
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
      int cnt = 0;
      try
      {
         // Confirming size of messages is correct is
         // handled later by the Messages.* classes themselves
         cnt = in.read(s, 0, size);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, cnt);
   }
}