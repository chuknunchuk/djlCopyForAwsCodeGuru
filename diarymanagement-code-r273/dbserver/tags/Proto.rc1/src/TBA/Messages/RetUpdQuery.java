/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TBA.Messages;
import TBA.Exceptions.MessagesException;
import java.util.Vector;

/**
 *
 * @author cs321tx2
 */
public class RetUpdQuery extends QueryMessage
{

   private final String QueryType = "RETUPD";

   private int DiaryID = -1;
   private String DiaryName = null;
   private int permissions = -1;
   private int RevNo = -1; // revision number of local
   private int NumberOfEntries = -1; //entries that have changed

   private final int diaryIDLen = 4;
   private final int diaryNameLen = 32;
   private final int permissionsLen = 3;
   private final int revNumLen = 8;
   private final int NumberOfEntriesLen = 8;

   Vector<DiaryEntry> entryRecords = new Vector<DiaryEntry>();

   //TODO: some sort of min len? cant have definate one because of entry records

    public RetUpdQuery(){}

    @Override
    public String Get(String dummy) throws MessagesException
    {
       
       String message = null;
       
       if (DiaryID == -1)
       {
           throw new MessagesException("Diary ID not set");
       }
       if (DiaryName == null)
       {
           throw new MessagesException("Diary Name is null");
       }

       if (permissions == -1)
       {
           throw new MessagesException("permisions are not set");
       }
       if (RevNo == -1)
       {
           throw new MessagesException("revision number is not set");
       }
       if (NumberOfEntries == -1)
       {
           throw new MessagesException("invalid number of entries");
       }


       message = String.format("%0" + Integer.toString(diaryIDLen) + "x", DiaryID);
       message += String.format("%1$-" + Integer.toString(diaryNameLen) + "s", DiaryName);
       message += String.format("%0" + Integer.toString(permissionsLen) + "o", permissions);
       message += String.format("%0" + Integer.toString(revNumLen) + "x", RevNo);
       message += String.format("%0" + Integer.toString(NumberOfEntriesLen) + "x", NumberOfEntries);

       for(int i=0; i < NumberOfEntries; i++)
       {
            message += entryRecords.get(i).Get();
       }

       super.SetQueryType(QueryType);
       return super.Get(message);
    }

    @Override
    public String Set(String message) throws MessagesException
    {
        message = super.Set(message);
        DiaryEntry tempRecord;

        try
        {
            DiaryID = Integer.parseInt(message.substring(0,diaryIDLen),16); //16 coz hexidecimal
            message = message.substring(diaryIDLen, message.length());
        }
        catch (NumberFormatException ex)
        {
           throw new MessagesException("entry ID is invalid");
        }

        DiaryName = message.substring(0,diaryNameLen).trim();
        message = message.substring(diaryNameLen, message.length());

        try
        {
           permissions = Integer.parseInt(message.substring(1, permissionsLen+1), 8);
           message = message.substring(permissionsLen + 1, message.length());
        }
        catch (NumberFormatException ex)
        {
           throw new MessagesException("The permissions are invalid");
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
        
        return null;
    }


   public int GetDiaryID()
   {
      return DiaryID;
   }

   public void SetDiaryID(int diary) throws MessagesException
   {
      if(diary < 0)
      {
         DiaryID = -1;
         throw new MessagesException("The Diary ID is invalid");
      }
      else
      {
         DiaryID = diary;
      }
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
        return RevNo;
    }

    public void setRevisionNo(int newRevNo)throws MessagesException {
        if(newRevNo < 0)
        {
            throw new MessagesException("Revision No is invalid");
        } else {
            RevNo = newRevNo;
        }
    }

    public int getNumberOfEntries() {
        return NumberOfEntries;
    }

    public void setNumberOfEntries(int newNumberOfEntries) throws MessagesException {
        if(newNumberOfEntries < 0)
        {
            throw new MessagesException("Number of entries is invalid");
        } else {
            NumberOfEntries = newNumberOfEntries;
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
