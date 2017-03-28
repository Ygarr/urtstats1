<#import "../common.ftl" as c />
<#include "page.ftl"/>



<#-- PAGE NAVIGATION NESTED CONTENT  -->  <#t/>
<#macro page_nav>

</#macro>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
    <#local matches = (matchList)![] >  <#t/>
    <div class="table_title"><@c.msg "portal.main.page.match.table.title" />&nbsp;(${matches?size?c})</div>  <#t/>
    <#if ((error!{})?has_content)>  <#t/>
      <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
    </#if>  <#t/>
    <@c.drawMatchTable pageUrl="/portal?page=" matchList=matches onClickUrl="/portal/match_details?id=" sort=sort/>  <#t/>
</#macro>

<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.main.page.title" activeMenuItem="menu_main"/>
