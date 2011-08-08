<#-- TODO: PRODUCTION: Remove. -->
<#global "action.code" = "Подделка"/>
<#include "confirm-email.ftl"/>
<#--
Hi!
This is test email.

Encoded URL + hash map of params test.
------------------------------------------------
URL = ${action.buildUrl("test.action", {"email":"user@example.com", "ruText":"Привет Мир!"})}


FreeMarker vars in text test.
------------------------------------------------
${action.getText("msgkey.text.test.fmVars")}
${action.getText("msgkey.text.test.ognlVars")}


UTF-8 test.
------------------------------------------------
en: Hello, ${name}!
ru: Привет, ${name}!


"action" test.
------------------------------------------------
"action" = [${action}]
"action.to" = [${action.to}]
"action.subject" = [${action.subject}]
"action.name" = [${action.name}]


--
Thanks,
The Struts2Shop Team
http://www.site.com
-->