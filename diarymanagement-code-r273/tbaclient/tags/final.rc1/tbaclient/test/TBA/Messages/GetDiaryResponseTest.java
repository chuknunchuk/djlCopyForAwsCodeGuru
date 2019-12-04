/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import junit.framework.TestCase;
import TBA.Exceptions.MessagesException;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author cs321tx2
 */
public class GetDiaryResponseTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public GetDiaryResponseTest(String testName) {
        super(testName);
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /**
    * Test of GetDiaryResponseLen method, of class GetDiaryResponse.
    */
/*   public void testGetDiaryResponseLen()
   {
      System.out.println("GetDiaryResponseLen");
      GetDiaryResponse instance = new GetDiaryResponse();
      int expResult = 0;
      int result = instance.GetDiaryResponseLen();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }*/

   /**
    * Test of Get method, of class GetDiaryResponse.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
      //stupidest longest test case ever
      System.out.println("Get");
      String dummy = "";
      
      GetDiaryResponse setInstance = new GetDiaryResponse();
      GetDiaryResponse getInstance = new GetDiaryResponse();

      //getDiaryResponse vars
      String expDiaryName = "aDiaryName";
      String resDiaryName;

      char expOwnerFlag = 'Y';
      char resOwnerFlag;
      
      int expPermissions = 444;
      int resPermissions;

      int expRevNo = 1;
      int resRevNo;

      //int expTotalEntries = 3;
      int resTotalEntries;

      Vector<DiaryEntry> entryRecords = new Vector<DiaryEntry>();
      Vector<DiaryEntry> resEntryRecords = new Vector<DiaryEntry>();


      //diary vars
      int expEntryIDRec = 20;
      int resEntryIDRec; //result entryID

      Calendar expStartRec = Calendar.getInstance();
      long resStartRec;

      Calendar expEndRec = Calendar.getInstance();
      long resEndRec;
      char expOwnerFlagRec = 'Y';
      char resOwnerFlagRec;

      char expLockedFlagRec = 'Y';
      char resLockedFlagRec;

      String expUserRec = "bob";
      String resUserRec;

      String expTitleRec = "title";
      String resTitleRec;

      String BodyRec = "superMeetinglify";
      String resBodyRec;



      DiaryEntry setDiaryEntry = new DiaryEntry();
      
      setDiaryEntry.setEntryID(expEntryIDRec);
      setDiaryEntry.setStart(expStartRec.getTimeInMillis());
      setDiaryEntry.setEnd(expEndRec.getTimeInMillis());
      setDiaryEntry.setOwnerFlag(expOwnerFlagRec);
      setDiaryEntry.setLockedFlag(expLockedFlagRec);
      setDiaryEntry.setCreatingUser(expUserRec);
      setDiaryEntry.setTitle(expTitleRec);
      setDiaryEntry.setBody(BodyRec);

      entryRecords.add(setDiaryEntry);

      try
      {
         setInstance.setDiaryName(expDiaryName);
         setInstance.setOwnerFlag(expOwnerFlag);
         setInstance.setPermissions(expPermissions);
         setInstance.setRevisionNo(expRevNo);
         setInstance.setNumberOfEntries(entryRecords.size());
         setInstance.setEntries(entryRecords);

         getInstance.Set(setInstance.Get(""));

         resDiaryName = getInstance.getDiaryName();
         resOwnerFlag = getInstance.getOwnerFlag();
         resPermissions = getInstance.getPermissions();
         resRevNo = getInstance.getRevisionNo();
         resTotalEntries = getInstance.getNumberOfEntries();
         resEntryRecords = getInstance.getEntries();

         assertEquals(expDiaryName, resDiaryName);
         assertEquals(expOwnerFlag, resOwnerFlag);
         assertEquals(expPermissions,resPermissions);
         assertEquals(expRevNo,resRevNo);
         assertEquals(entryRecords.size(),resTotalEntries);

         /*boolean result = entryRecords.equals((Vector<DiaryEntry>)resEntryRecords);
         assertEquals(true,result);*/

      } catch (MessagesException ex) {
          fail("Unexpected throw!");
      }


        try
            {
               getInstance = new GetDiaryResponse();

               String failResult = getInstance.Get("");

               fail("Expected throw, but did not receive!");
            }
        catch (MessagesException ex)
        {
        }
   }
   
   /**
    * Test of getDiaryName method, of class GetDiaryResponse.
    */
   public void testGetDiaryName()
   {
      System.out.println("getDiaryName");
      GetDiaryResponse instance = new GetDiaryResponse();
      String expResult = "bob";

      try
      {
         instance.setDiaryName(expResult);
         String result = instance.getDiaryName();

         assertEquals(expResult, result);
      } catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      // TODO: go see what thsi try does
      try
      {
       instance.setDiaryName(null);
       fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }

   }

   /**
    * Test of getNumberOfEntries method, of class GetDiaryResponse.
    */
   public void testGetNumberOfEntries()
   {
      System.out.println("getNumberOfEntries");
      GetDiaryResponse instance = new GetDiaryResponse();
      int expResult = 20;

      try
      {
         instance.setNumberOfEntries(expResult);
         int result = instance.getNumberOfEntries();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setNumberOfEntries(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getOwnerFlag method, of class GetDiaryResponse.
    */
   public void testGetOwnerFlag()
   {
      System.out.println("getOwnerFlag");
      GetDiaryResponse instance = new GetDiaryResponse();
      char expResult = 'Y';

      try
      {
         instance.setOwnerFlag(expResult);
         char result = instance.getOwnerFlag();

         assertEquals(expResult, result);

         expResult = 'N';
         instance.setOwnerFlag(expResult);
         result = instance.getOwnerFlag();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setOwnerFlag('M');
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getPermissions method, of class GetDiaryResponse.
    * @throws Exception
    */
   public void testGetPermissions() throws Exception
   {
      System.out.println("getPermissions");
      GetDiaryResponse instance = new GetDiaryResponse();

      int expResult = 511;

      try
      {
         instance.setPermissions(expResult);
         int result = instance.getPermissions();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setPermissions(777);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getRevisionNo method, of class GetDiaryResponse.
    */
   public void testGetRevisionNo()
   {
      System.out.println("getRevisionNo");
      GetDiaryResponse instance = new GetDiaryResponse();
      int expResult = 0;
      try
      {
         instance.setRevisionNo(expResult);
         int result = instance.getRevisionNo();

         assertEquals(expResult, result);

      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setRevisionNo(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

}
