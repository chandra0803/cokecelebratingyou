/*exported ReportsModuleView*/
/*global
FusionCharts,
ReportsModuleView:true
*/
ReportsModuleView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';
        var that = this;

        this.$el = this.options.$el;

        //report index
        this.reportIndex = this.options.reportIndex;

        this.on( 'loadChartDone', function( tpl, vars, subTpls ) {
            // console.log(tpl, vars, subTpls);
            that.tplVars = vars;
            that.subTpls = subTpls;

            //it is now safe to load chart (trigger render)
            that.render();
        } );

        this.on( 'renderDone', this.renderChart, this );

        if( this.options.json ) {
            this.processData( this.options.json );
            this.trigger( 'loadChartDone' );
        }
        else {
            this.loadChart();
        }
    }, // initialize
    events: {
        // 'click .tab': 'tabSwitch'
    },
    // populate module & draw charts
    render: function() {
        'use strict';
        var $html = $( this.options.tpl( this.model.toJSON() ) );

        if( this.$el === undefined ) {
            this.$el = $html;
        }
        else {
            this.$el
                .html( $html.html() )
                .attr( 'class', $html.attr( 'class' ) )
                .data( {
                    reportIndex: this.model.get( 'reportIndex' ),
                    id: this.model.get( 'id' ),
                    parentId: this.model.get( 'parentId' )
                } );
        }

        if( this.options.$target ) {
            this.$el.appendTo( this.options.$target );
        }
        // this.$el = $html;
        this.trigger( 'renderDone' );
    }, // render

    // load JSON for this chart
    loadChart: function() {
        'use strict';
        var that = this;

        G5.util.showSpin( this.$el );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_REPORTS_MODULE,
            data: {
                method: 'display',
                newIndex: this.reportIndex
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                that.$el.find( '#chartContainer' + that.reportIndex ).html(
                    that.make( 'p', {}, textStatus + ': ' + jqXHR.status + ' ' + errorThrown )
                );
            },
            success: function( servResp ) {
                var json = servResp.data.favorites[ 0 ] || {};

                that.processData( json );
                that.trigger( 'loadChartDone' );
            } // ajax success
        } ); // ajax
    }, // loadChart

    processData: function( json ) {
        if( json.id === undefined ) {
            json.isEmpty = true;
        }

        //json.clickThruUrl = G5.props.URL_REPORTS_ALL + '#' + json.parentId + '/' + json.id;
        json.clickThruUrl = G5.props.URL_REPORTS_ALL + ( G5.props.URL_REPORTS_ALL.indexOf( '?' ) !== -1 ? '&' : '?' ) + 'reportId=' + json.parentId + '&dashboardItemId=' + json.id + '#' + json.parentId;

        json.reportIndex = this.reportIndex;

        this.model = new Backbone.Model( json );
    },

    emptyModule: function() {
        'use strict';

        this.$el.find( '.reportsModule' ).click( function() {
            window.location.href = G5.props.URL_REPORTS_ALL;
        } );

        // I'm not sure what this does, but I kept it in here just in case we come across a new error now that I changed this to use subTpls

        /* var myChartGenerated = [],
            chartIdIWantToDelete = '';

        // need to determine whether chart exists
        // if so, use fusioncharts dispose method
        chartIdIWantToDelete = this.$el.find('[id^=chartId]').attr('id');//'chartId' + this.reportIndex;

        if ($('#'+chartIdIWantToDelete).length > 0) {
            FusionCharts(chartIdIWantToDelete).dispose();
        }
        this.$el.find('#chartContainer'+this.reportIndex+' .spin').spin(false);*/

    }, // emptyModule

    renderChart: function() {
        'use strict';
        var that = this,
            id = this.model.get( 'id' );

        if( id === undefined ) {
            return;
        }

        G5.util.showSpin( this.$el.find( '.chartContainer' ) );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.model.get( 'dataUrl' ),
            error: function( jqXHR, textStatus, errorThrown ) {
                that.$el.find( '.chartContainer' ).html(
                    that.make( 'p', {}, textStatus + ': ' + jqXHR.status + ' ' + errorThrown )
                );
            },
            data: {},
            success: function( servResp ) {
                var data = servResp.data,
                    chartStyle,
                    colors;

                // forces FusionCharts to render in javascript only (no Flash)
                FusionCharts.setCurrentRenderer( 'javascript' );
                // helps to ameliorate conflict with modernizr.js
                FusionCharts.options.allowIESafeXMLParsing = false;

                chartStyle = _.clone( G5.props.chartStyle );
                colors = window.getComputedStyle( document.querySelector( '.' + $.trim( that.$el.attr( 'class' ) ).replace( / /g, '.' ) ), ':before' ).getPropertyValue( 'content' ).replace( /\"/g, '' ).replace( / /g, '' );
                if( colors && colors != 'none' ) {
                    chartStyle.paletteColors = colors;
                }
                that.model.set( 'chartStyle', chartStyle );

                // copy fusionChartsParemeters into the location that FC expects it to be
                data.chart = $.extend(
                    true,                                       // deep merge = true
                    {},                                         // start with an empty object
                    {                                           // add some defaults
                        showZeroPies: false                        // don't show pie wedges that represent 0%
                    },
                    that.model.get( 'fusionChartsParameters' ),   // merge in the fusionChartsParameters from chartSet
                    servResp.data.chart,                        // then overwrite with any values passed explicitly in the individual chart JSON feed
                    {                                           // overwrite with custom props for the module
                        caption: '',
                        subCaption: ''
                    },
                    that.model.get( 'chartStyle' )                // finally, override with computed chart styles
                );

                that.chart = new FusionCharts( {
                    type: that.model.get( 'chartType' ).replace( /3D/i, '2d' ).toLowerCase(),
                    id: 'chartId' + id,
                    width: '100%',
                    height: '100%',
                    renderAt: 'chartContainer' + id,
                    dataSource: data,
                    dataFormat: 'json'
                } );
                that.chart.configure( that.model.get( 'chartConfigure' ) );
                that.chart.render();
            } // chart data JSON success
        } ); // individual chart ajax

        return this;
    } // renderChart

} ); // extend
