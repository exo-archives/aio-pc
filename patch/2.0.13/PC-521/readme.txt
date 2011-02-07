Summary

    * Status: Compliance of PortletFilter with JSR-286 specification
    * CCP Issue: CCP-675, Product Jira Issue: PC-521.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
According to PLT.20.2.1 of JSR-286 specification, when doFilter method of FilterChain is called from the doFilter of calling filter, two use cases are given:

   1. If there are more filters to execute, the doFilter of the next filter is applied.
   2. If there is no more filter to execute, the targeted method of the portlet is called ref (PLT.20.2.1-4)

This rule is however not respected in our test (attached to PC-521): the Portlet has two filters.
The first use case is respected on it but the second one is not.
The last filter (SecondFilter) doFilter method is applying the doFilter of FilterChain. The targeted method of the portlet was not executed.

Fix description

How is the problem fixed?
* Refactor FilterChainImpl to make lifecycle correct.

Patch file: PC-521.patch

Tests to perform

Reproduction test
* Tests on test-portal with pocfilter app.

Tests performed at DevLevel
* Tests on test-portal with pocfilter app.

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* None

Configuration changes

Configuration changes:
* None

Will previous configuration continue to work?
* Yes.

Risks and impacts

Can this bug fix have any side effects on current client projects?
* Function or ClassName change: no

Is there a performance risk/cost?
* No.

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM.

Support Comment
* Support Team Review: proposed patch validated

QA Feedbacks
*

