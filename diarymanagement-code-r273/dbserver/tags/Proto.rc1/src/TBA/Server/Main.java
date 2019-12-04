package TBA.Server;

import TBA.Exceptions.*;
import TBA.Logging.TBALogger;
import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class of the DBServer
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 129  $ $Date:: 2009-09-15 #$
 */
public class Main
{
   private static int port = 8888;
   private static boolean console = false;
   private final static Logger LOGIT = Logger.getLogger(Main.class.getName());
   private NetServer netServ;
   private Thread netThread;
   private DBServer dbInstance;

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
      System.setProperty("javax.net.ssl.keyStore", "tbaKeyStore");
      System.setProperty("javax.net.ssl.keyStorePassword", "Rgr4j9");

      LOGIT.info("Starting Server");
      Main server = new Main();
      try
      {
         TBALogger.setup("TBAServer.log");
         parseArgs(args);
         server.startNet();
         LOGIT.finer("Server socket opened");

         LOGIT.info("Starting Local mode");
         server.startLocal();
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
                     LOGIT.info("Change Port to: " + arg);
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
            LOGIT.info("Console output enabled");
         }
      }

      // This is a hack. The first handler *should* be a console, at least if
      // only call this once. This code should be changed to look through the
      // handles and determine if they are a console handler, then remove it
      // when found.
      if(!console)
      {
         TBALogger.removeConsole();
      }
   }

   /**
    * Kicks of the Networking thread that deal with clients
    */
   public void startNet()
   {
      try
      {
         LOGIT.fine("Creating Database connection");
         dbInstance = new DBServer("", "");
         dbInstance.setup();

         LOGIT.fine("Starting network connection");
         netServ = new NetServer(port, dbInstance);
         netThread = new Thread(netServ);
         netThread.start();
      }
      catch (DBServerException ex)
      {
         LOGIT.severe("Unable to setup Database");
         LOGIT.info(ex.getLocalizedMessage());
         System.exit(1);
      }
      catch (NetServerException ex)
      {
         LOGIT.severe("Unable to setup Network. Continue in local mode only");
         LOGIT.info(ex.getLocalizedMessage());
      }
   }

  /**
    * Handles keyboard entry directly into the server.
    */
   public void startLocal()
   {
      boolean finished = false;
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

      String command;
      while(!finished)
      {
         try
         {
            command = stdin.readLine();
            if(command.compareToIgnoreCase("quit") == 0 || command.compareToIgnoreCase("exit") == 0)
            {
               LOGIT.info("Exiting server");
               finished = true;
               netServ.setFinished(finished);
            }
            // TODO: Add additional commands here!s
         }
         catch (IOException ex)
         {
            LOGIT.severe("Error with standard input, local mode.");
            LOGIT.info(ex.getLocalizedMessage());
         }
      }
   }
}