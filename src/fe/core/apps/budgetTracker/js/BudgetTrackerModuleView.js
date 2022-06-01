/*exported BudgetTrackerModuleView */
/*global
SidebarModuleView,
BudgetCollectionView,
BudgetTrackerModuleView:true
*/
BudgetTrackerModuleView = SidebarModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';

        //this is how we call the super-class initialize function (inherit its magic)
        SidebarModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass SidebarModuleView
        this.events = _.extend( {}, SidebarModuleView.prototype.events, this.events );

        this.on( 'templateLoaded', this.postRender, this );
    },

    events: {
        'click .reloadBtn': 'doReload'
    },

    postRender: function() {
        //add the budget collection view
        this.budgColl = new BudgetCollectionView( { el: this.$el.find( '.budgetCollectionView' ) } );

        // start the loading state and spinner
        this.dataLoad( true );

        this.budgColl.on( 'budgetAsOfTimestampLoaded', function() {
            // stop the loading state and spinner
            this.dataLoad( false );
            G5.util.hideSpin( this.$el );


            // each budget list has a special CM key containing the full translated string for the "Remaining as of" text
            // the key ouput will have a {0} placeholder where the budgetAsOfTimestamp value is inserted
            // this allows the translations to have plain text and the budgetAsOfTimestamp in any order
            // we embed this CM output as a tplVariable in our budgetTrackerModule Handlebars template
            // we also have an budgetAsOfTimestamp subTpl embedded in our budgetTrackerModule Handlebars template
            // we pass the budgets.budgetAsOfTimestamp value from the JSON to the subTpl, then replace the {0} with the rendered output
            // the final string is assigned to budgets.budgetAsOfTimestampFormatted in the JSON to be inserted into the module via jQ
            if ( this.tplVariables.budgetAsOfTimestamp ) {
                this.budgColl.budgetAsOfTimestampFormatted = G5.util.cmReplace( {
                    cm: this.tplVariables.budgetAsOfTimestamp,
                    subs: [
                        this.subTpls.budgetAsOfTimestamp( { budgetAsOfTimestamp: this.budgColl.budgetAsOfTimestamp } )
                    ]
                } );
            }


            // insert the timestamp string
            this.$el.find( '.budgetAsOfTimestamp' ).html( this.budgColl.budgetAsOfTimestampFormatted );

            this.budgColl.adjustBudgetQtips();
        }, this );
    },

    doReload: function() {
        'use strict';
        if ( !this.budgColl.isLoading ) {
            G5.util.showSpin( this.$el, { cover: true } );
            this.budgColl.loadBudgets();
        }
    },

    updateView: function() {
        if( this.budgColl ) {
            this.budgColl.adjustBudgetQtips();
        }
    }

} );
