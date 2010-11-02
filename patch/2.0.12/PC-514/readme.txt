Summary

    * Status: Problem when rendering parameters to Struts portlet
    * CCP Issue: CCP-620, Product Jira Issue: PC-514.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Add a new page in which you insert a Struts portlet (StrutsPortlet) and a normal portlet (NormalPortlet)
   1. Click on the delete icon of NormalPortlet
   2. Exception appears in StrutsPortlet.

Fix description

How is the problem fixed?

    * Revert the PC-501 commit (r.45414)
    * Modify ServletContextProviderImpl.getHttpServletRequest. 
      In RENDER phase, set parameterMap and parse the StrutsPageURL.
    * Modify CustomRequestWrapper to take into account Struts portlet.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch files: PC-514.patch

Tests to perform

Reproduction test
* Cf. above  

Tests performed at DevLevel
* 

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
*

Configuration changes

Configuration changes:
*

Will previous configuration continue to work?
*

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
*

Validation (PM/Support/QA)

PM Comment
*

Support Comment
*

QA Feedbacks
*

