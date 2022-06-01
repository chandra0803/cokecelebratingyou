<%@ include file="/include/taglib.jspf"%>
<div id="plateauAwardsPageView" data-promotion-id="${promotionId}" class="page-content">

	<div class="page-topper">
	    <div class="row-fluid">
	        <div class="promotionSelectWrapper span12 form-inline form-labels-inline">
	            <div class="control-group">
	                 <label class="control-label"><cms:contentText key="PICK_A_PROMOTION" code="hometile.plateauAward" /></label>
	                <div class="controls">
	                    <select id="promotionSelect">
	                        <option value="-1"><cms:contentText key="SELECT_A_PROMOTION" code="hometile.plateauAward" /></option>

	                        <!-- dynamic -->

	                    </select>
	                </div>
	            </div>
	        </div>
	    </div>

	    <div class="row-fluid">
	        <div class="span12">
	            <ul id="levelsTabs" class="nav nav-tabs" data-msg-empty="<cms:contentText key="NO_LEVELS" code="hometile.plateauAward" />">

	                <!-- dynamic -->

	            </ul>
	        </div>
	    </div>
    </div>

    <div id="productBrowser" class="tab-content">

    	<div class="row-fluid noPromotionSelected" style="display:none">
            <div class="span12">
                <p>
                	<cms:contentText key="CHOOSE_PROMO" code="hometile.plateauAward" />
                </p>
            </div>
        </div><!-- /.noPromotionSelected -->

        <div class="row-fluid">
            <div class="span12">
              <h2 class="levelDescription">
                    <!-- dynamic -->
 			  </h2>
            </div>
        </div>

		<div class="row-fluid">
            <div class="span12">
                <ul class="currentProducts thumbnails" data-msg-empty="<cms:contentText key="NO_PRODUCTS" code="hometile.plateauAward" />">
                    <!-- dynamic -->
                </ul>
            </div>
        </div>
    </div><!-- /#productBrowser -->

</div><!-- /#plateauAwardsPageView -->
