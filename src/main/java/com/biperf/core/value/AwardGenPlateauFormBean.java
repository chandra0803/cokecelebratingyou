
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * AwardGenPlateauFormBean
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>chowdhur</td>
 * <td>Jul 15, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenPlateauFormBean implements Serializable, Comparable
{
  private String plateauYear;
  private String plateauDays;
  private List<AwardGenPlateauValueBean> plateauValueBeanList = new ArrayList<AwardGenPlateauValueBean>();
  private boolean deleted = false;
  private Long awardInactiveAwardGenId;

  public AwardGenPlateauFormBean()
  {

  }

  public String getPlateauYear()
  {
    return plateauYear;
  }

  public void setPlateauYear( String plateauYear )
  {
    this.plateauYear = plateauYear;
  }

  public String getPlateauDays()
  {
    return plateauDays;
  }

  public void setPlateauDays( String plateauDays )
  {
    this.plateauDays = plateauDays;
  }

  public List<AwardGenPlateauValueBean> getPlateauValueBeanList()
  {
    return plateauValueBeanList;
  }

  public void setPlateauValueBeanList( List<AwardGenPlateauValueBean> plateauValueBeanList )
  {
    this.plateauValueBeanList = plateauValueBeanList;
  }

  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  public Long getAwardInactiveAwardGenId()
  {
    return awardInactiveAwardGenId;
  }

  public void setAwardInactiveAwardGenId( Long awardInactiveAwardGenId )
  {
    this.awardInactiveAwardGenId = awardInactiveAwardGenId;
  }

  public int getPlateauValueBeanListCount()
  {
    if ( plateauValueBeanList == null )
    {
      return 0;
    }

    return plateauValueBeanList.size();
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof AwardGenPlateauFormBean ) )
    {
      throw new ClassCastException( "A AwardGenPlateauFormBean was expected." );
    }
    AwardGenPlateauFormBean plateauValueFormBean = (AwardGenPlateauFormBean)object;
    String currentYear = this.getPlateauYear();
    String newYear = plateauValueFormBean.getPlateauYear();
    if ( StringUtils.isNotEmpty( currentYear ) && StringUtils.isNotEmpty( newYear ) )
    {
      int compare1 = new Long( currentYear ).compareTo( new Long( newYear ) );
      if ( compare1 != 0 )
      {
        return compare1;
      }
    }
    String currentDays = this.getPlateauDays();
    String newDays = plateauValueFormBean.getPlateauDays();
    if ( StringUtils.isNotEmpty( currentDays ) && StringUtils.isNotEmpty( newDays ) )
    {
      int compare2 = new Long( currentDays ).compareTo( new Long( newDays ) );
      if ( compare2 != 0 )
      {
        return compare2;
      }
    }

    return 0;
  }

  public AwardGenPlateauValueBean getPlateauValueBeanList( int index )
  {
    try
    {
      return (AwardGenPlateauValueBean)plateauValueBeanList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }
}
