/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Client.GUI;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 *
 * @author cs321tx2
 */
public class jImageLayeredPane extends JLayeredPane
{
   private ImageIcon backgroundImage;

   public jImageLayeredPane()
   {
      backgroundImage = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/BackgroundMain.png"));
   }

   @Override
   public void paintComponent(Graphics g)
   {
      int width = getWidth();
      int height = getHeight();
      int imageW = backgroundImage.getIconWidth();
      int imageH = backgroundImage.getIconHeight();

      // Tile the image to fill our area.
      for (int x = 0; x < width; x += imageW) {
        for (int y = 0; y < height; y += imageH) {
            g.drawImage(backgroundImage.getImage(), x, y, this);
        }
      }
   }
}
