/*exported BunchBallModuleView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
console,
$,
_,
G5,
LaunchModuleView,
BunchBallModuleView:true
*/
BunchBallModuleView = LaunchModuleView.extend( {

    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );


        this.on( 'templateLoaded', function() {
            //console.log("[INFO] BunchBallModuleView Template loaded", this.$el);            
            this.loadWidgets();
        }, this );

        this.on( 'loadDataFinished', function( data ) {                      
           this.renderWidget( data );
        }, this );


        this.moduleCollection.on( 'filterChanged', function() {
           // Widget was not initiated when ever the filter getteing swiched. 
           // So Loding the widget again when you click mission filter
           if ( this.moduleCollection._filter == 'missions' ) {
             this.loadWidgets(); 
           }
        }, this );
    },

    loadWidgets: function() {
        var dataReturned,
        that = this;
        
        dataReturned = $.ajax( {
            dataType: 'g5Html',
            url: G5.props.URL_BUNCHBALL_WIDGET,                    
            type: 'GET',
            success: function( serverResp ) {
                //regular .ajax html object response
                var data = serverResp;
                   
                //notify listener
                that.trigger( 'loadDataFinished', data );
            }
        } );

        dataReturned.fail( function( jqXHR, textStatus ) {
            console.log( '[INFO] BunchBall: Request failed: ' + textStatus );
        } );
    }, 

    renderWidget: function( data ) {
        this.$el.find( '.missionWrapper' ).empty();
        this.$el.find( '.missionWrapper' ).append( data );
    }

} );
