/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Data;

import java.util.Vector;

/**
 *
 * @author dmcgra
 */
public class User
{
   private String displayName;
   private String sessionID;
   private int defaultDiaryID;
   private int numberOfDiaries;
   Vector<Diary> Diaries;

    public Vector<Diary> getDiaries()
    {
        return Diaries;
    }

    public void setDiaries(Vector<Diary> Diaries)
    {
        this.Diaries = Diaries;
    }

   public String getSessionID()
   {
      return sessionID;
   }

   public void setSessionID(String sessionID)
   {
      this.sessionID = sessionID;
   }

   public void setDefaultDiaryID(int defaultDiaryID)
   {
      this.defaultDiaryID = defaultDiaryID;
   }

   public void setDisplayName(String displayName)
   {
      this.displayName = displayName;
   }

   public void setNumberOfDiaries(int numberOfDiaries)
   {
      this.numberOfDiaries = numberOfDiaries;
   }

   public int getDefaultDiaryID()
   {
      return defaultDiaryID;
   }

   public String getDisplayName()
   {
      return displayName;
   }

   public int getNumberOfDiaries()
   {
      return numberOfDiaries;
   }
}
