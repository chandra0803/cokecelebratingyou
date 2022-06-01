/*exported CareerMomentsModuleView */
/*global
LaunchModuleView,
CareerMomentsModel,
CareerMomentsModuleView:true
*/
CareerMomentsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function () {
        var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass LaunchModuleView

        this.celebrateModel = new CareerMomentsModel( {
            isModule: true
        } );

        this.on( 'templateLoaded', function( tpl, vars, subTpls ) {            
            this.careerMomentsNewHireTpl = subTpls.careerMomentsNewHireTpl;
            this.careerMomentsJobChangeTpl = subTpls.careerMomentsJobChangeTpl;
            that.dataLoad( true );

            that.celebrateModel.loadRibbonData();

            that.celebrateModel.on( 'dataLoaded', function() {
                that.renderList();
            } );

        }, this );
    },
    events: {
        'click .profile-popover': 'attachParticipantPopover',
        'click .cheers-popover': 'attachCheersPopover' // Cheers Customization - Celebrating you wip-62128
    },
    renderList: function() {
        var $careerMomentListCont = this.$el.find( '.careerMomentsList' ),
            careerMomentsSets = this.celebrateModel.get( 'careerMomentsSets' ),
            $newHireDom = this.$el.find( '#newHireCarousel' ),
            $jobChangeDom = this.$el.find( '#jobChangeCarousel' ),
            that = this;

        _.each( careerMomentsSets, function( cmSet ) {
	        	if ( cmSet.newHires && cmSet.newHires.newHiresData ) {
	        		cmSet.newHires.count = cmSet.newHires.newHiresData.length;
	    		}
                $careerMomentListCont.append( that.careerMomentsNewHireTpl( cmSet ) );
                that.renderCarousel( '#newHireCarousel' );
                if ( cmSet.newHires && cmSet.newHires.newHiresData ) {                
                    that.$el.find( '#newHireCount' ).text( cmSet.newHires.newHiresData.length );                    
                }
        } );

        _.each( careerMomentsSets, function( cmSet ) {
        		if ( cmSet.jobChanges && cmSet.jobChanges.jobChangesData ) {
        			cmSet.jobChanges.count = cmSet.jobChanges.jobChangesData.length;
        		}
                $careerMomentListCont.append( that.careerMomentsJobChangeTpl( cmSet ) );
                that.renderCarousel( '#jobChangeCarousel' );
                if ( cmSet.jobChanges && cmSet.jobChanges.jobChangesData ) {
                    that.$el.find( '#jobChangeCount' ).text( cmSet.jobChanges.jobChangesData.length );                    
                }
        } );

        that.dataLoad( false );

    },


    // Cheers Customization - Celebrating you wip-62128 - starts
    attachCheersPopover: function( e ) {
        var $tar = $( e.target ),
            $paxId = $tar.data( 'participantIds' ),
            $promoId = $tar.data( 'cheersPromotionId' );

        //attach participant popovers
        if ( !$tar.data( 'cheersPopover' ) ) {
            $tar.cheersPopover( { containerEl: this.$el, recipientId: $paxId, promotionId : $promoId, canReload : true  } ).qtip( 'show' );
        }
        e.preventDefault();
    },
    // Cheers Customization - Celebrating you wip-62128 - ends

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );

        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    },
    renderCarousel: function( dom ) {        
        $( dom ).on( 'init', function() {
            $( dom ).find( '.item' ).filter( ':nth-child(1), :nth-child(2), :nth-child(3)' ).removeClass( 'slick-active' );
            _.delay( function( ) { $( dom ).find( '.item' ).filter( ':nth-child(1), :nth-child(2), :nth-child(3)' ).addClass( 'slick-active' ); }, 500 );
        } );
        this.startSlick( dom );
    },
    startSlick: function( dom ) {
        // slick length =
        var slickNumItems = $( dom ).find( '.item' ).length,
            desktopNum = 3,
            tabletNum = 2;
        var $slickEl = this.$el.find( dom ); //find all modules with a carousel

        if ( slickNumItems === 1 ) {
            desktopNum = 1;
            tabletNum = 1;
        }else if( slickNumItems === 2 ) {
            desktopNum = 2;
        }
        $slickEl.slick( {
            dots: false,
            infinite: false,
            speed: 300,
            slidesToShow: desktopNum,
            slidesToScroll: desktopNum,
            centerMode: false,
            variableWidth: false,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            responsive: [
            {
              breakpoint: 1199,
              settings: {
                slidesToShow: tabletNum,
                slidesToScroll: tabletNum,
                dots: false
              }
            },
            {
              breakpoint: 640,
              settings: {
                slidesToShow: 1,
                slidesToScroll: 1,
                dots: false
              }
            }
            ]
        } );
    }

} );
