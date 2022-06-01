
package com.biperf.core.ui.ots;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.ots.OTSBillCode;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.BatchDescription;
import com.biperf.core.value.ots.v1.program.Program;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;

public class OTSBillCodesForm extends BaseForm
{
  public static final String SESSION_KEY = "otsBillCodesForm";
  public static final String DEPT_NAME = "department";
  public static final String ORG_UNIT_NAME = "orgUnitName";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String LOGIN_ID = "userName";

  private String programNumber;
  private String batchNumber;

  private boolean billCodesActive;

  private String billCode1;
  private String customValue1;
  private String billCode2;
  private String customValue2;
  private String billCode3;
  private String customValue3;
  private String billCode4;
  private String customValue4;
  private String billCode5;
  private String customValue5;
  private String billCode6;
  private String customValue6;
  private String billCode7;
  private String customValue7;
  private String billCode8;
  private String customValue8;
  private String billCode9;
  private String customValue9;
  private String billCode10;
  private String customValue10;
  private List<BatchDescription> translationsTextList;
  private String method;
  private List<BatchDescription> batchDescriptions;

  public static final String CUSTOM_VALUE = "customValue";
  public static final String USER_NAME = "userName";

  @SuppressWarnings( "unchecked" )
  public void load( Batch batch )
  {
    this.setBatchNumber( batch.getBatchNumber() );

    // this.billCodesActive = batch.isBillCodesActive();
    if ( ( batch.getOTSBillCodes().size() > 0 ) || ( this.translationsTextList.size() > 0 ) )
    {
      this.billCodesActive = true;
    }
    List<OTSBillCode> otsBillCodes = null;

    if ( this.billCodesActive )
    {
      otsBillCodes = new ArrayList<OTSBillCode>();

      for ( OTSBillCode otsBillCode : batch.getBillCodes() )
      {
        otsBillCodes.add( otsBillCode );
      }
      loadOTSBillCodes( otsBillCodes );

    }
    List<Content> localeItems = getCMAssetService().getSupportedLocales( true );
    List<BatchDescription> nonEmptyDescriptions = new ArrayList<>();
    for ( BatchDescription b : batch.getBatchDescription() )
    {
      if ( !Objects.isNull( b.getCmText() ) && b.getCmText().length() > 0 )
      {
        for ( Content content : localeItems )
        {
          if ( b.getLocale().equalsIgnoreCase( (String)content.getContentDataMap().get( "CODE" ) ) )
          {
            b.setDisplayName( new String( content.getContentDataMap().get( "DESC" ).toString().getBytes(), Charset.forName( "UTF-8" ) ) );
            nonEmptyDescriptions.add( b );
            break;
          }
        }
        if ( b.getLocale().equalsIgnoreCase( LanguageType.ENGLISH ) )
        {
          b.setDisplayName( new String( UserManager.getDefaultLocale().getDisplayName().toString().getBytes(), Charset.forName( "UTF-8" ) ) );
          nonEmptyDescriptions.add( b );
        }

      }
    }
    setBatchDescriptions( nonEmptyDescriptions );

    this.translationsTextList = getBatchDescriptions();
    BatchDescription description = null;
    int count = 0;
    for ( Content content : localeItems )
    {
      String localeCode = (String)content.getContentDataMap().get( "CODE" );

      String localeDesc = (String)content.getContentDataMap().get( "DESC" ); // displayName
      byte[] ptext = localeDesc.getBytes();
      String value = new String( ptext, Charset.forName( "UTF-8" ) );
      if ( value.contains( "??" ) )
      {
        value = localeDesc;
      }
      description = getBatchDescriptionByLocale( getBatchDescriptions(), localeCode, translationsTextList );
      if ( Objects.isNull( description ) )
      {
        description = new BatchDescription();
        description.setLocale( localeCode );
        description.setDisplayName( value );
        description.setCmText( null );
        description.setCount( count );
        count = count + 1;

        this.translationsTextList.add( description );

      }

      description.setDisplayName( value );

    }

  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {

    translationsTextList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "translationListCount" ) );
  }

  private List<BatchDescription> getEmptyValueList( int valueListCount )
  {
    List<BatchDescription> valueList = new ArrayList<BatchDescription>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      BatchDescription batchDescription = new BatchDescription();
      valueList.add( batchDescription );
    }
    return valueList;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( !this.billCodesActive )
    {
      return errors;
    }

    List<String> billCodes = Arrays.asList( this.billCode1,
                                            this.billCode2,
                                            this.billCode3,
                                            this.billCode4,
                                            this.billCode5,
                                            this.billCode6,
                                            this.billCode7,
                                            this.billCode8,
                                            this.billCode9,
                                            this.billCode10 );

    if ( billCodes.stream().filter( billCode -> !StringUtils.isEmpty( billCode ) ).count() == 0 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.bill.code.NO_BILL_CODES" ) );
    }
    else
    {

      // Checks if custom bill code is selected but value is empty
      if ( checkCustomBillCodeMissing( this.billCode1, this.customValue1 ) || checkCustomBillCodeMissing( this.billCode2, this.customValue2 )
          || checkCustomBillCodeMissing( this.billCode3, this.customValue3 ) || checkCustomBillCodeMissing( this.billCode4, this.customValue4 )
          || checkCustomBillCodeMissing( this.billCode5, this.customValue5 ) || checkCustomBillCodeMissing( this.billCode6, this.customValue6 )
          || checkCustomBillCodeMissing( this.billCode7, this.customValue7 ) || checkCustomBillCodeMissing( this.billCode8, this.customValue8 )
          || checkCustomBillCodeMissing( this.billCode9, this.customValue9 ) || checkCustomBillCodeMissing( this.billCode10, this.customValue10 ) )
      {
        errors.add( "otsBillCodes", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_BILL_CODE" ) ) );
      }
    }

    // Validate max length of each custom input field
    validateCustomValue( errors, customValue1, "promotion.bill.code.BILL_CODE_1" );
    validateCustomValue( errors, customValue2, "promotion.bill.code.BILL_CODE_2" );
    validateCustomValue( errors, customValue3, "promotion.bill.code.BILL_CODE_3" );
    validateCustomValue( errors, customValue4, "promotion.bill.code.BILL_CODE_4" );
    validateCustomValue( errors, customValue5, "promotion.bill.code.BILL_CODE_5" );
    validateCustomValue( errors, customValue6, "promotion.bill.code.BILL_CODE_6" );
    validateCustomValue( errors, customValue7, "promotion.bill.code.BILL_CODE_7" );
    validateCustomValue( errors, customValue8, "promotion.bill.code.BILL_CODE_8" );
    validateCustomValue( errors, customValue9, "promotion.bill.code.BILL_CODE_9" );
    validateCustomValue( errors, customValue10, "promotion.bill.code.BILL_CODE_10" );
    request.setAttribute( "batchNumber", this.batchNumber );
    Program pgm = new Program();
    pgm.setProgramNumber( programNumber );
    request.setAttribute( "otsProgramDetails", pgm );

    return errors;
  }

  private boolean checkCustomBillCodeMissing( String billCode, String customValue )
  {
    return !StringUtils.isEmpty( billCode ) && billCode.equalsIgnoreCase( CUSTOM_VALUE ) && StringUtils.isEmpty( customValue );
  }

  private void validateCustomValue( ActionErrors errors, String value, String billCodeKey )
  {
    int CUSTOM_INPUT_LENGTH = 25;

    if ( value != null && value.length() > CUSTOM_INPUT_LENGTH )
    {
      errors.add( "otsBillCodeCustomValue",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_MAXLENGTH,
                                     CmsResourceBundle.getCmsBundle().getString( billCodeKey ) + ": " + CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_VALUE" ),
                                     String.valueOf( CUSTOM_INPUT_LENGTH ) ) );
    }
  }

  public Batch toDomainObject( Batch batch )
  {
    batch.setBillCodesActive( this.billCodesActive );
    if ( this.billCodesActive )
    {
      batch.setBillCodes( getOTSBillCodeList( batch ) );
      batch.setOTSBillCodes( getOTSBillCodeList( batch ) );
    }

    batch.setBatchDescription( translationsTextList );

    return batch;
  }

  private List<OTSBillCode> getOTSBillCodeList( Batch batch )
  {
    List<OTSBillCode> otsBillCodes = new ArrayList<OTSBillCode>();
    if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 1 ), this.billCode1, this.customValue1 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 1 ), null, null ) );
    }
    if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 2 ), this.billCode2, this.customValue2 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 2 ), null, null ) );
    }
    if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 3 ), this.billCode3, this.customValue3 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 3 ), null, null ) );
    }
    if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 4 ), this.billCode4, this.customValue4 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 4 ), null, null ) );
    }
    if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 5 ), this.billCode5, this.customValue5 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 5 ), null, null ) );
    }
    if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 6 ), this.billCode6, this.customValue6 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 6 ), null, null ) );
    }
    if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 7 ), this.billCode7, this.customValue7 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 7 ), null, null ) );
    }
    if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 8 ), this.billCode8, this.customValue8 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 8 ), null, null ) );
    }
    if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
    {

      otsBillCodes.add( new OTSBillCode( batch, new Long( 9 ), this.billCode9, this.customValue9 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 9 ), null, null ) );
    }
    if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
    {

      otsBillCodes.add( new OTSBillCode( batch, new Long( 10 ), this.billCode10, this.customValue10 ) );
    }
    else
    {
      otsBillCodes.add( new OTSBillCode( batch, new Long( 10 ), null, null ) );
    }
    return otsBillCodes;
  }

  private void loadOTSBillCodes( List<OTSBillCode> otsBillCodes )
  {
    if ( otsBillCodes != null && otsBillCodes.size() > 0 )
    {
      Iterator otsBillCodesList = otsBillCodes.iterator();
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode1 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode1 != null && !Objects.isNull( otsBillCode1.getBillCode() ) )
        {
          this.billCode1 = otsBillCode1.getBillCode();
          this.customValue1 = otsBillCode1.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode2 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode2 != null && !Objects.isNull( otsBillCode2.getBillCode() ) )
        {
          this.billCode2 = otsBillCode2.getBillCode();
          this.customValue2 = otsBillCode2.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode3 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode3 != null && !Objects.isNull( otsBillCode3.getBillCode() ) )
        {
          this.billCode3 = otsBillCode3.getBillCode();
          this.customValue3 = otsBillCode3.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode4 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode4 != null && !Objects.isNull( otsBillCode4.getBillCode() ) )
        {
          this.billCode4 = otsBillCode4.getBillCode();

          this.customValue4 = otsBillCode4.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode5 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode5 != null && !Objects.isNull( otsBillCode5.getBillCode() ) )
        {
          this.billCode5 = otsBillCode5.getBillCode();
          this.customValue5 = otsBillCode5.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode6 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode6 != null && !Objects.isNull( otsBillCode6.getBillCode() ) )
        {
          this.billCode6 = otsBillCode6.getBillCode();
          this.customValue6 = otsBillCode6.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode7 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode7 != null && !Objects.isNull( otsBillCode7.getBillCode() ) )
        {
          this.billCode7 = otsBillCode7.getBillCode();
          this.customValue7 = otsBillCode7.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode8 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode8 != null && !Objects.isNull( otsBillCode8.getBillCode() ) )
        {
          this.billCode8 = otsBillCode8.getBillCode();
          this.customValue8 = otsBillCode8.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode9 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode9 != null && !Objects.isNull( otsBillCode9.getBillCode() ) )
        {
          this.billCode9 = otsBillCode9.getBillCode();
          this.customValue9 = otsBillCode9.getCustomValue();

        }
      }
      if ( otsBillCodesList.hasNext() )
      {
        OTSBillCode otsBillCode10 = (OTSBillCode)otsBillCodesList.next();
        if ( otsBillCode10 != null && !Objects.isNull( otsBillCode10.getBillCode() ) )
        {
          this.billCode10 = otsBillCode10.getBillCode();
          this.customValue10 = otsBillCode10.getCustomValue();

        }
      }
    }
  }

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getCustomValue1()
  {
    return customValue1;
  }

  public void setCustomValue1( String customValue1 )
  {
    this.customValue1 = customValue1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getCustomValue2()
  {
    return customValue2;
  }

  public void setCustomValue2( String customValue2 )
  {
    this.customValue2 = customValue2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getCustomValue3()
  {
    return customValue3;
  }

  public void setCustomValue3( String customValue3 )
  {
    this.customValue3 = customValue3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getCustomValue4()
  {
    return customValue4;
  }

  public void setCustomValue4( String customValue4 )
  {
    this.customValue4 = customValue4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getCustomValue5()
  {
    return customValue5;
  }

  public void setCustomValue5( String customValue5 )
  {
    this.customValue5 = customValue5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getCustomValue6()
  {
    return customValue6;
  }

  public void setCustomValue6( String customValue6 )
  {
    this.customValue6 = customValue6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getCustomValue7()
  {
    return customValue7;
  }

  public void setCustomValue7( String customValue7 )
  {
    this.customValue7 = customValue7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getCustomValue8()
  {
    return customValue8;
  }

  public void setCustomValue8( String customValue8 )
  {
    this.customValue8 = customValue8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getCustomValue9()
  {
    return customValue9;
  }

  public void setCustomValue9( String customValue9 )
  {
    this.customValue9 = customValue9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

  public String getCustomValue10()
  {
    return customValue10;
  }

  public void setCustomValue10( String customValue10 )
  {
    this.customValue10 = customValue10;
  }

  public String getBatchNumber()
  {
    return batchNumber;
  }

  public void setBatchNumber( String batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public List<BatchDescription> getTranslationsTextList()
  {
    return translationsTextList;
  }

  public void setTranslationsTextList( List<BatchDescription> translationsTextList )
  {
    this.translationsTextList = translationsTextList;
  }

  public List<BatchDescription> getBatchDescriptions()
  {
    return batchDescriptions;
  }

  public void setBatchDescriptions( List<BatchDescription> batchDescriptions )
  {
    this.batchDescriptions = batchDescriptions;
  }

  public int getTranslationListCount()
  {
    if ( this.translationsTextList == null )
    {
      return 0;
    }

    return this.translationsTextList.size();
  }

  public static String getJsString( String input )
  {
    String output = null;
    if ( input != null )
    {
      output = input.replace( "\'", "\\'" );
      output = output.replace( "\"", "\\\"" );
      output = output.replace( "\r", "\\r" );
      output = output.replace( "\n", "\\n" );
    }
    return output;
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

  private BatchDescription getBatchDescriptionByLocale( List<BatchDescription> descriptions, String locale, List<BatchDescription> translationsTextList2 )
  {
    BatchDescription description = null;
    description = descriptions.stream().filter( b -> b.getLocale().equals( locale ) ).findFirst().orElse( null );
    if ( Objects.isNull( description ) )
    {
      return null;
    }

    return description;
  }

  public BatchDescription getTranslationList( int index )
  {
    try
    {
      return (BatchDescription)translationsTextList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  private OTSService getOTSService()
  {
    return (OTSService)BeanLocator.getBean( OTSService.BEAN_NAME );
  }

  private OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)BeanLocator.getBean( OTSProgramService.BEAN_NAME );
  }
}
