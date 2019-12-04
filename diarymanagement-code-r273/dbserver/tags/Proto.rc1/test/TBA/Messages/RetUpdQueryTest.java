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
public class RetUpdQueryTest extends TestCase {
    
    public RetUpdQueryTest(String testName) {
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
    * Test of Get method, of class RetUpdQuery.
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");
      String dummy = "";
      RetUpdQuery instance = new RetUpdQuery();
      String expResult = "";
      String result = instance.Get(dummy);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of GetDiaryID method, of class RetUpdQuery.
    */
   public void testGetDiaryID()
   {
      System.out.println("GetDiaryID");
      RetUpdQuery instance = new RetUpdQuery();
      int expResult = 20;

      try
      {
         instance.SetDiaryID(expResult);
         int result = instance.GetDiaryID();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetDiaryID(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getDiaryName method, of class RetUpdQuery.
    */
   public void testGetDiaryName()
   {
      System.out.println("getDiaryName");
      RetUpdQuery instance = new RetUpdQuery();
      String expResult = "";

      try
      {
         instance.setDiaryName(expResult);
         String result = instance.getDiaryName();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setDiaryName(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getPermissions method, of class RetUpdQuery.
    */
   public void testGetPermissions() throws Exception
   {
      System.out.println("getPermissions");
      RetUpdQuery instance = new RetUpdQuery();

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
    * Test of getRevisionNo method, of class RetUpdQuery.
    */
   public void testGetRevisionNo()
   {
      System.out.println("getRevisionNo");
      RetUpdQuery instance = new RetUpdQuery();
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

   /**
    * Test of getNumberOfEntries method, of class RetUpdQuery.
    */
   public void testGetNumberOfEntries()
   {
      System.out.println("getNumberOfEntries");
      RetUpdQuery instance = new RetUpdQuery();
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

}
