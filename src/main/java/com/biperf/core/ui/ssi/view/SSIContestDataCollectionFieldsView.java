
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutObjectivesContestView.
 * 
 * @author patelp
 * @since May 12, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestDataCollectionFieldsView
{
  private Long id;
  private String label;
  private String name;
  private String type;
  private String typeDisplay;
  private Boolean isRequired;
  private Boolean isSelected;
  private Integer sequenceNumber;
  private String metaType;
  private String fieldGroup;
  private String subType;
  private String value;
  private String description;
  private String maxFileSize;
  private String supportedFileTypes;

  private List<SelectView> selectList = new ArrayList<SelectView>();

  private static final String REQUIRED = "required";
  private static final String PREDEFINED = "predefined";
  private static final String FIELD = "field";

  private static final Map<String, String> elementTypeMap = new HashMap<String, String>();

  static
  {
    elementTypeMap.put( "file", "file" );
    elementTypeMap.put( "date", "date" );
    elementTypeMap.put( "number", "number" );
    elementTypeMap.put( "text", "text" );
    elementTypeMap.put( "text_box", "textarea" );
    elementTypeMap.put( "address_block", "address" );
  }

  public SSIContestDataCollectionFieldsView()
  {

  }

  // used for creator while creating data collection step
  public SSIContestDataCollectionFieldsView( ClaimFormStepElement element, int sequence )
  {
    this.id = element.getId();
    this.label = element.getI18nLabel();
    this.name = element.getI18nLabel();
    this.type = elementTypeMap.get( element.getClaimFormElementType().getCode() );
    this.typeDisplay = elementTypeMap.get( element.getClaimFormElementType().getCode() ) + " " + FIELD;
    this.isRequired = element.isRequired();
    this.isSelected = element.isRequired();
    this.sequenceNumber = sequence;
    this.metaType = element.isRequired() ? REQUIRED : PREDEFINED;
  }

  // used for participant while submitting claim form
  public SSIContestDataCollectionFieldsView( SSIContest contest, SSIContestClaimField claimField )
  {
    if ( ! ( contest.getContestType().isDoThisGetThat() && ( SSIContestUtil.CLAIM_FIELD_QUANTITY.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() )
        || SSIContestUtil.CLAIM_FIELD_AMOUNT.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() ) ) ) )
    {
      this.id = claimField.getId();
      this.isRequired = claimField.isRequired();
      this.sequenceNumber = claimField.getSequenceNumber();
      this.type = elementTypeMap.get( claimField.getFormElement().getClaimFormElementType().getCode() );
      if ( SSIContestUtil.CLAIM_FIELD_AMOUNT.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() ) )
      {
        this.label = claimField.getFormElement().getI18nLabel() + " (in " + contest.getActivityMeasureCurrencyCode().toUpperCase() + ")";
      }
      else
      {
        this.label = claimField.getFormElement().getI18nLabel();
      }
      this.name = claimField.getFormElement().getI18nLabel();
      if ( claimField.getFormElement().getClaimFormElementType().isFile() )
      {
        this.maxFileSize = claimField.getFormElement().getFileSize() + "MB";
        this.supportedFileTypes = claimField.getFormElement().getFileType();
      }
    }
  }

  // used for participant while submitting claim form, for address
  public SSIContestDataCollectionFieldsView( SSIContestClaimField claimField, int sequence, List<Country> countries )
  {
    this.id = claimField.getId();
    this.isRequired = claimField.isRequired();
    this.sequenceNumber = claimField.getSequenceNumber();
    this.fieldGroup = SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS;
    if ( sequence == 1 )
    {
      // select input for country
      this.type = "select";
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.COUNRTY_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY;
      for ( Country country : countries )
      {
        this.selectList.add( new SelectView( country.getI18nCountryName(), country.getCountryCode() ) );
      }
    }
    else if ( sequence == 2 || sequence == 3 || sequence == 4 || sequence == 5 )
    {
      // text input for address1, address2, address3 & city
      this.type = "text";
    }
    if ( sequence == 2 )
    {
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR1_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1;
    }
    else if ( sequence == 3 )
    {
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR2_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS2;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS2;
      this.isRequired = false; // overriding
    }
    else if ( sequence == 4 )
    {
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR3_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS3;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS3;
      this.isRequired = false; // overriding
    }
    else if ( sequence == 5 )
    {
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.CITY_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY;
    }
    else if ( sequence == 6 )
    {
      // select input for state
      this.type = "select";
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.STATE_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE;
      for ( Country country : countries )
      {
        if ( Country.CANADA.equals( country.getCountryCode() ) || Country.MEXICO.equals( country.getCountryCode() ) || Country.UNITED_STATES.equals( country.getCountryCode() ) )
        {
          List<StateType> states = StateType.getList( country.getCountryCode() );
          for ( StateType state : states )
          {
            this.selectList.add( new SelectView( state.getName(), state.getCode(), country.getCountryCode() ) );
          }
        }
      }
    }
    else if ( sequence == 7 )
    {
      // text input for postal code
      this.type = "text";
      this.label = CmsResourceBundle.getCmsBundle().getString( "participant.address.POSTAL_CODE_LABEL" );
      this.name = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE;
      this.subType = SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE;
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getTypeDisplay()
  {
    return typeDisplay;
  }

  public void setTypeDisplay( String typeDisplay )
  {
    this.typeDisplay = typeDisplay;
  }

  @JsonProperty( "isRequired" )
  public Boolean getIsRequired()
  {
    return isRequired;
  }

  public void setIsRequired( Boolean isRequired )
  {
    this.isRequired = isRequired;
  }

  @JsonProperty( "isSelected" )
  public Boolean getIsSelected()
  {
    return isSelected;
  }

  public void setIsSelected( Boolean isSelected )
  {
    this.isSelected = isSelected;
  }

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public String getMetaType()
  {
    return metaType;
  }

  public void setMetaType( String metaType )
  {
    this.metaType = metaType;
  }

  public String getFieldGroup()
  {
    return fieldGroup;
  }

  public void setFieldGroup( String fieldGroup )
  {
    this.fieldGroup = fieldGroup;
  }

  public String getSubType()
  {
    return subType;
  }

  public void setSubType( String subType )
  {
    this.subType = subType;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getMaxFileSize()
  {
    return maxFileSize;
  }

  public void setMaxFileSize( String maxFileSize )
  {
    this.maxFileSize = maxFileSize;
  }

  public String getSupportedFileTypes()
  {
    return supportedFileTypes;
  }

  public void setSupportedFileTypes( String supportedFileTypes )
  {
    this.supportedFileTypes = supportedFileTypes;
  }

  public List<SelectView> getSelectList()
  {
    return selectList;
  }

  public void setSelectList( List<SelectView> selectList )
  {
    this.selectList = selectList;
  }

  public class SelectView
  {
    private String name;
    private String id;
    private String countryCode;

    public SelectView()
    {

    }

    public SelectView( String name, String id )
    {
      this.name = name;
      this.id = id;
    }

    public SelectView( String name, String id, String countryCode )
    {
      this.name = name;
      this.id = id;
      this.countryCode = countryCode;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getCountryCode()
    {
      return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
      this.countryCode = countryCode;
    }

  }
}
