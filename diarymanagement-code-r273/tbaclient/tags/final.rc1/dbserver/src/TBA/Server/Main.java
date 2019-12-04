package TBA.Server;

import TBA.Exceptions.*;
import TBA.Logging.TBALogger;
import TBA.Security.PasswordHashing;
import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class of the DBServer
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 233  $ $Date:: 2009-10-22 #$
 */
public class Main {

    private static int port = 9999;
    private static boolean console = false;
    private final static Logger LOGIT = Logger.getLogger(Main.class.getName());
    private NetServer netServ;
    private Thread netThread;
    private DBServer dbInstance;
    private PasswordHashing PassHash;

    //  public void Main()
    //  {
//   }
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
    public static void main(String[] args) throws PasswordHashingException, DBServerException {
        System.setProperty("javax.net.ssl.keyStore", "tbaKeyStore");
        System.setProperty("javax.net.ssl.keyStorePassword", "Rgr4j9");

        LOGIT.info("Starting Server");
        Main server = new Main();
        try {
            TBALogger.setup("TBAServer.log");
            parseArgs(args);
            server.startNet();
            LOGIT.finer("Server socket opened");
            LOGIT.info("Starting Local mode");
            server.startLocal();
        } catch (IOException ex) {
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
    private static void parseArgs(String args[]) {
        String arg = null;
        for (int argnum = 0; argnum < args.length; argnum++) {
            arg = args[argnum];
            if (arg.compareTo("-l") == 0 || arg.compareTo("--log-level") == 0) {
                argnum++;
                if (argnum < args.length) {
                    arg = args[argnum];
                    try {
                        Level loglevel = Level.parse(arg);
                        TBALogger.setLevel(loglevel);
                        LOGIT.setLevel(loglevel);

                        // Setup this classes logger to write to the console
                        for (Handler logHandler : LOGIT.getHandlers()) {
                            logHandler.setLevel(loglevel);
                        }

                        LOGIT.info("Change Log level to: " + arg);
                    } catch (IllegalArgumentException ex) {
                        LOGIT.warning("Invalid Log Level set via parameter: " + arg);
                    } catch (IOException ex) {
                        LOGIT.warning("Error setting log level");
                        LOGIT.info(ex.getLocalizedMessage());
                    }
                } else {
                    LOGIT.warning("Missing detail for Log Level argument");
                }
            } else if (arg.compareTo("-p") == 0 || arg.compareTo("--port") == 0) {
                argnum++;
                if (argnum < args.length) {
                    arg = args[argnum];
                    try {
                        int portnum = Integer.parseInt(arg);
                        if (portnum > 1024 && portnum < 65536) {
                            port = portnum;
                            LOGIT.info("Change Port to: " + arg);
                        } else {
                            LOGIT.warning("Out of range Port number set via parameter: " + arg);
                        }
                    } catch (NumberFormatException ex) {
                        LOGIT.warning("Invalid Port number set via parameter: " + arg);
                    }
                } else {
                    LOGIT.warning("Missing detail for Port Number argument");
                }
            } else if (arg.compareTo("-c") == 0 || arg.compareTo("--console") == 0) {
                console = true;
                LOGIT.info("Console output enabled");
            }
        }

        // This is a hack. The first handler *should* be a console, at least if
        // only call this once. This code should be changed to look through the
        // handles and determine if they are a console handler, then remove it
        // when found.
        if (!console) {
            TBALogger.removeConsole();
        }
    }

    /**
     * Kicks of the Networking thread that deal with clients
     */
    public void startNet() {
        try {
            LOGIT.fine("Creating Database connection");
            dbInstance = new DBServer("", "");
            dbInstance.setup();

            LOGIT.fine("Starting network connection");
            netServ = new NetServer(port, dbInstance);
            netThread = new Thread(netServ);
            netThread.start();
        } catch (DBServerException ex) {
            LOGIT.severe("Unable to setup Database");
            LOGIT.info(ex.getLocalizedMessage());
            System.exit(1);
        } catch (NetServerException ex) {
            LOGIT.severe("Unable to setup Network. Continue in local mode only");
            LOGIT.info(ex.getLocalizedMessage());
        }
    }

    /**
     * Handles keyboard entry directly into the server.
     */
    public void startLocal() throws DBServerException, PasswordHashingException {
        boolean finished = false;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
 
        char creationchar = ' ';
        try{
                do {
                    System.out.println("Do you wish to recreate the Database? (Y/N)");
                    System.out.print("$");

                creationchar = stdin.readLine().charAt(0);

                    creationchar = Character.toUpperCase(creationchar);
                    if (creationchar == 'Y') {
                        System.out.println("Database is being constructed.\nPlease Be Patient");
                        dbInstance.RealDatabase();
                        System.out.println("Database Created Successully.");
                    } else if (creationchar == 'N') {
                        System.out.println("Continuing to use existing database.");
                    }
                    //*/
                    // tmpDummyDatabase();
                    //*/
                } while (creationchar != 'Y' && creationchar != 'N');
        } catch (IOException ex) {
                LOGIT.severe("Error with standard input, local mode.");
                LOGIT.info(ex.getLocalizedMessage());
            }
        
        System.out.println("\n : TBA Server Cmd :\n");
        System.out.println("Type \"help\" for list of commands.");

        String commandline;
        String[] command = new String[5];

        while (!finished) {
            try {
                //clear the array
                for (int i = 0; i < command.length; i++) {
                    command[i] = "";
                }

                System.out.print("$");
                commandline = stdin.readLine();
                command = commandline.split(" ");
                if (command[0] != null && (command[0].length() > 3)) {
                    commandline = command[0];
                    if (command[0].compareToIgnoreCase("quit") == 0 || command[0].compareToIgnoreCase("exit") == 0) {
                        LOGIT.info("Exiting server");
                        finished = true;
                        netServ.setFinished(finished);
                    } else if (command[0].compareToIgnoreCase("addusr") == 0) {
                        String usr, pws, DisplayName, DairyName;
                        System.out.println("Insert username:");
                        usr = stdin.readLine();
                        System.out.println("Insert password:");
                        pws = stdin.readLine();
                        System.out.println("Insert DisplayName:");
                        DisplayName = stdin.readLine();
                        System.out.println("Insert DiaryName:");
                        DairyName = stdin.readLine();
                        System.out.println(dbInstance.Addusr(usr, pws, DisplayName, DairyName));
                    } else if (command[0].compareToIgnoreCase("adddry") == 0) {
                        System.out.println("Insert User Name:");
                        String usr = stdin.readLine();
                        System.out.println("Insert Diary Name:");
                        String DairyName = stdin.readLine();
                        System.out.println(dbInstance.Adddry(usr, DairyName));
                    } else if (command[0].compareToIgnoreCase("addper") == 0) {
                        System.out.println("Insert User Name:");
                        String usr = stdin.readLine();
                        System.out.println("Insert Diary Owner:");
                        String diaryowner = stdin.readLine();
                        Integer dairyid = dbInstance.SmartList(diaryowner, 1);
                        System.out.println("Insert Permissions:");
                        String premissions = stdin.readLine();
                        System.out.println(dbInstance.addPremission(usr, dairyid, premissions));
                    } else if (command[0].compareToIgnoreCase("rmusr") == 0) {
                        System.out.println("Insert User Name:");
                        String usr = stdin.readLine();
                        System.out.println(dbInstance.rmUsr(usr));
                    } else if (command[0].compareToIgnoreCase("rmdry") == 0) {
                        System.out.println("Insert Owners Name:");
                        String usr = stdin.readLine();
                        System.out.println("Insert DiaryID:");
                        String diaryid = stdin.readLine();
                        System.out.println(dbInstance.rmDry(usr, diaryid));
                    } else if (command[0].compareToIgnoreCase("rmper") == 0) {
                        System.out.println("Insert User Name:");
                        String usr = stdin.readLine();
                        System.out.println("Insert Diary Name:");
                        String diaryid = stdin.readLine();
                        System.out.println(dbInstance.rmPer(usr, diaryid));
                    } else if (command[0].compareToIgnoreCase("help") == 0) {
                        System.out.println("addusr  : Add A User.");
                        System.out.println("adddry  : Add A Diary. ");
                        System.out.println("addper  : Add Premission.");
                        System.out.println("rmusr   : Remove A User.");
                        System.out.println("rmdry   : Remove A Dairy.");
                        System.out.println("rmper   : Remove Premissions From A User.");
                        System.out.println("exit    : Quits Server.");
                        System.out.println("quit    : Quits Server.");
                    }
                } else {
                    // ensures arugmens greater then 3 are passed in and the rest are discaded
                    // saves time on users incorrectly inputting arguments.
                    System.out.println("Incorrect Input");
                    System.out.println("Type \"help\" for list of commands.\n");
                }

            } catch (IOException ex) {
                LOGIT.severe("Error with standard input, local mode.");
                LOGIT.info(ex.getLocalizedMessage());
            }
        }
    }
}
