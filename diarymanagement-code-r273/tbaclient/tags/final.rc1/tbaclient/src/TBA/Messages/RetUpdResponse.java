/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;
import TBA.Exceptions.MessagesException;
import java.util.Vector;

/**
 * This class is for the RetUpdResponse message, it is a response to a RetUpdQuery
 * @author cs321tx2
 */
public class RetUpdResponse extends ResponseMessage
{
   private String DiaryName = null;
   private char ownerFlag = '\0';
   private int permissions = -1;
   private int revisionNo = -1;
   private int NumberOfEntries = -1; //entries that have changed

   Vector<DiaryEntry> entryRecords = new Vector<DiaryEntry>();

   private final int DiaryNameLen = 32;
   private final int ownerFlagLen = 1;
   private final int permissionsLen = 3;
   private final int revisionNoLen = 8;
   private final int NumberOfEntriesLen = 8;
   //TODO: use function of size of entries maybe?

   //Dont think we can have a final RetUpdResponseLen becuase of differing body sizes? will use function
   //private final int GetUpdResponseLen = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();

   /**
    * This method returns the total length of a RetUpdResponse string including
    * all the entry sizes total
    * @return
    */
   public int RetUpdResponseLen()
   {
       int length;

       length = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();

       return length;
   }

     /**
     * constructor does nothing
     */
   public RetUpdResponse(){}

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


     /**
     * The Get() Method gets the RetUpdResponse message with the Message and Query header.
     * @return The actual message string you can send
     * @throws MessagesException
     * @see #Set(java.lang.String)
     * @see TBA.Messages.ResponseMessage#Get(java.lang.String)
     * @see TBA.Messages.Messages#Get(java.lang.String)
     * @param dummy - to override Response's Get(String)
     */
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

     /**
     * The Set() Method strips out all the fields of a RetUpdResponse message. They
     * are then available by the Get methods of this class.
     * @param message that fields will be set from
     * @return remainder of message
     * @throws MessagesException
     * @see #Get(java.lang.String)
     * @see TBA.Messages.ResponseMessage#Set(java.lang.String)
     * @see TBA.Messages.Messages#Set(java.lang.String)
     */
    @Override
   public String Set(String message) throws MessagesException
   {
        message = super.Set(message);
       //System.out.println(message);

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
          message = message.substring(revisionNoLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("revision number is invalid");
       }

       try
       {
           NumberOfEntries = Integer.parseInt(message.substring(0,NumberOfEntriesLen),16); //16 coz hexidecimal
           message = message.substring(NumberOfEntriesLen, message.length());
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

       //message = super.Set(message);

       return message;
   }

    /**
     * This method returns the DiaryName field
     * @return DiaryName field
     */
    public String getDiaryName() {
        return DiaryName;
    }

    /**
     * Sets DiaryName field to newDiaryName, cannot be null
     * @param newDiaryName as the new DiaryName field
     * @throws MessagesException
     */
    public void setDiaryName(String newDiaryName) throws MessagesException {
        if(newDiaryName == null)
        {
            throw new MessagesException("Diary Name is invalid");
        } else {
            DiaryName = newDiaryName;
        }
    }

     /**
     * This method returns the NumberOfEntries field
     * @return NumberOfEntries field
     */
    public int getNumberOfEntries() {
        return NumberOfEntries;
    }

    /**
     * NumberOfEntries field is set when entries are added,
     * Use this to manually set/change this
     * @param newNumberOfEntries - new NumberOfEntries to set to
     * @throws MessagesException
     */
    public void setNumberOfEntries(int newNumberOfEntries) throws MessagesException {
        if(newNumberOfEntries < 0)
        {
            throw new MessagesException("Number of entries is invalid");
        } else {
            NumberOfEntries = newNumberOfEntries;
        }
    }

   /**
     * This method returns the ownerFlag field, will only be 'Y' or 'N'
     * @return ownerFlag field, will only be 'Y' or 'N'
     */
    public char getOwnerFlag() {
        return ownerFlag;
    }

     /**
     * Sets ownerFlag field, can only be 'Y' or 'N'
     * @param newOwnerFlag
     * @throws MessagesException
     */
    public void setOwnerFlag(char newOwnerFlag) throws MessagesException {
        if((newOwnerFlag == 'Y') || (newOwnerFlag == 'N'))
        {
            ownerFlag = newOwnerFlag;
        } else {
            throw new MessagesException("Invalid flag");
        }
    }

     /**
     * This method returns the permissions field using unix format
     * @return permissions
     * @throws MessagesException
     */
    public int getPermissions() throws MessagesException {
        return permissions;
    }

    /**
     * Sets permissions field using unix format, cannot be <0 or >511
     * @param newPermissions
     * @throws MessagesException
     */
    public void setPermissions(int newPermissions) throws MessagesException {
        if(newPermissions < 0  || newPermissions > 511 )
        {
            throw new MessagesException("Permissions is invalid");
        } else {
            permissions = newPermissions;
        }
    }

     /**
     * This method returns the revision number
     * @return revisionNo
     */
    public int getRevisionNo() {
        return revisionNo;
    }

     /**
     * Sets revisionNo field, cannot be < 0
     * @param newRevNo
     * @throws MessagesException
     */
    public void setRevisionNo(int newRevNo)throws MessagesException {
        if(newRevNo < 0)
        {
            throw new MessagesException("Revision No is invalid");
        } else {
            revisionNo = newRevNo;
        }
    }

    /**
     * Sets entryRecord field which is a vector of {@link DiaryEntry}
     * @param temps new DiaryEntries to send
     * @throws MessagesException
     */
    @SuppressWarnings("unchecked")
    //I think the cloning/casting is beacuse of the pointer property
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

    /**
     * This method returns the entryRecord field which is a vector of {@link DiaryEntry}
     * @return vector of {@link DiaryEntry}
     */
    @SuppressWarnings("unchecked")
    //I think the cloning/casting is beacuse of the pointer property
   public Vector<DiaryEntry> getEntries()
   {
      return (Vector<DiaryEntry>)entryRecords.clone();
   }
    

}
