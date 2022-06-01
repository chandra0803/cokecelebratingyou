/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepElementItem.java,v $
 *
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.claim.HierarchyUniqueConstraintEnum;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.DynaPickListType;

/**
 * ClaimFormStepElementItem. This is a holder class for the list of items in a particualar
 * ClaimFormStepElement type. <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ClaimFormStepElementItem
{
  private String labelKey;
  private boolean required;
  private String inputType;
  private String formProperty;
  private String pickList;
  private List enumList;

  public static List NUMBER_FIELD_ITEMS = new ArrayList();
  public static List TEXT_FIELD_ITEMS = new ArrayList();
  public static List TEXT_BOX_FIELD_ITEMS = new ArrayList();
  public static List SECTION_HEADING_ITEMS = new ArrayList();
  public static List COPY_BLOCK = new ArrayList();
  public static List LINK = new ArrayList();
  public static List DATE_FIELD = new ArrayList();
  public static List BOOLEAN_SELECTION = new ArrayList();
  public static List SINGLE_SELECTION = new ArrayList();
  public static List MULTI_SELECTION = new ArrayList();
  public static List BUTTON = new ArrayList();
  public static List ADDRESS_BOOK_SELECTION = new ArrayList();
  public static List BOOLEAN_CHECKBOX = new ArrayList();
  public static List ADDRESS_BLOCK = new ArrayList();
  public static List FILE_FIELD_ITEMS = new ArrayList();
  private static Map itemListMap = new HashMap();

  private static String TEXT_FIELD = "text";
  private static String TEXT_BOX_FIELD = "text_box";
  private static String SELECT_FIELD = "select";
  private static String TEXT_AREA_FIELD = "textarea";
  private static String YES_NO_RADIO_FIELD = "yesNoRadio";
  private static String ENUM_LIST_RADIO_FIELD = "enumListRadio";
  private static String SELECT_PICKLIST_FIELD = "selectPickList";
  private static String NUMBER_FIELD = "number";

  static
  {
    // Create lists for all the ClaimFormStepElementTypes of ClaimFormStepElementItems. These items
    // define what is displayed
    // on the create, update and view pages.
    // The parameters are as follows:
    // Param1: the CMKey for the item label
    // Param2: boolean for if this item is required
    // Param3: the type of this item - text, textarea, selection, radio....
    // Param4: form property name for this item. Same as how the html tags specify a form property.
    // Param5: PickList AssetName if type is SELECT_FIELD, or ENUM List if type is
    // ENUM_LIST_RADIO_FIELD

    NUMBER_FIELD_ITEMS.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    NUMBER_FIELD_ITEMS.add( new ClaimFormStepElementItem( "MAX_DECIMALS", true, NUMBER_FIELD, "maxDecimals" ) );
    NUMBER_FIELD_ITEMS.add( new ClaimFormStepElementItem( "MASKED", true, YES_NO_RADIO_FIELD, "maskedOnEntry" ) );
    NUMBER_FIELD_ITEMS.add( new ClaimFormStepElementItem( "UNIQUE", true, ENUM_LIST_RADIO_FIELD, "uniquenessTypeCode", HierarchyUniqueConstraintEnum.getEnumList() ) );
    NUMBER_FIELD_ITEMS.add( new ClaimFormStepElementItem( "ENCRYPT", true, YES_NO_RADIO_FIELD, "shouldEncrypt" ) );

    TEXT_BOX_FIELD_ITEMS.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    TEXT_BOX_FIELD_ITEMS.add( new ClaimFormStepElementItem( "IS_WHY_FIELD", true, YES_NO_RADIO_FIELD, "whyField" ) );
    TEXT_BOX_FIELD_ITEMS.add( new ClaimFormStepElementItem( "MAX_SIZE", true, NUMBER_FIELD, "maxSize" ) );
    TEXT_BOX_FIELD_ITEMS.add( new ClaimFormStepElementItem( "INPUT_FORMAT", true, SELECT_FIELD, "textFieldInputFormatTypeCode", "picklist.textfieldinputformattype" ) );

    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "MAX_SIZE", true, NUMBER_FIELD, "maxSize" ) );
    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "INPUT_FORMAT", true, SELECT_FIELD, "textFieldInputFormatTypeCode", "picklist.textfieldinputformattype" ) );
    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "MASKED", true, YES_NO_RADIO_FIELD, "maskedOnEntry" ) );
    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "UNIQUE", true, ENUM_LIST_RADIO_FIELD, "uniquenessTypeCode", HierarchyUniqueConstraintEnum.getEnumList() ) );
    TEXT_FIELD_ITEMS.add( new ClaimFormStepElementItem( "ENCRYPT", true, YES_NO_RADIO_FIELD, "shouldEncrypt" ) );

    SECTION_HEADING_ITEMS.add( new ClaimFormStepElementItem( "HEADING", true, TEXT_FIELD, "cmData.heading" ) );

    COPY_BLOCK.add( new ClaimFormStepElementItem( "COPY", true, TEXT_AREA_FIELD, "cmData.copyBlock" ) );

    LINK.add( new ClaimFormStepElementItem( "LINK_NAME", true, TEXT_FIELD, "cmData.linkName" ) );
    LINK.add( new ClaimFormStepElementItem( "URL", true, TEXT_FIELD, "linkURL" ) );

    DATE_FIELD.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );

    BOOLEAN_SELECTION.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    BOOLEAN_SELECTION.add( new ClaimFormStepElementItem( "LABEL_TRUE", true, TEXT_FIELD, "cmData.labelTrue" ) );
    BOOLEAN_SELECTION.add( new ClaimFormStepElementItem( "LABEL_FALSE", true, TEXT_FIELD, "cmData.labelFalse" ) );

    SINGLE_SELECTION.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    SINGLE_SELECTION.add( new ClaimFormStepElementItem( "OPTIONS_SRC", true, SELECT_PICKLIST_FIELD, "selectionPickListName" ) );

    MULTI_SELECTION.add( new ClaimFormStepElementItem( "REQUIRED_FIELD", true, YES_NO_RADIO_FIELD, "required" ) );
    MULTI_SELECTION.add( new ClaimFormStepElementItem( "OPTIONS_SRC", true, SELECT_PICKLIST_FIELD, "selectionPickListName" ) );

    BUTTON.add( new ClaimFormStepElementItem( "BTN_LABEL", true, TEXT_FIELD, "cmData.buttonLabel" ) );
    BUTTON.add( new ClaimFormStepElementItem( "BTN_SCRIPT", true, TEXT_FIELD, "buttonScript" ) );

    FILE_FIELD_ITEMS.add( new ClaimFormStepElementItem( "FILE_TYPE_LABEL", true, TEXT_FIELD, "fileType" ) );
    FILE_FIELD_ITEMS.add( new ClaimFormStepElementItem( "FILE_SIZE_LABEL", true, NUMBER_FIELD, "fileSize" ) );

    itemListMap.put( ClaimFormElementType.NUMBER_FIELD, NUMBER_FIELD_ITEMS );
    itemListMap.put( ClaimFormElementType.TEXT_BOX_FIELD, TEXT_BOX_FIELD_ITEMS );
    itemListMap.put( ClaimFormElementType.TEXT_FIELD, TEXT_FIELD_ITEMS );
    itemListMap.put( ClaimFormElementType.SECTION_HEADING, SECTION_HEADING_ITEMS );
    itemListMap.put( ClaimFormElementType.COPY_BLOCK, COPY_BLOCK );
    itemListMap.put( ClaimFormElementType.LINK, LINK );
    itemListMap.put( ClaimFormElementType.DATE_FIELD, DATE_FIELD );
    itemListMap.put( ClaimFormElementType.BOOLEAN, BOOLEAN_SELECTION );
    itemListMap.put( ClaimFormElementType.SELECTION, SINGLE_SELECTION );
    itemListMap.put( ClaimFormElementType.MULTI_SELECTION, MULTI_SELECTION );
    itemListMap.put( ClaimFormElementType.BUTTON, BUTTON );
    itemListMap.put( ClaimFormElementType.ADDRESS_BOOK_SELECTION, ADDRESS_BOOK_SELECTION );
    itemListMap.put( ClaimFormElementType.BOOLEAN_CHECKBOX, BOOLEAN_CHECKBOX );
    itemListMap.put( ClaimFormElementType.ADDRESS_BLOCK, ADDRESS_BLOCK );
    itemListMap.put( ClaimFormElementType.FILE, FILE_FIELD_ITEMS );
  }

  public ClaimFormStepElementItem( String labelKey, boolean required, String inputType, String formProperty )
  {
    this.labelKey = labelKey;
    this.required = required;
    this.inputType = inputType;
    this.formProperty = formProperty;
  }

  public ClaimFormStepElementItem( String labelKey, boolean required, String inputType, String formProperty, String pickList )
  {
    this.labelKey = labelKey;
    this.required = required;
    this.inputType = inputType;
    this.formProperty = formProperty;
    this.pickList = pickList;
  }

  public ClaimFormStepElementItem( String labelKey, boolean required, String inputType, String formProperty, List enumList )
  {
    this.labelKey = labelKey;
    this.required = required;
    this.inputType = inputType;
    this.formProperty = formProperty;
    this.enumList = enumList;
  }

  public static List getItemListByType( ClaimFormElementType elementType )
  {
    return (List)itemListMap.get( elementType.getCode() );
  }

  public String getLabelKey()
  {
    return labelKey;
  }

  public void setLabelKey( String labelKey )
  {
    this.labelKey = labelKey;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public String getInputType()
  {
    return inputType;
  }

  public void setInputType( String inputType )
  {
    this.inputType = inputType;
  }

  public String getFormProperty()
  {
    return formProperty;
  }

  public void setFormProperty( String formProperty )
  {
    this.formProperty = formProperty;
  }

  public List getPickList()
  {
    return DynaPickListType.getList( pickList );
  }

  public String getPickListAsset()
  {
    return pickList;
  }

  public List getEnumList()
  {
    return enumList;
  }

  public void setEnumList( List enumList )
  {
    this.enumList = enumList;
  }

  public boolean isTextAreaField()
  {
    return TEXT_AREA_FIELD.equals( this.inputType );
  }

  public boolean isSelectField()
  {
    return SELECT_FIELD.equals( this.inputType );
  }

  public boolean isTextBoxField()
  {
    return TEXT_BOX_FIELD.equals( this.inputType );
  }

  public boolean isTextField()
  {
    return TEXT_FIELD.equals( this.inputType );
  }

  public boolean isYesNoRadioField()
  {
    return YES_NO_RADIO_FIELD.equals( this.inputType );
  }

  public boolean isEnumListRadioField()
  {
    return ENUM_LIST_RADIO_FIELD.equals( this.inputType );
  }

  public boolean isSelectPickListField()
  {
    return SELECT_PICKLIST_FIELD.equals( this.inputType );
  }

  public boolean isNumberField()
  {
    return NUMBER_FIELD.equals( this.inputType );
  }
}
