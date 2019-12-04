/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//compile hates this file, doesnt find junit import

package TBA.Messages;

import junit.framework.TestCase;
import TBA.Exceptions.MessagesException;

/**
 *
 * @author cs321tx2
 */
public class DiaryEntryTest extends TestCase {
    
    public DiaryEntryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /**
    * Test of GetEntrySize method, of class DiaryEntry.
    */
  /* public void testGetEntrySize()
   {
      System.out.println("GetEntrySize");
      DiaryEntry instance = new DiaryEntry();
      int expResult = 0;
      int result = instance.GetEntrySize();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }*/

   /**
    * Test of Get method, of class DiaryEntry.
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");
      DiaryEntry setInstance = new DiaryEntry();
      DiaryEntry getInstance = new DiaryEntry();

      String expResult = "";

      int expEntryID = 2;
      int resEntryID; //result entryID


      String expStartDate = "20091014";
      String resStartDate;

      String expEndDate = "20091014";
      String resEndDate;

      String expStartTime = "1300";
      String resStartTime;

      String expEndTime = "1400";
      String resEndTime;

      char expOwnerFlag = 'Y';
      char resOwnerFlag;

      char expLockedFlag = 'Y';
      char resLockedFlag;

      String expUser = "bob";
      String resUser;
      
      String expTitle = "title";
      String resTitle;

      String Body = "superMeetinglify";
      String resBody;

      try
      {
         setInstance.setEntryID(expEntryID);
         setInstance.setStartDate(expStartDate);
         setInstance.setEndDate(expEndDate);
         setInstance.setStartTime(expStartTime);
         setInstance.setEndTime(expEndTime);
         setInstance.setOwnerFlag(expOwnerFlag);
         setInstance.setLockedFlag(expLockedFlag);
         setInstance.setCreatingUser(expUser);
         setInstance.setTitle(expTitle);
         setInstance.setBody(Body);


         getInstance.Set(setInstance.Get());

         resEntryID = getInstance.getEntryID();
         resStartDate = getInstance.getStartDate();
         resEndDate = getInstance.getEndDate();
         resStartTime = getInstance.getStartTime();
         resEndTime = getInstance.getEndTime();
         resOwnerFlag = getInstance.getOwnerFlag();
         resLockedFlag = getInstance.getLockedFlag();
         resUser = getInstance.getCreatingUser();
         resTitle = getInstance.getTitle();
         resBody = getInstance.getBody();

         assertEquals(expEntryID,resEntryID);
         assertEquals(expStartDate,resStartDate);
         assertEquals(expEndDate,resEndDate);
         assertEquals(expStartTime,resStartTime);
         assertEquals(expEndTime,resEndTime);
         assertEquals(expOwnerFlag,resOwnerFlag);
         assertEquals(expLockedFlag,resLockedFlag);
         assertEquals(expUser,resUser);
         System.out.println(resBody);
         //assertEquals(Body,resBody);
        
      } catch (MessagesException ex) {
         fail("Unexpected throw!");
      }


        try
            {
               getInstance = new DiaryEntry();

               String failResult = getInstance.Get();

               fail("Expected throw, but did not receive!");
            }
        catch (MessagesException ex)
        { 
        }

   }

   
   /**
    * Test of getBody method, of class DiaryEntry.
    */
   public void testGetBody()
   {
      System.out.println("getBody");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setBody(expResult);
         String result = instance.getBody();
         assertEquals(expResult, result);
      } catch( MessagesException ex) {
          fail("Unexpected throw!");
      }

      //TODO: testGetBody, maybe for null
   }

   /**
    * Test of getCreatingUser method, of class DiaryEntry.
    */
   public void testGetCreatingUser()
   {
      System.out.println("getCreatingUser");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setCreatingUser(expResult);
         String result = instance.getCreatingUser();
         assertEquals(expResult, result);
      } catch (MessagesException ex) {
         fail("unexpected throw!");
      }

      try
      {
         instance.setCreatingUser(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getEndDate method, of class DiaryEntry.
    */
   public void testGetEndDate()
   {
      System.out.println("getEndDate");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setEndDate(expResult);
         String result = instance.getEndDate();
         assertEquals(expResult, result);
      } catch (MessagesException ex) {
         fail("unexpected throw!");
      }
     
      try
      {
         instance.setEndDate(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getEndTime method, of class DiaryEntry.
    */
   public void testGetEndTime()
   {
      System.out.println("getEndTime");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setEndTime(expResult);
         String result = instance.getEndTime();
         assertEquals(expResult, result);
      } catch (MessagesException ex) {
         fail("unexpected throw!");
      }

      try
      {
         instance.setEndTime(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getEntryID method, of class DiaryEntry.
    */
   public void testGetEntryID()
   {
      System.out.println("getEntryID");
      DiaryEntry instance = new DiaryEntry();
      int expResult = 20;

      try
      {
         instance.setEntryID(expResult);
         int result = instance.getEntryID();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setEntryID(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getLockedFlag method, of class DiaryEntry.
    */
   public void testGetLockedFlag()
   {
      System.out.println("getLockedFlag");
      DiaryEntry instance = new DiaryEntry();
      char expResult = 'Y';

      try
      {
         instance.setLockedFlag(expResult);
         char result = instance.getLockedFlag();

         assertEquals(expResult, result);

         expResult = 'N';
         instance.setLockedFlag(expResult);
         result = instance.getLockedFlag();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setLockedFlag('M');
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getOwnerFlag method, of class DiaryEntry.
    */
   public void testGetOwnerFlag()
   {
      System.out.println("getOwnerFlag");
      DiaryEntry instance = new DiaryEntry();
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
    * Test of getStartDate method, of class DiaryEntry.
    */
   public void testGetStartDate()
   {
      System.out.println("getStartDate");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setStartDate(expResult);
         String result = instance.getStartDate();
         assertEquals(expResult, result);
      } catch (MessagesException ex) {
         fail("unexpected throw!");
      }

      try
      {
         instance.setStartDate(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getStartTime method, of class DiaryEntry.
    */
   public void testGetStartTime()
   {
      System.out.println("getStartTime");
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setStartTime(expResult);
         String result = instance.getStartTime();
         assertEquals(expResult, result);
      } catch (MessagesException ex) {
         fail("unexpected throw!");
      }

      try
      {
         instance.setStartTime(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getTitle method, of class DiaryEntry.
    */
   public void testGetTitle()
   {
      DiaryEntry instance = new DiaryEntry();
      String expResult = "";
      try
      {
         instance.setTitle(expResult);
         String result = instance.getTitle();
         assertEquals(expResult, result);
      } catch( MessagesException ex) {
          fail("Unexpected throw!");
      }

      try
      {
         instance.setTitle(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

}
