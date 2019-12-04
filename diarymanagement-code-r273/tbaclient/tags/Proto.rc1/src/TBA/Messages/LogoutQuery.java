package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the LOGOUT query message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 96            $ $Date:: 2009-09-06 #$
 */
public class LogoutQuery extends QueryMessage
{
   private final String QueryType = "LOGOUT";

   public LogoutQuery() { }

   /**
    * The Get() Method gets the LOGOUT message with the Message and Query header.
    *<p>
    * @returns The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      String message = null;

      super.SetQueryType(QueryType);
      
      return super.Get(message);
   }

   /**
    * The Set() Method strips out all the fields of a Logout Query message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetQuery(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);

      if(message.length() != 0)
      {
         throw new MessagesException("The Login Query is invalid");
      }

      return null;
   }
}