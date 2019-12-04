/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import junit.framework.TestCase;
import TBA.Exceptions.MessagesException;

/**
 *
 * @author cs321tx2
 */
public class RetUpdResponseTest extends TestCase {
    
    public RetUpdResponseTest(String testName) {
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
    * Test of RetUpdResponseeLen method, of class RetUpdResponse.
    */
/*   public void testRetUpdResponseLen()
   {
      System.out.println("RetUpdResponseeLen");
      RetUpdResponse instance = new RetUpdResponse();
      int expResult = 0;
      int result = instance.RetUpdResponseLen();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }*/

   /**
    * Test of Get method, of class RetUpdResponse.
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");
      String dummy = "";
      RetUpdResponse instance = new RetUpdResponse();
      String expResult = "";
      String result = instance.Get(dummy);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getDiaryName method, of class RetUpdResponse.
    */
   public void testGetDiaryName()
   {
      System.out.println("getDiaryName");
      RetUpdResponse instance = new RetUpdResponse();

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
    * Test of getNumberOfEntries method, of class RetUpdResponse.
    */
   public void testGetNumberOfEntries()
   {
      System.out.println("getNumberOfEntries");
      RetUpdResponse instance = new RetUpdResponse();
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
    * Test of getOwnerFlag method, of class RetUpdResponse.
    */
   public void testGetOwnerFlag()
   {
      System.out.println("getOwnerFlag");
      RetUpdResponse instance = new RetUpdResponse();

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
    * Test of getPermissions method, of class RetUpdResponse.
    */
   public void testGetPermissions() throws Exception
   {
      System.out.println("getPermissions");
      RetUpdResponse instance = new RetUpdResponse();

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
    * Test of getRevisionNo method, of class RetUpdResponse.
    */
   public void testGetRevisionNo()
   {
      System.out.println("getRevisionNo");
      RetUpdResponse instance = new RetUpdResponse();
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
