Summary

    Status: Struts portlets answering to requests from other portlets
    CCP Issue: CCP-613, Product Jira Issue: PC-513.
    Complexity: LOW

The Proposal
Problem description

What is the problem to fix?

    To reproduce the problem you should follow these steps:

    Add the 2 portlet wars in the attached file to the server and run the server.
    Login
    Groups->administration->application registry and auto import.
    Site Editor->add Page Wizard create a new page and add to it one or two instances of TODOseguro portlet in addition to TODO Struts portlet and save the page.
    Navigate to the page newly created and for example push a button in the TODOseguro Portlet.
    The expected result is that only TODOseguro Portlet will respond to button click in her way.

But actually, you will notice that TODO Struts is acting like responding to the request sent to TODOseguro Portlet. So, the 2 portlets have the same behiavor despite the request is sent only to TODOseguro portlet.

This problem is noticed also if we replace the TODOseguro portlet with any other portlet.

The attached files.zip contains the TODOseguro portlet(cap5-lab3b.war),the TODO Struts portlet(cap7Struts-lab2.war) and a video showing the problem(out.ogv).
Fix description

How is the problem fixed?

    Added extra checking that incoming parameter is intended to this portlet window and not belongs to other portlets.

Patch information:
Patch files: PC-513.patch

Tests to perform

Reproduction test
    see above

Tests performed at DevLevel
    No

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
    No

Configuration changes

Configuration changes:
    No

Will previous configuration continue to work?
    Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?
    No

Is there a performance risk/cost?
    No

Validation (PM/Support/QA)

PM Comment
*

Support Comment
*

QA Feedbacks
*

