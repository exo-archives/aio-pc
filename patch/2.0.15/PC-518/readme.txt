Summary

    * Status: Problems with cache-portlet (timestamp changes)
    * CCP Issue: N/A, Product Jira Issue: PC-518.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Problems with cache-portlet (timestamp changes)

Fix description

How is the problem fixed?

    * Use getSession() instead of getSession(false)

Patch file: PC-518.patch

Tests to perform

Reproduction test
Use case steps:

   1. Login to the Test portlet
   2. Check the demos/CachePortlet on the portal page
   3. you'll see the timestamp in the first rendered portlet's markup, remember it.
   4. click 'view' to reload page
   5. timestamp changed, but timestamp must NOT change until the cache time is not expires (300sec = 5 minutes).

Timestamp changed before cache time expires.

Tests performed at DevLevel
* JUnit and manual tests on Test Portal

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* nothing

Configuration changes

Configuration changes:
* nothing

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
* Patch validated

QA Feedbacks
*

