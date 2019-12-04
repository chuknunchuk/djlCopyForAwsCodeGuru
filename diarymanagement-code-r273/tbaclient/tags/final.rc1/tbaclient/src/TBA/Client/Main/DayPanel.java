package TBA.Client.Main;


import TBA.Data.Entry;
import TBA.Data.SessionState;
import TBA.Events.TBAEvent;
import TBA.Events.TBAEventListener;
import TBA.Images.ImageResources;
import TBA.Sounds.SoundResources;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JLabel;

/*
 * DayPanel.java
 *
 * Created on 18/08/2009, 2:08:14 PM
 */

/**
 * This DayPanel class provides an interface for a diary day. 5 of these can be
 * shown representing the working days in a week and each holds 12 hourPanels
 * representing hours of the day on screen.
 * @author Joe
 * @author Dan McGrath
 */
public class DayPanel extends javax.swing.JPanel
{
   private DayPanel previousPanel;
   private DayPanel nextPanel;
   private Vector<Entry> entries = null;
   private Vector<hourPanel> hourVector = new Vector<hourPanel>();
   private Calendar cal = Calendar.getInstance();

   /**
    * This method is to register for {@link TBA.Events.TBAEvent}
    * @param listener
    */
   public void addTBAEventListener(TBAEventListener listener)
   {
      listenerList.add(TBAEventListener.class, listener);
   }

   /**
    * This method is to unregister for {@link TBA.Events.TBAEvent}
    * @param listener
    */
   public void removeTBAEventListener(TBAEventListener listener)
   {
      listenerList.remove(TBAEventListener.class, listener);
   }

   /**
    * This private method is used to fire {@link TBA.Events.TBAEvent}
    */
   private void fireTBAEvent(TBAEvent evt)
   {
      Object[] listeners = listenerList.getListenerList();
      for (int listnerNum = 0; listnerNum < listeners.length; listnerNum += 2)
      {
         if (listeners[listnerNum] == TBAEventListener.class)
         { // First one is the class, second is the instance. Hence the + 1.
            ((TBAEventListener)listeners[listnerNum + 1]).TBAEventOccurred(evt);
         }
      }
   }

   /**
    * Sets imageResources for all the hourPanels in day
    * @param imageResources
    * @see tbaclient.hourPanel#setImageResources(TBA.Images.ImageResources)
    */
   public void setImageResources(ImageResources imageResources)
   {
      for(hourPanel aHour : hourVector)
      {
         aHour.setImageResources(imageResources);
      }
   }

   /**
    * mainclientview pushes down the soundresources into hourpanel through here
    * @param soundResources - from mainclientview
    */
   public void setSoundResources(SoundResources soundResources)
   {
       for(hourPanel aHour : hourVector)
       {
         aHour.setSoundResources(soundResources);
       }
   }


   /**
    * Sets the transparency drag panels that may need to get repainted.
    * @param fadeLeft Left transparency drag panel that may need to get repainted.
    * @param fadeRight Right transparency drag panel that may need to get repainted.
    * @see tbaclient.hourPanel#setFadeLeft(javax.swing.JLabel, javax.swing.JLabel)
    */
   public void setFadeLeft(JLabel fadeLeft, JLabel fadeRight)
   {
      for(hourPanel aHour : hourVector)
      {
         aHour.setFadeLeft(fadeLeft, fadeRight);
      }
   }

