package TBA.Communications;

import TBA.Exceptions.*;
import TBA.Messages.*;
import TBA.Data.*;
import TBA.Security.PasswordHashing;
import java.io.*;
import java.util.Vector;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.logging.Logger;

/**
 * This class handles the connection to the TBA server
 *<p>
 *
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 119           $ $Date:: 2009-09-12 #$
 */

public class ServerCom
{
   private SSLSocket myConx = null;
   private int socketPort = 0;
   private String socketAddress = null;
   private DataOutputStream os;
   private DataInputStream is;
   private boolean isConnected = false;
   private DBServer DBCom;

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
   public void Start(String machineName, int portNumber) throws ServerComException
   {
      try
      {
         socketAddress = machineName;
         socketPort = portNumber;
         SSLSocketFactory sslSockFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         myConx = (SSLSocket) sslSockFactory.createSocket(machineName, portNumber);
         isConnected = true;
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket");
         throw new ServerComException(ex);
      }

      try
      {
         os = new DataOutputStream(myConx.getOutputStream());
         is = new DataInputStream(myConx.getInputStream());
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating stream for the socket");
         isConnected = false;
         throw new ServerComException(ex);
      }
   }

   public User Login(String Username, String Password) throws ServerComException
   {
      if(!isConnected)
      {
         Start(socketAddress, socketPort);
      }

      try
      {
         if(isConnected)
         {
            return NetLogin(Username, Password);
         }
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            LOGIT.info(ex.getLocalizedMessage());
            throw ex;
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.fine("No network connection, attempting DB");
            return null; //DBLogin(Username, Password);
         }
      }
      
      return null;
   }

   public User NetLogin(String Username, String Password) throws ServerComException
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
            return null;
         }
         else
         {
            User currentUser = new User();
            Vector<Diary> diaries = new Vector<Diary>();
            Diary aDiary ;

            currentUser.setDisplayName(lR.GetDisplayName().trim());
            currentUser.setDefaultDiaryID(lR.GetDefaultDiary());
            currentUser.setSessionID(lR.GetSessionID());


            for(DiaryHeader aDiaryHeader : lR.GetDiaryHeaders())
            {
               aDiary = new Diary();
               aDiary.setID(aDiaryHeader.GetDiaryID());
               aDiary.setName(aDiaryHeader.GetDiaryName());
               aDiary.setPermissions(aDiaryHeader.GetPermissions());
               aDiary.setOwnerFlag(aDiaryHeader.GetOwnerFlag());
               aDiary.setRevision(aDiaryHeader.GetRevNum());
               diaries.add(aDiary);
            }

            currentUser.setDiaries(diaries);

            return currentUser;
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         isConnected = false;
         throw new ServerComException("Login error");
      }
      catch (MessagesException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
      catch (PasswordHashingException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
   }

   public void Logout(String sessionID) throws ServerComException
   {
      if(!isConnected)
      {
         Start(socketAddress, socketPort);
      }
      try
      {
         if(isConnected)
         {
            NetLogout(sessionID);
         }
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            LOGIT.info(ex.getLocalizedMessage());
            throw ex;
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.fine("No network connection, attempting DB");
            //DBLogin(sessionID);
         }
      }
   }

   public void NetLogout(String sessionID) throws ServerComException
   {
      LogoutQuery lQ = new LogoutQuery();
      try
      {
         lQ.SetSessionID(sessionID);

         putData(lQ.Get(""));

         // Pretty much for show, since we can't actually do anything about a
         // failed logout attempt...
         Messages header = new Messages();
         String headerData = getHeaderData();
         header.Set(headerData);
         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);
         LogoutResponse lR = new LogoutResponse();
         lR.Set(headerData + bodyData);
      }
      catch (MessagesException ex)
      {
         throw new ServerComException(ex.getLocalizedMessage());
      }
      catch (NetworkDataException ex)
      {
         throw new ServerComException(ex.getLocalizedMessage());
      }
   }

   /**
    * Receive the header data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   private String getHeaderData() throws NetworkDataException
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
   private String getData(int size) throws NetworkDataException
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
   private void putData(String data) throws ServerComException
   {
      try
      {
         os.writeBytes(data);
         os.flush();
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
   }
}