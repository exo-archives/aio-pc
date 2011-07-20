Summary

    * Status: Problem when computing Mean MBeans attributes' for Portlets
    * CCP Issue: CCP-908, Product Jira Issue: PC-529.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

To reproduce the problem, You should add the JMX options and run the server.
Then connect with JConsole to the server
Inside eXo MBean, attributes for each portlet are defined. All Mean Values are set to 0
We foccused on the problem, We found that compute method of PortletManaged is gathering values from ExoContainerContext. In The PortletRuntimeData values are not setted ex: ActionExecutionTime, EventExecutionTime.
Fix description

How is the problem fixed?

    * Add 'count' incrementation for the 'operate' method.

Patch file: PC-529.patch

Tests to perform

Reproduction test
* Jconsole with portal portlet ApplicationRegistry

Tests performed at DevLevel
* Jconsole with portal portlet ApplicationRegistry

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
* Nothing

Configuration changes

Configuration changes:
* Nothing

Will previous configuration continue to work?
* yes
Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
* no

Validation (PM/Support/QA)

PM Comment
*

Support Comment
* Patch validated.

QA Feedbacks
*

