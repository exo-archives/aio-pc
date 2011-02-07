Summary

    * Status: Fix the Portlet.addContainerRuntimeOption method (NullPointerException) and the method XMLParser.readContainerRuntimeOption
    * CCP Issue: CCP-296, Product Jira Issue: PC-520.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
* If local containerRuntimeOption field was not initialized, the exception appears

Fix description

How is the problem fixed?
* Check this.containerRuntimeOption == null (checked containerRuntimeOption1, which is the method parameter)

Patch file: PC-520.patch

Tests to perform

Tests performed at DevLevel
* JUnit and TCK (update TestParser.java)

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* None

Configuration changes

Configuration changes:
* None

Will previous configuration continue to work?
* Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?
* No

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM.

Support Comment
* Support Review: patch validated

QA Feedbacks
*
