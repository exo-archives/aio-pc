Summary

    * Status: RenderResponse.setContentType still be compolsory before applying getWriter
    * CCP Issue: CCP-676, Product Jira Issue: PC-522.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
According to PLT.2.5 of Portlet specification JSR-286, the call of RenderResponse.setContentType is no longer required before calling getWriter comparing to JSR-168.
In the attched portlet We commented the

response.setContentType("text/html");

in doFilter method of AdminFilter filter. We got this exception:

[ERROR] portal:UIPortletLifecycle - The portlet null could not be loaded. Check if properly deployed. <java.lang.IllegalStateException: the content type has not been s
et before calling thegetWriter() method.>java.lang.IllegalStateException: the content type has not been set before calling thegetWriter() method.
        at org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.MimeResponseImp.getWriter(MimeResponseImp.java:139)
        at net.guto.pocfilter.AdminFilter.doFilter(AdminFilter.java:27)

Fix description

How is the problem fixed?

    * Removed exception throwing, added content-type detection from request.

Patch information:
Patch files: PC-522.patch 	  	

Tests to perform

Reproduction test
* Test portal + pocfilter webapp;

Tests performed at DevLevel
* Test portal + pocfilter webapp;

* TCK tests;

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* None;

Configuration changes

Configuration changes:
* None;

Will previous configuration continue to work?
* Yes;

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
* N/A

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Validated

QA Feedbacks
*

