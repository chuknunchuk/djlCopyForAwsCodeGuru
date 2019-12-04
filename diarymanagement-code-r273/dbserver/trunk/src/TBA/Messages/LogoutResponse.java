package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is the response message to a Logout Query
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 252           $ $Date:: 2009-10-23 #$
 */
public class LogoutResponse extends ResponseMessage
{
    /**
    * Constructor does nothing
    */
   public LogoutResponse() {}

   /**
    * The Get() Method gets the LOGOUT message
    *<p>
    * @param dummy 
    * @return The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      return super.Get("");
   }

   /**
    * The Set() Method strips out all the fields of a Logout Query message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @return The message without the 'Response Header'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);
      return null;
   }
}