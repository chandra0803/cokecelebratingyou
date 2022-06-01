import React from 'react';

class RecAdvisorModalFooter extends React.Component {
  constructor( props ) {
    super( props );
  }

  printRARecognition( e ) {
    const origin = window.location.origin;
    const loc = window.location.pathname;
    const context = loc.split( '/' )[ 1 ];
    const claimUrl = e.target.getAttribute( 'data-url' );
    const url = `${origin}/${context}/${claimUrl}`;
    window.location.href = url;
  }

  render() {

    const printUrl = this.props.recognitionAdvisor.raClaimDetail;
    const {
      content
    } = this.props;

    return(
      <div id="ModalFooter" className="modal-footer">
      <table className="table table-striped">
        <tbody>
          <tr>
            <td>
              <button type="button" className="btnRecognize btn btn-primary btn-block flipButton" onClick={ ( e ) => this.props.removeComponent() }>{ content[ 'system.button.ok' ] }</button>
            </td>
            <td>
              <button type="button" className="btnRecognize btn default btn-block flipButton" data-url={ printUrl } onClick={ ( e ) => this.printRARecognition( e ) }>{ content[ 'recognition.confirmation.print_copies' ] }</button>
            </td>
          </tr>
        </tbody>
      </table>
      </div>
    );
  }
}

export default RecAdvisorModalFooter;
