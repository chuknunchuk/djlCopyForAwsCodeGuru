/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;
import TBA.Exceptions.MessagesException;
/**
 *
 * @author cs321tx2
 */
public class DiaryEntry 
{
   private int entryID = -1;
   private String startDate = null; //YYYYMMDD
   private String endDate = null;   //YYYYMMDD
   private String startTime = null; //HHMM
   private String endTime = null;   //HHMM
   private char ownerFlag = '\0';   //Y if owner N if not
   private char lockedFlag = '\0';   //Y if locked N if not
   private String creatingUser = null; //Username who created it
   private String title = null;
   private String body = null;
   
   private final int entryIDLen = 8;
   private final int startDateLen = 8;
   private final int endDateLen = 8;
   private final int startTimeLen = 4;
   private final int endTimeLen = 4;
   private final int ownerFlagLen = 1;
   private final int lockedFlagLen = 1;
   private final int creatingUserLen = 32;
   private final int titleLen = 32;
   //TODO: size of body, prolly wont final EntryLen because of changing body size maybe?
   
   private final int EntryLen = entryIDLen + startDateLen + endDateLen + startTimeLen + endTimeLen + ownerFlagLen + lockedFlagLen + creatingUserLen + titleLen + getBodyLen();//bodyLength;
   
   //TODO: get body length, think we using | for delimiter
   private int getBodyLen()
   {
       int length = -1;
       return length;
   } 
   
   public int GetEntrySize()
   {
       return EntryLen;
   }
   
   public DiaryEntry(){}
   
   public String Get() throws MessagesException
   {
       String entry;
       
       if (entryID == -1)
       {
           throw new MessagesException("entry ID not set");
       }
       if (startDate == null)
       {
           throw new MessagesException("Start Date not set");
       }
       if (endDate == null)
       {
           throw new MessagesException("End Date not set");
       }
       if (startTime == null)
       {
           throw new MessagesException("Start Time is not set");
       }
       if (endTime == null)
       {
           throw new MessagesException("End Time is not set");
       }
       if (ownerFlag == '\0')
       {
           throw new MessagesException("Owner Flag is not set");
       }
       if(lockedFlag == '\0')
       {
           throw new MessagesException("Locked Flag is not set");
       }
       if(creatingUser == null)
       {
           throw new MessagesException("Creating User is not set");
       }
       if(title == null)
       {
           throw new MessagesException("Title is not set");
       }
       if(body == null)
       {
           throw new MessagesException("Body is not set");
       }
      
       //TODO: check to see if we need a title and body, ie can we have an empty body actual formatted string to send
       return entry;   
               
   }
   
   public String Set(String test) throws MessagesException
   {
       //TODO: DiaryEntry set
       return test;
   }
   
   //TODO: get/sets for member vars
 }
