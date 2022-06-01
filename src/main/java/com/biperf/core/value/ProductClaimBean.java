
package com.biperf.core.value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ProductClaimBean.ProductBean.CharacteristicsValueBean.CharacteristicsTypeValueBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public final class ProductClaimBean
{
  @JsonProperty( "id" )
  private Long id;
  @JsonProperty( "name" )
  private String name;
  @JsonProperty( "rulesText" )
  private String rulesText;
  @JsonProperty( "teamActive" )
  private boolean teamActive;
  @JsonProperty( "customSections" )
  private List<String> customSections = new ArrayList<String>();
  @JsonProperty( "products" )
  private List<ProductBean> products = new ArrayList<ProductBean>();
  @JsonProperty( "editProductQuantity" )
  private boolean editProductQuantity;
  @JsonProperty( "defaultProductQuantity" )
  private int defaultProductQuantity;

  // need these for claim forms; not for JSON
  @JsonIgnore
  private boolean claimFormUsed;
  @JsonIgnore
  private ClaimForm claimForm;

  public ProductClaimBean( ProductClaimPromotion promotion, String webRulesText )
  {
    this( promotion );
    this.rulesText = webRulesText;
  }

  public ProductClaimBean( ProductClaimPromotion promotion )
  {
    this.id = promotion.getId();
    this.name = promotion.getName();
    this.teamActive = promotion.isTeamUsed();

    this.claimFormUsed = promotion.isClaimFormUsed();
    this.claimForm = promotion.getClaimForm();

    setClaimFormSteps( promotion );

    List<Product> promotionProducts = getProductService().getProductsByPromotionAndDateRange( promotion.getId(), UserManager.getTimeZoneID() );
    for ( Iterator iter = promotionProducts.iterator(); iter.hasNext(); )
    {
      Product product = (Product)iter.next();
      this.products.add( new ProductBean( product ) );
    }

    if ( promotion.getDefaultQuantity() > 0 )
    {
      defaultProductQuantity = promotion.getDefaultQuantity();
      editProductQuantity = false;
    }
    else
    {
      editProductQuantity = true;
    }
  }

  private void setClaimFormSteps( ProductClaimPromotion promotion )
  {
    if ( promotion != null && promotion.isClaimFormUsed() )
    {
      if ( addCustomSection( promotion.getClaimForm() ) )
      {
        customSections.add( "customSection-" + promotion.getId() );
      }
    }
  }

  private boolean addCustomSection( ClaimForm claimForm )
  {
    if ( claimForm != null && claimForm.hasCustomFormElements() )
    {
      return true;
    }
    return false;
  }

  public static class ProductBean
  {
    @JsonProperty( "id" )
    private Long id;
    @JsonProperty( "name" )
    private String name;
    @JsonProperty( "category" )
    private String category;
    @JsonProperty( "subcategory" )
    private String subcategory;
    private List<CharacteristicsValueBean> characteristics = new ArrayList<CharacteristicsValueBean>();

    public ProductBean( Product product )
    {
      if ( product != null )
      {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getProductCategoryName();
        this.subcategory = product.getProductSubCategoryName();

        for ( Iterator iter = product.getProductCharacteristicTypes().iterator(); iter.hasNext(); )
        {
          ProductCharacteristicType productCharacteristicType = (ProductCharacteristicType)iter.next();

          CharacteristicsValueBean characteristicsValueBean = new CharacteristicsValueBean();
          List<CharacteristicsTypeValueBean> typeList = new ArrayList<CharacteristicsTypeValueBean>();
          if ( productCharacteristicType.getPlName() != null )
          {
            if ( !DynaPickListType.getList( productCharacteristicType.getPlName() ).isEmpty() )
            {
              for ( Iterator listIter = DynaPickListType.getList( productCharacteristicType.getPlName() ).iterator(); listIter.hasNext(); )
              {
                CharacteristicsTypeValueBean characteristicsTypeValueBean = new CharacteristicsTypeValueBean();
                DynaPickListType dynaPickListType = (DynaPickListType)listIter.next();
                characteristicsTypeValueBean.setId( dynaPickListType.getCode() );
                characteristicsTypeValueBean.setName( dynaPickListType.getName() );
                typeList.add( characteristicsTypeValueBean );
              }
            }
          }
          characteristicsValueBean.setList( typeList );
          characteristicsValueBean.setId( productCharacteristicType.getId() );
          characteristicsValueBean.setName( productCharacteristicType.getCharacteristicName() );
          characteristicsValueBean.setRequired( productCharacteristicType.getIsRequired() );
          characteristicsValueBean.setUnique( productCharacteristicType.getIsUnique() );
          characteristicsValueBean.setDateStart( productCharacteristicType.getDisplayDateStart() );
          characteristicsValueBean.setDateEnd( productCharacteristicType.getDisplayDateEnd() );
          characteristicsValueBean.setMin( productCharacteristicType.getMinValue() );
          characteristicsValueBean.setMax( productCharacteristicType.getMaxValue() );
          characteristicsValueBean.setMaxSize( productCharacteristicType.getMaxSize() );
          characteristicsValueBean.setType( productCharacteristicType.getCharacteristicDataType().getCode() );

          this.characteristics.add( characteristicsValueBean );
        }
      }
    }

    public Long getId()
    {
      return id;
    }

    public String getName()
    {
      return name;
    }

    public String getCategory()
    {
      return category;
    }

    public String getSubcategory()
    {
      return subcategory;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public void setCategory( String category )
    {
      this.category = category;
    }

    public void setSubcategory( String subcategory )
    {
      this.subcategory = subcategory;
    }

    @JsonProperty( "characteristics" )
    public List<CharacteristicsValueBean> getCharacteristics()
    {
      return characteristics;
    }

    public void setCharacteristics( List<CharacteristicsValueBean> characteristics )
    {
      this.characteristics = characteristics;
    }

    public static class CharacteristicsValueBean
    {
      @JsonProperty( "type" )
      private String type;
      @JsonProperty( "maxSize" )
      private Long maxSize;
      @JsonProperty( "name" )
      private String name;
      @JsonProperty( "value" )
      private String value;
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "required" )
      private Boolean required;
      @JsonProperty( "unique" )
      private Boolean unique;
      @JsonProperty( "dateStart" )
      private String dateStart;
      @JsonProperty( "dateEnd" )
      private String dateEnd;
      @JsonProperty( "min" )
      private BigDecimal min;
      @JsonProperty( "max" )
      private BigDecimal max;
      private List<CharacteristicsTypeValueBean> list = new ArrayList<CharacteristicsTypeValueBean>();

      public String getType()
      {
        return type;
      }

      public void setType( String type )
      {
        this.type = type;
      }

      public Long getMaxSize()
      {
        return maxSize;
      }

      public void setMaxSize( Long maxSize )
      {
        this.maxSize = maxSize;
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
      }

      public String getValue()
      {
        return value;
      }

      public void setValue( String value )
      {
        this.value = value;
      }

      public Long getId()
      {
        return id;
      }

      public void setId( Long id )
      {
        this.id = id;
      }

      public Boolean isRequired()
      {
        return required;
      }

      public void setRequired( Boolean required )
      {
        this.required = required;
      }

      public Boolean isUnique()
      {
        return unique;
      }

      public void setUnique( Boolean unique )
      {
        this.unique = unique;
      }

      public String getDateStart()
      {
        return dateStart;
      }

      public void setDateStart( String dateStart )
      {
        this.dateStart = dateStart;
      }

      public String getDateEnd()
      {
        return dateEnd;
      }

      public void setDateEnd( String dateEnd )
      {
        this.dateEnd = dateEnd;
      }

      public BigDecimal getMin()
      {
        return min;
      }

      public void setMin( BigDecimal min )
      {
        this.min = min;
      }

      public BigDecimal getMax()
      {
        return max;
      }

      public void setMax( BigDecimal max )
      {
        this.max = max;
      }

      @JsonProperty( "list" )
      public List<CharacteristicsTypeValueBean> getList()
      {
        return list;
      }

      public void setList( List<CharacteristicsTypeValueBean> list )
      {
        this.list = list;
      }

      public static class CharacteristicsTypeValueBean
      {
        @JsonProperty( "id" )
        private String id;
        @JsonProperty( "name" )
        private String name;

        public String getId()
        {
          return id;
        }

        public void setId( String id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

      }

    }

  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getRulesText()
  {
    return rulesText;
  }

  public boolean isTeamActive()
  {
    return teamActive;
  }

  public List<String> getCustomSections()
  {
    return customSections;
  }

  public List<ProductBean> getProducts()
  {
    return products;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public void setRulesText( String rulesText )
  {
    this.rulesText = rulesText;
  }

  public void setTeamActive( boolean teamActive )
  {
    this.teamActive = teamActive;
  }

  public void setCustomSections( List<String> customSections )
  {
    this.customSections = customSections;
  }

  public void setProducts( List<ProductBean> products )
  {
    this.products = products;
  }

  public boolean isEditProductQuantity()
  {
    return editProductQuantity;
  }

  public void setEditProductQuantity( boolean editProductQuantity )
  {
    this.editProductQuantity = editProductQuantity;
  }

  public int getDefaultProductQuantity()
  {
    return defaultProductQuantity;
  }

  public void setDefaultProductQuantity( int defaultProductQuantity )
  {
    this.defaultProductQuantity = defaultProductQuantity;
  }

  public boolean isClaimFormUsed()
  {
    return claimFormUsed;
  }

  public ClaimForm getClaimForm()
  {
    return claimForm;
  }

  private ProductService getProductService()
  {
    return (ProductService)BeanLocator.getBean( ProductService.BEAN_NAME );
  }
}
