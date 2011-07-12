Summary

    * Status: NPE caused by an unimplemented method in StateAwareResponseImp
    * CCP Issue: N/A Product Jira Issue: PC-528.
    * Complexity: low

The Proposal
Problem description

What is the problem to fix?
The class org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.StateAwareResponseImp contains an unimplemented method:

/**
   * Overridden method.
   *
   * @return render parameters
   * @see javax.portlet.StateAwareResponse#getRenderParameterMap()
   */
  public final Map getRenderParameterMap() {
    // TODO
    return null;
  }

The newest version of the spring portlet mvc framework (3.0.5) uses this method in its exception handling code.

The contract on javax.portlet.StateAwareResponse.getRenderParameterMap() doesn't say null is a valid return value for this method.

Fix description

How is the problem fixed?

    *  Added method implementation

Patch files: PC-528.patch

Tests to perform

Reproduction test
*

Tests performed at DevLevel
* none

Tests performed at QA/Support Level
* none

Documentation changes

Documentation changes:
* none

Configuration changes

Configuration changes:
* None

Will previous configuration continue to work?
* Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
* none

Validation (PM/Support/QA)

PM Comment
*

Support Comment
* Patch validated

QA Feedbacks
*

