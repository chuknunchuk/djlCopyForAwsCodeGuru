//ori

package TBA.Messages;

import TBA.Exceptions.MessagesException;
import java.util.Vector;

/**
 * This class is the response message to a Login Query
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 141           $ $Date:: 2009-09-17 #$
 */

public class GetUpdResponse extends ResponseMessage
{
   private String DiaryName = null;
   private char ownerFlag = '\0';
   private int permissions = -1;
   private int revisionNo = -1;
   private int NumberOfEntries = -1;

   Vector<DiaryEntry> entryRecords = new Vector<DiaryEntry>();

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
       
       length = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();
       
       return length;
   }
   
   public GetUpdResponse(){}
   
   private int totalEntrySize()
   {
       //TODO: get entry sizes

       int size = 0;
       for(int i=0; i < NumberOfEntries; i++)
       {
             size += entryRecords.get(i).GetEntrySize();
       }
      return size;
   }
   
    @Override
    public String Get(String dummy) throws MessagesException
    {
        String message;

        if (DiaryName == null)
        {
            throw new MessagesException("Diary Name is null");
        }
        if (ownerFlag == '\0')
        {
            throw new MessagesException("owner flag is not set");
        }
        if (permissions == -1)
        {
            throw new MessagesException("permisions are not set");
        }
        if (revisionNo == -1)
        {
            throw new MessagesException("revision number is not set");
        }
        if (NumberOfEntries == -1)
        {
            throw new MessagesException("invalid number of entries");
        }

        message = String.format("%1$-" + Integer.toString(DiaryNameLen) + "s", DiaryName);
        message += ownerFlag;
        message += String.format("%0" + Integer.toString(permissionsLen) + "o", permissions);
        message += String.format("%0" + Integer.toString(revisionNoLen) + "x", revisionNo);
        message += String.format("%0" + Integer.toString(NumberOfEntriesLen) + "x", NumberOfEntries);
        
        for(int i=0; i < NumberOfEntries; i++)
        {
             message += entryRecords.get(i).Get();
        }

        super.SetErrorCode(0);
        return super.Get(message);

    }

    @Override
   public String Set(String message) throws MessagesException
   {

       DiaryEntry tempRecord;

       if(GetErrorCode() > 0)
       {
          return "";
       }


       DiaryName = message.substring(0,DiaryNameLen).trim();
       message = message.substring(DiaryNameLen, message.length());

       ownerFlag = message.charAt(0);
       message = message.substring(ownerFlagLen, message.length());
       
       if(ownerFlag != 'N' && ownerFlag != 'Y')
       {
           throw new MessagesException("Invalid ownership flag");
       }

       try
       {
           permissions = Integer.parseInt(message.substring(0,permissionsLen),8);
           message = message.substring(permissionsLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("The permissions are invalid");
       }
       try
       {
          revisionNo = Integer.parseInt(message.substring(0,revisionNoLen),16); //16 coz hexidecimal
          message.substring(revisionNoLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("revision number is invalid");
       }

       try
       {
           NumberOfEntries = Integer.parseInt(message.substring(0,NumberOfEntriesLen),16); //16 coz hexidecimal
           message.substring(NumberOfEntriesLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("Number of entries are invalid");
       }
       
       for(int i=0; i < NumberOfEntries; i++)
       {
         // The following line can throw a MessagesException if the entry record is not valid.
         tempRecord = new DiaryEntry();
         message = tempRecord.Set(message);
         entryRecords.add(tempRecord);
       }


       message = super.Set(message);

       return message;
   }

    public String getDiaryName() {
        return DiaryName;
    }

    public void setDiaryName(String newDiaryName) throws MessagesException {
        if(newDiaryName == null)
        {
            throw new MessagesException("Diary Name is invalid");
        } else {
            DiaryName = newDiaryName;
        }
    }

    public int getNumberOfEntries() {
        return NumberOfEntries;
    }

    // to manually change number of entries
    public void setNumberOfEntries(int newNumberOfEntries) throws MessagesException {
        if(newNumberOfEntries < 0)
        {
            throw new MessagesException("Number of entries is invalid");
        } else {
            NumberOfEntries = newNumberOfEntries;
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

    public int getPermissions() throws MessagesException {
        return permissions;
    }

    public void setPermissions(int newPermissions) throws MessagesException {
        if(newPermissions < 0  || newPermissions > 511 )
        {
            throw new MessagesException("Permissions is invalid");
        } else {
            permissions = newPermissions;
        }
    }

    public int getRevisionNo() {
        return revisionNo;
    }

    public void setRevisionNo(int newRevNo)throws MessagesException {
        if(newRevNo < 0)
        {
            throw new MessagesException("Revision No is invalid");
        } else {
            revisionNo = newRevNo;
        }
    }

    public void setEntries(Vector<DiaryEntry> temps) throws MessagesException
    {
      if(temps == null)
      {
         entryRecords = new Vector<DiaryEntry>();
         throw new MessagesException("The Diary Entry records are null");
      }
      else
      {
         entryRecords = (Vector<DiaryEntry>)temps.clone();
         NumberOfEntries = entryRecords.size();
      }
    }

   public Vector<DiaryEntry> getEntries()
   {
      return (Vector<DiaryEntry>)entryRecords.clone();
   }

}
