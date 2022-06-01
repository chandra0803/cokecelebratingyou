import React from 'react';

class RecAdvisorModalHeader extends React.Component {
  constructor( props ) {
    super( props );
  }

    render() {

      const {
        content
      } = this.props;

      return(
       <div id="ModalHeader" className="modal-header">
       <button data-dismiss="modal" className="close" type="button" onClick={ ( e ) => this.props.removeComponent() }><i className="icon-close"></i></button>
       <h2 className="module-title">{ content[ 'recognition.confirmation.thank_you' ] }</h2>
       <p><b>{ content[ 'recognition.confirmation.submitted_message' ] }</b></p>
      </div>
    );
  }
}

export default RecAdvisorModalHeader;
