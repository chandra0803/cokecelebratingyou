/*exported XpromoStoryModel */
/*global
Backbone,
XpromoStoryModel:true
*/
XpromoStoryModel = Backbone.Model.extend( {
    defaults: {
            type: 'promo type',
            data: {
            //banner
                text: 'banner > text',
                linkText: 'linkText',
                linkUrl: 'http://www.biworldwide.com/',
                target: '_blank',
                classes: 'bunch, of, classes',
            //news
                sortDate: 'sortDate',
                storyName: 'storyName',
                storySlug: 'storySlug',
                storyDate: 'storyDate',
                storyContent: 'storyContent',
                storyContentShort: 'storyContentShort',
                storyImageUrl: 'storyImageUrl',
                storyFormat: 'storyFormat'
            }
    },

    initialize: function() {

    }
  } );
