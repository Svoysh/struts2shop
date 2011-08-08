<#include "header.ftl"/>

${action.getText("msgkey.email.confirmEmail.followTheLink")}

${action.buildUrl("confirm-email!submit.action", {"code":code})}

<#include "footer.ftl"/>