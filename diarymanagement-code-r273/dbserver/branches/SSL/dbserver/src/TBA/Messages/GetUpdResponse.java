package TBA.Messages;

import TBA.Exceptions.MessagesException;
import java.util.Vector;

/**
 * This class is the response message to a Login Query
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 27            $ $Date:: 2009-07-31 #$
 */

public class GetUpdResponse extends ResponseMessage
{
   private String DiaryName;
   private char ownerFlag;
   private int permissions;
   private int revisionNo;

   private final int DiaryNameLen = 32;
   private final int ownerFlagLen = 1;
   private final int permissionsLen = 3;
   private final int revisionNoLen = 8;
   private final int NumberOfEntriesLen = 8;
   //TODO: use function of size of entries maybe?
   
   //Dont think we can have a final GetUpdResponseLen becuase of differing body sizes? will use function
   //private final int GetUpdResponseLen = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();
    
   public int GetUpdResponseLen()
   {
       int length;
       
       length = GetUpdResponseLen = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();
       
       return length;
   }
   
   public GetUpdResponse(){}
   
   private int totalEntrySize()
   {
       //TODO: get entry sizes
   }
   
   //TODO: Get() and Set()
   //TODO: get/sets for member vars 
 
}