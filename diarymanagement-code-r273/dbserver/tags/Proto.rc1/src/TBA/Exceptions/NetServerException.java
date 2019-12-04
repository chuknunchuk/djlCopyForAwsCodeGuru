/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class NetServerException extends Exception
{
   public NetServerException() {}

   public NetServerException(String message)
   {
      super(message);
   }

   public NetServerException(Throwable cause)
   {
      super(cause);
   }

   public NetServerException(String message, Throwable cause)
   {
      super(message, cause);
   }
}