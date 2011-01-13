Summary

    * Status: Fix the name of the Runtime Option
    * CCP Issue: CCP-296, Product Jira Issue: PC-519.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
PLT.10.4.3 Runtime Option javax.portlet.servletDefaultSessionScope
In our code we use old name "javax.portlet.includedPortletSessionScope"
Fix description

How is the problem fixed?

Change the option from javax.portlet.includedPortletSessionScope to javax.portlet.servletDefaultSessionScope.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PC-519.patch

Tests to perform
Tests performed at DevLevel
* JUnit and TCK

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* none

Configuration changes

Configuration changes:
* none

Will previous configuration continue to work?
* yes

Risks and impacts

Can this bug fix have any side effects on current client projects?
    * Function or ClassName change: none

Is there a performance risk/cost?
*

Validation (PM/Support/QA)

PM Comment
* Patch approved by the PM

Support Comment
* Support review: patch validated

QA Feedbacks
*

