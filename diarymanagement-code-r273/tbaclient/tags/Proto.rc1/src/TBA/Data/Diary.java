/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Data;

import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

/**
 * This class serves as the interface to the data of a single Diary.
 *<p>
 * @author Dan Mcgrath
 *
 * @version $Rev:: 132           $ $Date:: 2009-09-17 #$
 */
public class Diary
{
   private int ID;
   private String Name;
   private String ownerName; // TODO: Maybe int userID?
   private char ownerFlag; // Y or N
   private int permissions; // TODO: Right data type?
   private int revision;
   private Vector<Entry> entries = null;

   public int getID()
   {
      return ID;
   }

   public String getName()
   {
      return Name;
   }

   @Override
   public String toString()
   {
      return Name;
   }


   public Vector<Entry> getEntries()
   {
      return entries;
   }

   public String getOwnerName()
   {
      return ownerName;
   }

   public int getPermissions()
   {
      return permissions;
   }

   public int getRevision()
   {
      return revision;
   }

   public char getOwnerFlag()
   {
      return ownerFlag;
   }

   public void setOwnerFlag(char ownerFlag)
   {
      this.ownerFlag = ownerFlag;
   }

   public void setID(int ID)
   {
      this.ID = ID;
   }

   public void setName(String Name)
   {
      this.Name = Name;
   }

   public void setEntries(Vector<Entry> entries)
   {
      this.entries = entries;
   }

   public void setOwnerName(String ownerName)
   {
      this.ownerName = ownerName;
   }

   public void setPermissions(int permissions)
   {
      this.permissions = permissions;
   }

   public void setRevision(int revision)
   {
      this.revision = revision;
   }

   public void addEntry(Entry newEntry)
   {
       if (entries == null )
       {
           entries=new Vector<Entry>();
       }
       entries.add(newEntry);
   }

   public Vector<Entry> getEntriesByDate(Calendar now)
   {
      Vector<Entry> trimmedEntries = new Vector<Entry>();
      
      for(Entry aEntry : entries)
      {
         if(aEntry.during(now))
         {
            trimmedEntries.add(aEntry);
         }
      }

      System.out.println("trimmed "+trimmedEntries.toString());
      if (trimmedEntries.isEmpty())
          trimmedEntries = null;
      else
          Collections.sort(trimmedEntries);
      return trimmedEntries;
   }
}
