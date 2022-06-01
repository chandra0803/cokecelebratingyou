/*exported CelebrationCongratsModuleView */
/*global
LaunchModuleView,
Modernizr,
CelebrationCongratsModuleView:true
*/
CelebrationCongratsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        //var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView

        // this.model.set(
        //     'allowedDimensions', [
        //         { w: 2, h: 2 }

        //     ],
        //     { silent: true }
        // );

        this.on( 'templateLoaded', function() {
            var video = document.getElementById( 'video' ),
                path = window.getComputedStyle( document.querySelector( '.congrats-vid-container' ), ':before' ).getPropertyValue( 'content' ).replace( /"/g, '' ),
                extension = '.mp4';

            if( path.length ) {
                if( Modernizr.video && Modernizr.video.webm ) {
                    extension = '.webm';
                } else if( Modernizr.video && Modernizr.video.ogg ) {
                    extension = '.ogv';
                } else if( Modernizr.video && Modernizr.video.h264 ) {
                    extension = '.mp4';
                }
                video.setAttribute( 'src', path + '' + extension );
                video.load();
            }else{
                video.outerHTML = '';
            }


            //_.delay( G5.util.textShrink, 100, this.$el.find( '.congrats-name' ), { minFontSize: 20 } );
            //G5.util.textShrink( this.$el.find( '.congrats-name' ), { minFontSize: 20 } );
            //that.nameResize();
        } );
    },
    nameResize: function () {
        var $nameContainer = this.$el.find( '.celebrationCongratsInfo' ),
            $name = $nameContainer.find( '.congrats-name' );

            if( $name.outerWidth() < $name[ 0 ].scrollWidth ) {
                $name.css( 'word-break', 'break-all' );
            }
    }
} );
