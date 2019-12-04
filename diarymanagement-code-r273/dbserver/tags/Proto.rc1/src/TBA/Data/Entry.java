/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TBA.Data;

import java.util.Calendar;

/**
 * This class serves as the interface to the data of a single Entry.
 *<p>
 * @author Dan Mcgrath
 * @author Joe Neasy
 *
 * @version $Rev:: 97            $ $Date:: 2009-09-06 #$
 */
public class Entry implements Comparable<Entry>
{

    private String subject;
    private String body;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private Calendar start = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();

   public Calendar getEnd()
   {
      return end;
   }

   public void setEnd(Calendar end)
   {
      this.end = end;
   }

   public Calendar getStart()
   {
      return start;
   }

   public void setStart(Calendar start)
   {
      this.start = start;
   }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Deprecated
    public String getEndDate() {
        return endDate;
    }

    @Deprecated
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Deprecated
    public String getEndTime() {
        return endTime;
    }

    @Deprecated
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Deprecated
    public String getStartDate() {
        return startDate;
    }

    @Deprecated
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Deprecated
    public String getStartTime() {
        return startTime;
    }

    @Deprecated
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public boolean during(Calendar duringDate)
    {
       boolean afterStart = start.after(duringDate) || start.equals(duringDate);
       boolean beforeEnd = end.before(duringDate) || end.equals(duringDate);

       return afterStart && beforeEnd;
    }

   public int compareTo(Entry other)
   {
      return start.compareTo(other.getStart());
   }
}