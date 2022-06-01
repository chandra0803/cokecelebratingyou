/*globals
Raphael
*/
( function( $ ) {

    $.fn.engagementDrawScoreMeter = function( opts ) {
        return this.each( function() {
            var that = this,
                defaults,
                settings,
                calcSettings,
                drawScoreMeter;

            this.$el = opts.$el || $( this );

            defaults = {
                raphaelSrc: $( 'body' ).find( '.raphael' ).data( 'src' ),
                $box: this.$el.find( '.meter' ).not( '.meterAch' ),
                $boxAch: this.$el.find( '.meterAch' ),
                $actual: this.$el.find( '.number' ),
                boxId: 'meter_' + Math.round( Math.random() * 1000000 ),
                actual: opts.actual || this.$el.data( 'actual' ),
                target: opts.target || this.$el.data( 'target' ),
                arcStrokeWidth: 4,
                arcGlowMultiplier: 2,
                animationDuration: G5.props.ANIMATION_DURATION,
                redraw: false,
                noAnim: false
            };

            /*
             * default settings explained
             * --------------------------
             * raphaelSrc: a string pointing to the source for the Raphael script doing the drawing
             * arcStrokeWidth: width of the arc stroke in pixels
             * arcGlowMultiplier: how much wider the arc glow should be as a multiple of the arcStrokeWidth
             * animationDuration: how long the drawing animation takes in milliseconds
             * redraw: passing true destroys the existing meter and draws again from scratch
             * noAnim: passing true prevents animation and draws instantly
             *
             * additional settings
             * ----------------------------
             * NOTE: these will be generated automatically unless overridden
             *
             * $el: container holding the meters if not the container to which this plugin is applied
             * $box: container holding the actual meter
             * $boxAch: container holding the achieved meter
             * $actual: container holding the final number
             * actual: user's actual score value
             * target: user's ideal value against which the actual is compared
             * bgArcColor: color value for the arc track in RGB format: rgb(R,G,B)
             * fgArcColor: color value for the actual arc in RGB format: rgb(R,G,B)
             * ovArcColor: color value for the achieved arc background in RGB format: rgb(R,G,B)
             */

            calcSettings = {
                boxW: defaults.$box.width(),
                boxH: defaults.$box.height(),
                score: Math.min( defaults.actual / defaults.target * 100, 200 ),
                bgArcColor: defaults.$box.css( 'border-top-color' ),
                fgArcColor: defaults.$box.css( 'outline-color' ),
                ovArcColor: defaults.$box.css( 'color' ),
                arcGlowMultiplier: parseInt( defaults.$box.css( 'word-spacing' ) ) || defaults.arcGlowMultiplier,
                arcStrokeWidth: parseInt( defaults.$box.css( 'letter-spacing' ) ) || defaults.arcStrokeWidth
            };

            settings = $.extend( {}, defaults, calcSettings, opts );

            this.$el.data( '_meter', that.$el.data( '_meter' ) || {} );

            if ( settings.redraw === true ) {
                settings.$box.empty();
                settings.$boxAch.empty();
                this.$el.data( '_meter', {} );
            }

            //console.log(settings);

            /*
             * this is where the magic happens...
             */

            drawScoreMeter = function( settings ) {
                var _meter = that.$el.data( '_meter' );

                if ( isNaN( settings.score ) ) {
                    settings.score = 0;
                }

                if ( !settings.$box.is( ':visible' ) ) {
                    return false;
                }

                // // the meter never draws when scores are inactive
                // if( this.model.get('isScoreActive') === false ) {
                //     return false;
                // }

                if ( !window.Raphael ) {
                    $.cachedScript( settings.raphaelSrc ).done( function() {
                        drawScoreMeter( settings );
                    } );

                    return false;
                }

                settings.$box.attr( 'id', settings.boxId );
                settings.$box.closest( '.achieved' ).removeClass( 'achieved' );
                settings.$boxAch.attr( 'id', 'ach_' + settings.boxId );

                if ( !_meter.meter ) {
                    _meter.meter = Raphael( settings.boxId, settings.boxW, settings.boxH );
                    _meter.meter.customAttributes.arc = function ( xloc, yloc, value, total, R ) {
                        var alpha = 360 / total * value,
                            a = ( 90 - alpha ) * Math.PI / 180,
                            x = xloc + R * Math.cos( a ),
                            y = yloc - R * Math.sin( a ),
                            path;
                        if ( total === value ) {
                            path = [
                                [ 'M', xloc, yloc - R ],
                                [ 'A', R, R, 0, 1, 1, xloc - 0.01, yloc - R ]
                            ];
                        } else {
                            path = [
                                [ 'M', xloc, yloc - R ],
                                [ 'A', R, R, 0, +( alpha > 180 ), 1, x, y ]
                            ];
                        }
                        return {
                            path: path
                        };
                    };

                    _meter.meterArcs = {
                        bg: [ settings.boxW / 2, settings.boxH / 2, 100, 100, Math.min( settings.boxW, settings.boxH ) / 2 - settings.arcStrokeWidth * 2 ],
                        fg: [ settings.boxW / 2, settings.boxH / 2, Math.min( settings.score, 100 ), 100, Math.min( settings.boxW, settings.boxH ) / 2 - settings.arcStrokeWidth * 2 ],
                        ov: [ settings.boxW / 2, settings.boxH / 2, Math.max( settings.score - 100, 0 ), 100, Math.min( settings.boxW, settings.boxH ) / 2 - settings.arcStrokeWidth * 1.5 ]
                    };

                    _meter.meterAch = Raphael( 'ach_' + settings.boxId, settings.boxW, settings.boxH );
                }                else if ( _meter.meter ) {
                    _meter.meterBgArc.remove();
                    _meter.meterFgArc.remove();
                    _meter.meterAchArc.remove();
                    if ( _meter.meterAchArcGlow ) { _meter.meterAchArcGlow.remove(); }
                }

                _meter.meterBgArc = _meter.meter
                    .path()
                    .attr( {
                        'stroke': settings.bgArcColor,
                        'stroke-width': settings.arcStrokeWidth,
                        arc: _meter.meterArcs.bg
                    } );

                _meter.meterFgArc = _meter.meter
                    .path()
                    .attr( {
                        'stroke': settings.fgArcColor,
                        'stroke-width': settings.arcStrokeWidth,
                        arc: [ _meter.meterArcs.fg[ 0 ], _meter.meterArcs.fg[ 1 ], 0.01, _meter.meterArcs.fg[ 3 ], _meter.meterArcs.fg[ 4 ] ]
                    } );

                _meter.meterAchArc = _meter.meterAch
                    .circle( _meter.meterArcs.ov[ 0 ], _meter.meterArcs.ov[ 1 ], _meter.meterArcs.ov[ 4 ] )
                    .attr( {
                        'stroke-width': 0,
                        fill: settings.fgArcColor
                    } );

                _meter.meterAchArcGlow = _meter.meterAchArc
                    .glow( {
                        width: settings.arcStrokeWidth * settings.arcGlowMultiplier,
                        opacity: 0.625,
                        color: settings.ovArcColor
                    } );

                _meter.meterFgArc
                    .animate( {
                           arc: _meter.meterArcs.fg
                        },
                        settings.noAnim === true ? 0 : settings.animationDuration,
                        '<>',
                        function() {
                            if ( settings.score >= 100 ) {
                                that.$el.addClass( 'achieved' );

                                _meter.meterFgArc.attr( {
                                    stroke: settings.ovArcColor
                                } );
                            }
                        }
                    );

                settings.$actual.text( '0' );
                settings.$actual.animateNumber(
                    settings.actual,
                    settings.noAnim === true ? 0 : settings.animationDuration
                );
            };

            /*
             * this is where we call the magic
             */
            drawScoreMeter( settings );

        } );
    };
} )( jQuery );