   /**
    * Creates new form DayPanel
    */
   public DayPanel()
   {
       initComponents();
       setHourVector();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDate = new javax.swing.JLabel();
        entryPanel1 = new TBA.Client.Main.hourPanel();
        entryPanel2 = new TBA.Client.Main.hourPanel();
        entryPanel3 = new TBA.Client.Main.hourPanel();
        entryPanel4 = new TBA.Client.Main.hourPanel();
        entryPanel5 = new TBA.Client.Main.hourPanel();
        entryPanel6 = new TBA.Client.Main.hourPanel();
        entryPanel7 = new TBA.Client.Main.hourPanel();
        entryPanel8 = new TBA.Client.Main.hourPanel();
        entryPanel9 = new TBA.Client.Main.hourPanel();
        entryPanel10 = new TBA.Client.Main.hourPanel();
        entryPanel11 = new TBA.Client.Main.hourPanel();
        entryPanel12 = new TBA.Client.Main.hourPanel();

        setPreferredSize(new java.awt.Dimension(145, 436));
        setLayout(new java.awt.GridLayout(0, 1));

        lblDate.setText("jLabel1");
        add(lblDate);

        entryPanel1.setBorder(null);
        add(entryPanel1);

        entryPanel2.setBorder(null);
        add(entryPanel2);

        entryPanel3.setBorder(null);
        add(entryPanel3);

        entryPanel4.setBorder(null);
        add(entryPanel4);

        entryPanel5.setBorder(null);
        add(entryPanel5);

        entryPanel6.setBorder(null);
        add(entryPanel6);

        entryPanel7.setBorder(null);
        add(entryPanel7);

        entryPanel8.setBorder(null);
        add(entryPanel8);

        entryPanel9.setBorder(null);
        add(entryPanel9);

        entryPanel10.setBorder(null);
        add(entryPanel10);

        entryPanel11.setBorder(null);
        add(entryPanel11);

        entryPanel12.setBorder(null);
        add(entryPanel12);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private TBA.Client.Main.hourPanel entryPanel1;
    private TBA.Client.Main.hourPanel entryPanel10;
    private TBA.Client.Main.hourPanel entryPanel11;
    private TBA.Client.Main.hourPanel entryPanel12;
    private TBA.Client.Main.hourPanel entryPanel2;
    private TBA.Client.Main.hourPanel entryPanel3;
    private TBA.Client.Main.hourPanel entryPanel4;
    private TBA.Client.Main.hourPanel entryPanel5;
    private TBA.Client.Main.hourPanel entryPanel6;
    private TBA.Client.Main.hourPanel entryPanel7;
    private TBA.Client.Main.hourPanel entryPanel8;
    private TBA.Client.Main.hourPanel entryPanel9;
    private javax.swing.JLabel lblDate;
    // End of variables declaration//GEN-END:variables


   /**
    * This method returns the entries in a day
    * @return entries - vector of entries
    */
   public Vector<Entry> getEntries()
   {
        return entries;
    }

   /**
   * Sets the entries of a day from a vector of entries
   * @param entries - vector of entries
   * @see tbaclient.hourPanel#setEntry(TBA.Data.Entry)
   */
   public void setEntries(Vector<Entry> entries)
   {
      int entryCount = -1;
      this.entries = entries;
      if (entries != null)
      {
         entryCount = entries.size();
      }
      for (int i = 0; i < hourVector.size(); i++)
      {
         if (i < entryCount)
         {
            hourVector.elementAt(i).setEntry(entries.get(i));
         }
         else
         {
            hourVector.elementAt(i).setEntry(null);
         }
      }
   }

    /**
     * This method returns the current date
     * @return cal
     */
    public Calendar getDate()
    {
        return cal;
    }

    /**
     * This method increases cal field by 1 day
     * @see #setDate(java.util.Calendar) 
     */
    public void increaseDate()
    {
        cal.add(Calendar.DAY_OF_YEAR, 1);
        setDate(cal);
    }

    /**
     * This method decreases cal field by 1 day
     * @see #setDate(java.util.Calendar)
     */
    public void decreaseDate()
    {
        cal.add(Calendar.DAY_OF_YEAR, -1);
        setDate(cal);
    }

    /**
     * Sets cal field and displays date of particular daypanel to tmpCal passed
     * from mainclientview onto lblDate and also passes down to the day's
     * hourpanels
     * @param tmpCal - from mainclientview and obtained from the previous day
     * when screen moved right, and from next if left
     */
    public void setDate(Calendar tmpCal)
    {
        String day, month, year, date;
        this.cal = tmpCal;
        Date tmp = tmpCal.getTime(); //to get strings to display on lblDate
        date = tmp.toString().substring(8, 10);
        month = tmp.toString().substring(4, 7);
        year = tmp.toString().substring(24, 28);
        day = tmp.toString().substring(0, 3);
        
        lblDate.setText(day+" "+date+"/"+month+"/"+year);

        for (hourPanel hrVecPan : hourVector) {
            hrVecPan.setCurrentDay(cal);
        }
        
    }

    private void setHourVector()
    {
        hourVector.add(entryPanel1);
        hourVector.add(entryPanel2);
        hourVector.add(entryPanel3);
        hourVector.add(entryPanel4);
        hourVector.add(entryPanel5);
        hourVector.add(entryPanel6);
        hourVector.add(entryPanel7);
        hourVector.add(entryPanel8);
        hourVector.add(entryPanel9);
        hourVector.add(entryPanel10);
        hourVector.add(entryPanel11);
        hourVector.add(entryPanel12);

        for (hourPanel ahourpanel : hourVector)
        {
            ahourpanel.addTBAEventListener(new TBAEventListener()
            {
               public void TBAEventOccurred(TBAEvent evt)
               {
                  fireTBAEvent(evt);
               }
            });
        }
    }

    /**
     * Returns next day panel
     * @return nextPanel field
     */
    public DayPanel getNextPanel()
   {
      return nextPanel;
   }

    /**
     * Sets next day panel
     * @param nextPanel field
     */
    public void setNextPanel(DayPanel nextPanel)
   {
      this.nextPanel = nextPanel;
   }

    /**
     * Return previous day panel
     * @return previousPanel field
     */
    public DayPanel getPreviousPanel()
   {
      return previousPanel;
   }

    /**
     * Sets the previous day panel
     * @param previousPanel field
     */
    public void setPreviousPanel(DayPanel previousPanel)
   {
      this.previousPanel = previousPanel;
   }

   /**
    * Give this class a pointer to the SessionState object.
    * @param session The SessionState
    */
   public void setSession(SessionState session)
   {
      for (hourPanel anHourPanel : hourVector)
      {
         anHourPanel.setSession(session);
      }
   }
}
