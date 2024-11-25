/* =========================================================
 * bootstrap-modal.js v2.3.1
 * http://twitter.github.com/bootstrap/javascript.html#modals
 * =========================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================= */


/*
 *  PATCHED
 *  - class added to body when modal is open
 *  - stack page style modal
 *  - global event triggers when a modal opens or closes
 *  - allow more than one modal to open - http://stackoverflow.com/a/13649538
 *  - move the modal-backdrop behind all open modals by putting it first in the DOM
 *  - stack modal is always moved to the end of the body and is put back where it came from when closed
 */

!function ($) {

  "use strict"; // jshint ;_;


 /* MODAL CLASS DEFINITION
  * ====================== */

  var Modal = function (element, options) {
    this.options = options
    this.$element = $(element)
      .delegate('[data-dismiss="modal"]', 'click.dismiss.modal', $.proxy(this.hide, this))
    this.options.remote && this.$element.find('.modal-body').load(this.options.remote)
  }

  Modal.prototype = {

      constructor: Modal

    , toggle: function () {
        return this[!this.isShown ? 'show' : 'hide']()
      }

    , show: function () {
        var that = this
          , e = $.Event('show')

        this.$element.trigger(e)

        if (this.isShown || e.isDefaultPrevented()) return

        //PATCH START - class added to body when modal is open
        $('body').addClass('modal-open');
        $('html').addClass('modal-open');
        //PATCH END

        //PATCH START - stack modal - adjust top to current viewport
        if(this.$element.hasClass('modal-stack')) {
          $('body').addClass('modal-stack-open');
          $('html').addClass('modal-stack-open');

          //PATCH START 2 - stack modal is always moved to the end of body when opened (moved back in hideModal below)
          if(this.$element.closest('#contents').length) {
            this.$element.attr('id', this.$element.attr('id') || 'tempID'+Math.round(Math.random()*10000) )
            this.$placeholder = $('<div class="stack-modal-placeholder" data-id="'+this.$element.attr('id')+'"></div>')
            this.$element.before( this.$placeholder )
            this.$element.appendTo( $('body') )
          }
          //PATCH END 2

          // removed the conditional that used to be here which checked for this.$element.data('yOffset') === 'adjust'
          // because we're always moving the stack modal to the end of the body, we need to adjust its position every time
          // pageYOffset works for most, but < ie9 needs $.scrollTop() applied to 'html' -- chrome uses 'body'
          // this.$element.css('top', (window.pageYOffset||$('html').scrollTop()) + parseInt(this.$element.css('left'), 10));
        }
        //PATCH END

        this.isShown = true

        this.escape()

        this.backdrop(function () {
          var transition = $.support.transition && that.$element.hasClass('fade')

          if (!that.$element.parent().length) {
            that.$element.appendTo(document.body) //don't move modals dom position
          }
          //PATCH START - make sure the modal is always after the backdrop in the DOM
          that.$backdrop.insertBefore(that.$element);
          //PATCH END

          that.$element.show()

          if (transition) {
            that.$element[0].offsetWidth // force reflow
          }

          that.$element
            .addClass('in')
            .attr('aria-hidden', false)

          that.enforceFocus()

          //PATCH START - trigger custom event when modal is done being shown
          //PATCH START 2 - took out .focus() to fix the two-modals-at-once bug
          transition ?
            that.$element.one($.support.transition.end, function () { that.$element/*.focus()*/.trigger('shown').closest('body').trigger('modal-shown', that.$element) }) :
            that.$element/*.focus()*/.trigger('shown').closest('body').trigger('modal-shown', that.$element)
          //PATCH END 2
          //PATCH END

          // transition ?
          //   that.$element.one($.support.transition.end, function () { that.$element.focus().trigger('shown') }) :
          //   that.$element.focus().trigger('shown')
        })
      }

    , hide: function (e) {
        e && e.preventDefault()

        var that = this

        //PATCH START - class added to body when modal is open needs removing
        // moved here from 'hideModal' because we need to do this before Bootstrap does its event handling things
        $('body').removeClass('modal-open');
        $('html').removeClass('modal-open');
        //PATCH END

        e = $.Event('hide')

        this.$element.trigger(e)

        if (!this.isShown || e.isDefaultPrevented()) return

        this.isShown = false

        this.escape()

        $(document).off('focusin.modal')

        this.$element
          .removeClass('in')
          .attr('aria-hidden', true)

        $.support.transition && this.$element.hasClass('fade') ?
          this.hideWithTransition() :
          this.hideModal()
      }

    , enforceFocus: function () {
        var that = this
        $(document).on('focusin.modal', function (e) {
          if (that.$element[0] !== e.target && !that.$element.has(e.target).length) {
            that.$element.focus()
          }
        })
      }

    , escape: function () {
        var that = this
        if (this.isShown && this.options.keyboard) {
          this.$element.on('keyup.dismiss.modal', function ( e ) {
            e.which == 27 && that.hide()
          })
        } else if (!this.isShown) {
          this.$element.off('keyup.dismiss.modal')
        }
      }

    , hideWithTransition: function () {
        var that = this
          , timeout = setTimeout(function () {
              that.$element.off($.support.transition.end)
              that.hideModal()
            }, 500)

        this.$element.one($.support.transition.end, function () {
          clearTimeout(timeout)
          that.hideModal()
        })
      }

    , hideModal: function () {
        var that = this
        this.$element.hide()
        this.backdrop(function () {
          that.removeBackdrop()
          that.$element.trigger('hidden')

          //PATCH START - trigger custom event when modal has finished closing
          $('body').trigger('modal-hidden', that.$element).removeClass('modal-stack-open');
          $('html').removeClass('modal-stack-open');
          //PATCH END

          //PATCH START - stack modal is always moved to the end of body when opened so we move it back here
          if( that.$placeholder ) {
              that.$placeholder.replaceWith( that.$element );
              that.$placeholder = null;
          }
          //PATCH END
        })
      }

    , removeBackdrop: function () {
        this.$backdrop && this.$backdrop.remove()
        this.$backdrop = null
      }

    , backdrop: function (callback) {
        var that = this
          , animate = this.$element.hasClass('fade') ? 'fade' : ''

        if (this.isShown && this.options.backdrop) {
          var doAnimate = $.support.transition && animate

          this.$backdrop = $('<div class="modal-backdrop ' + animate + '" />')
            .appendTo(document.body)

          this.$backdrop.click(
            this.options.backdrop == 'static' ?
              $.proxy(this.$element[0].focus, this.$element[0])
            : $.proxy(this.hide, this)
          )

          if (doAnimate) this.$backdrop[0].offsetWidth // force reflow

          this.$backdrop.addClass('in')

          if (!callback) return

          doAnimate ?
            this.$backdrop.one($.support.transition.end, callback) :
            callback()

        } else if (!this.isShown && this.$backdrop) {
          this.$backdrop.removeClass('in')

          $.support.transition && this.$element.hasClass('fade')?
            this.$backdrop.one($.support.transition.end, callback) :
            callback()

        } else if (callback) {
          callback()
        }
      }
  }


 /* MODAL PLUGIN DEFINITION
  * ======================= */

  var old = $.fn.modal

  $.fn.modal = function (option) {
    return this.each(function () {
      var $this = $(this)
        , data = $this.data('modal')
        , options = $.extend({}, $.fn.modal.defaults, $this.data(), typeof option == 'object' && option)
      if (!data) $this.data('modal', (data = new Modal(this, options)))
      if (typeof option == 'string') data[option]()
      else if (options.show) data.show()
    })
  }

  $.fn.modal.defaults = {
      backdrop: true
    , keyboard: true
    , show: true
  }

  $.fn.modal.Constructor = Modal


 /* MODAL NO CONFLICT
  * ================= */

  $.fn.modal.noConflict = function () {
    $.fn.modal = old
    return this
  }


 /* MODAL DATA-API
  * ============== */

  $(document).on('click.modal.data-api', '[data-toggle="modal"]', function (e) {
    var $this = $(this)
      , href = $this.attr('href')
      , $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) //strip for ie7
      , option = $target.data('modal') ? 'toggle' : $.extend({ remote:!/#/.test(href) && href }, $target.data(), $this.data())

    e.preventDefault()

    $target
      .modal(option)
      .one('hide', function () {
        $this.focus()
      })
  })

}(window.jQuery);
