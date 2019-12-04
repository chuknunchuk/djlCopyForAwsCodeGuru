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

   //cant have this final int, coz body is changing... and you can only get after body's been set
   //private final int EntryLen = entryIDLen + startDateLen + endDateLen + startTimeLen + endTimeLen + ownerFlagLen + lockedFlagLen + creatingUserLen + titleLen + getBodyLen();//bodyLength;
   
   //delimter is only placed after message is made, it isnt stored as part of body
   private int getBodyLen()
   {
       int length = body.length();
       return length;
   } 

   //returns -1 if body is null?
   public int GetEntrySize()
   {
       if (body == null)
       {
           return -1;
       }else {
           return entryIDLen + startDateLen + endDateLen + startTimeLen + endTimeLen + ownerFlagLen + lockedFlagLen + creatingUserLen + titleLen + body.length();
       }
       //GetEntrySize returns size w/o delimiter
   }
   
   public DiaryEntry(){}

   //Get function makes the entry record string to send
   public String Get() throws MessagesException
   {
       String EntryRecord = null;
       
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
      
       //x is hex, s is string, o is octal - padding

       EntryRecord = String.format("%0" + Integer.toString(entryIDLen) + "x", entryID);
       EntryRecord += String.format("%1$-" + Integer.toString(startDateLen) + "s", startDate);
       EntryRecord += String.format("%1$-" + Integer.toString(endDateLen) + "s", endDate);
       EntryRecord += String.format("%1$-" + Integer.toString(startTimeLen) + "s", startTime);
       EntryRecord += String.format("%1$-" + Integer.toString(endTimeLen) + "s", endTime);
       EntryRecord += ownerFlag;
       EntryRecord += lockedFlag;
       //not sure if im padding teh user and title, but i'l leae for now
       EntryRecord += String.format("%1$-" + Integer.toString(creatingUserLen) + "s", creatingUser);
       EntryRecord += String.format("%1$-" + Integer.toString(titleLen) + "s", title);
       
       EntryRecord += body + '|';

       return EntryRecord;
       
   }

   //sets member variables from record string
   public String Set(String entryRecord) throws MessagesException
   {
       try
       {
           entryID = Integer.parseInt(entryRecord.substring(0,entryIDLen),16); //16 coz hexidecimal
           entryRecord = entryRecord.substring(entryIDLen, entryRecord.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("entry ID is invalid");
       }

       startDate = entryRecord.substring(0,startDateLen).trim();
       entryRecord = entryRecord.substring(startDateLen, entryRecord.length());

       endDate = entryRecord.substring(0,endDateLen).trim();
       entryRecord = entryRecord.substring(endDateLen, entryRecord.length());

       startTime = entryRecord.substring(0,startTimeLen).trim();
       entryRecord = entryRecord.substring(startTimeLen, entryRecord.length());

       endTime = entryRecord.substring(0,endTimeLen).trim();
       entryRecord = entryRecord.substring(endTimeLen,entryRecord.length());

       ownerFlag = entryRecord.charAt(0);
       entryRecord = entryRecord.substring(ownerFlagLen, entryRecord.length());
       
       if(ownerFlag != 'N' && ownerFlag != 'Y')
       {
           throw new MessagesException("Invalid ownership flag");
       }

       lockedFlag = entryRecord.charAt(0);
       entryRecord = entryRecord.substring(lockedFlagLen, entryRecord.length());

       if(lockedFlag != 'N' && lockedFlag != 'Y')
       {
           throw new MessagesException("Invalid locking flag");
       }

       creatingUser = entryRecord.substring(0,creatingUserLen).trim();
       entryRecord = entryRecord.substring(creatingUserLen, entryRecord.length());

       title = entryRecord.substring(0,titleLen).trim();
       entryRecord = entryRecord.substring(titleLen, entryRecord.length());


       int token = entryRecord.indexOf("|");

       if (token < 0)
       {
          body = entryRecord.substring(0,entryRecord.length()-1);
       } else {
          //-1 for the delimiter
          body = entryRecord.substring(0,token).trim();
          //+1 to skill the token
          entryRecord = entryRecord.substring(token+1,entryRecord.length());

          //body = entryRecord;
          //since everythign else is the body
       }

       return entryRecord;
   }
   
   //member variables get/sets
    public String getBody() {
        return body; // because the last char is a pipe
    }

    //can we have null bodies?
    public void setBody(String newBody) throws MessagesException {
        if(newBody == null)
        {
           throw new MessagesException("body is null");
        } else {
           body = newBody;
        }
    }

    public String getCreatingUser() {
        return creatingUser;
    }

    public void setCreatingUser(String newCreatingUser) throws MessagesException {
        if (newCreatingUser == null)
        {
            throw new MessagesException("creating user must not be null");
        } else {
            creatingUser = newCreatingUser;
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String newEndDate) throws MessagesException {
        if(newEndDate == null)
        {
           throw new MessagesException("title is null");
        } else {
           endDate = newEndDate;
        }
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String newEndTime) throws MessagesException {
        if(newEndTime == null)
        {
           throw new MessagesException("title is null");
        } else {
           endTime = newEndTime;
        }
    }

    public int getEntryID() {
        return entryID;
    }

    public void setEntryID(int newID) throws MessagesException {
        if (newID < 0)
        {
            throw new MessagesException("new entry ID is invalid");
        } else {
            entryID = newID;
        }
    }

    public char getLockedFlag() {
        return lockedFlag;
    }

    public void setLockedFlag(char newLockedFlag) throws MessagesException {
        if((newLockedFlag == 'Y') || (newLockedFlag == 'N'))
        {
            lockedFlag = newLockedFlag;
        } else {
            throw new MessagesException("Invalid flag");
        }
    }

    public char getOwnerFlag() {
        return ownerFlag;
    }

    public void setOwnerFlag(char newOwnerFlag) throws MessagesException {
        if((newOwnerFlag == 'Y') || (newOwnerFlag == 'N'))
        {
            ownerFlag = newOwnerFlag;
        } else {
            throw new MessagesException("Invalid flag");
        }
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String newStartDate) throws MessagesException {
        if (newStartDate == null)
        {
            throw new MessagesException("start date must not be null");
        } else {
            startDate = newStartDate;
        }
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String newStartTime) throws MessagesException {
        if (newStartTime == null)
        {
            throw new MessagesException("Start Time must not be null");
        } else {
            startTime = newStartTime;
        }
    }

    public String getTitle() {
        return title;
    }

    //im guessing there must always be a title?
    public void setTitle(String newTitle) throws MessagesException {
        if(newTitle == null)
        {
           throw new MessagesException("Title must not be null");
        } else {
           title = newTitle;
        }
    }

 }
