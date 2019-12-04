package TBA.Communications;

import TBA.Exceptions.*;
import TBA.Messages.*;
import TBA.Security.PasswordHashing;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the connection to the TBA server
 *<p>
 *
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 11            $ $Date:: 2009-07-21 #$
 */

public class ServerCom
{
   private static Socket MyConx;
   private static DataOutputStream os;
   private static DataInputStream is;
   public final static Logger LOGIT = Logger.getLogger(ServerCom.class.getName());
    
   /**
    * This constructor initialises the connection to the TBA Server
    *<p>
    *<p>
    * @throws ServerComException
    *
    * @param machineName This is the IP address/Hostname to connect to
    * @param portNumber The port number to connect on.
    *
    * @see java.net.Socket
    */
   public static void Start(String machineName, int portNumber) throws ServerComException
   {
      try
      {
         MyConx = new Socket(machineName, portNumber);
      }

      catch (UnknownHostException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket: Host name unknown");
         throw new ServerComException(ex);
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket");
         throw new ServerComException(ex);
      }

      try
      {
         os = new DataOutputStream(MyConx.getOutputStream());
         is = new DataInputStream(MyConx.getInputStream());
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating stream for the socket");
         throw new ServerComException(ex);
      }
   }

   public static boolean Login(String Username, String Password) throws ServerComException
   {
      LoginQuery lQ = new LoginQuery();
      try
      {
         PasswordHashing ph = new PasswordHashing();

         lQ.SetUserName(Username);
         lQ.SetPasswordHash(ph.getClientHash(Password));
         putData(lQ.Get(""));

         Messages header = new Messages();

         String headerData = getHeaderData();
         header.Set(headerData);

         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

         LoginResponse lR = new LoginResponse();
         lR.Set(headerData + bodyData);

         if(lR.GetErrorCode() != 0)
         {
            return false;
         }
         else
         {
            return true;
         }
      }
      catch (Exception ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
   }

   /**
    * Receive the header data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   private static String getHeaderData() throws NetworkDataException
   {
      byte[] s=new byte[Messages.messageHeaderLen];

      try
      {
         // Confirming size of message is correct is
         // handled later by the Messages.* classes themselves
         is.readFully(s, 0, Messages.messageHeaderLen);
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
   private static String getData(int size) throws NetworkDataException
   {
      byte[] s=new byte[size];
      try
      {
         // Confirming size of messages is correct is
         // handled later by the Messages.* classes themselves
         is.readFully(s, 0, size);
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
    * This sends data to the server
    *<p>
    *<p>
    * @throws ServerComException
    *
    * @param data Data to send to the server
    *
    * @see #getData()
    */
   private static void putData(String data) throws ServerComException
   {
      try
      {
         os.writeBytes(data);
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
   }
}