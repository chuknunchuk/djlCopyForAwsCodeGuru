package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the LOGOUT query message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 27            $ $Date:: 2009-07-31 #$
 */
public class GetDiaryQuery extends QueryMessage
{
   private final String QueryType = "GETDIARY";
   private int DiaryID;
   
   public GetDiaryQuery() { }

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
   
   public int GetDiaryID()
   {
       return DiaryID;
   }
   
   public void SetDiaryID(int temp) throws MessagesException
   {
      if(temp < 0)
      {
         DiaryID = -1;
         throw new MessagesException("The Diary ID is invalid");
      }
      else
      {
         DiaryID = temp;
      }
   }
}