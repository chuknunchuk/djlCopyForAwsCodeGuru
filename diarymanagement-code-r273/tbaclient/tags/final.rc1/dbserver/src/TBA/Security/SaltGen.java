/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Security;

import java.util.Random;

/**
 *
 * @author Kyle
 */
public class SaltGen {

  public String SaltGen(int length) {
        String salt = null;
        Random random = new Random();
        StringBuilder strbld = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strbld.append(Integer.toHexString(random.nextInt()));
        }
        salt = strbld.toString();
        return salt;
    }
}
