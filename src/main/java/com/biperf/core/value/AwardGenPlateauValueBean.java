
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AwardGenPlateauValueBean
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
public class AwardGenPlateauValueBean implements Serializable, Comparable
{

  private Long levelId;
  private Long promoMerchCountryId;
  private String countryAssetKey;
  private List<PromoMerchProgramLevel> levelList = new ArrayList<PromoMerchProgramLevel>();
  private Long id;

  public AwardGenPlateauValueBean()
  {

  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public List<PromoMerchProgramLevel> getLevelList()
  {
    return levelList;
  }

  public void setLevelList( List<PromoMerchProgramLevel> levelList )
  {
    this.levelList = levelList;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof AwardGenPlateauValueBean ) )
    {
      throw new ClassCastException( "A AwardGenPlateauValueBean was expected." );
    }

    AwardGenPlateauValueBean plateauValueBean = (AwardGenPlateauValueBean)object;

    String cmAssetCode1 = this.getCountryAssetKey();
    String cmString1 = ContentReaderManager.getText( cmAssetCode1, "COUNTRY_NAME" );

    String cmAssetCode2 = plateauValueBean.getCountryAssetKey();
    String cmString2 = ContentReaderManager.getText( cmAssetCode2, "COUNTRY_NAME" );
    return cmString1.compareTo( cmString2 );
  }

  public String getCountryAssetKey()
  {
    return countryAssetKey;
  }

  public void setCountryAssetKey( String countryAssetKey )
  {
    this.countryAssetKey = countryAssetKey;
  }

  public int getLevelListCount()
  {
    if ( levelList == null )
    {
      return 0;
    }

    return levelList.size();
  }

  public PromoMerchProgramLevel getLevelList( int index )
  {
    try
    {
      return (PromoMerchProgramLevel)levelList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public Long getPromoMerchCountryId()
  {
    return promoMerchCountryId;
  }

  public void setPromoMerchCountryId( Long promoMerchCountryId )
  {
    this.promoMerchCountryId = promoMerchCountryId;
  }

}
