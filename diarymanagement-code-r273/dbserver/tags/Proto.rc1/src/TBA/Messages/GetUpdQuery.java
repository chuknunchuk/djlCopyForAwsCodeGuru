package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the LOGOUT query message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 121           $ $Date:: 2009-09-13 #$
 */
public class GetUpdQuery extends QueryMessage
{
   private final String QueryType = "GETUPD";
   private int DiaryID = -1;
   private int RevNo = -1;

   private final int diaryIDLen = 4;
   private final int revNumLen = 8;

   private final int GetUpdQueryLen = diaryIDLen + revNumLen;
   
   public GetUpdQuery() { }

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

       if (DiaryID == -1)
       {
           throw new MessagesException("Diary ID not set");
       }
      
       if (RevNo == -1)
       {
           throw new MessagesException("revision number is not set");
       }

      message = String.format("%0" + Integer.toString(diaryIDLen) + "x", DiaryID);
      message += String.format("%0" + Integer.toString(revNumLen) + "x", RevNo);

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

      if(message.length() != GetUpdQueryLen)
      {
         throw new MessagesException("Get Update Query is invalid");
      }

      try
      {
         DiaryID = Integer.parseInt(message.substring(0,diaryIDLen),16); //16 coz hexidecimal
         message = message.substring(diaryIDLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("entry ID is invalid");
      }

      try
      {
         RevNo = Integer.parseInt(message.substring(0, revNumLen), 16);
         message = message.substring(revNumLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The revision number is invalid");
      }


      return null;
   }
   
   public int GetRevisionNumber()
   {
       return RevNo;
   }
   
   public void SetRevisionNumber(int newRevNo) throws MessagesException
   {
       if (newRevNo  < 0)
       {
           throw new MessagesException("Revision number is null");
       }
       else
       {
          RevNo = newRevNo;
       }
   }
   
   public int GetDiaryID()
   {
       return DiaryID;
   }
   
   public void SetDiaryID(int newID) throws MessagesException
   {
       if (newID  < 0)
       {
           throw new MessagesException("New Revision ID is null");
       }
       else
       {
          DiaryID = newID;
       }
   }
}