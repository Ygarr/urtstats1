<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
    <#local matchCount=(pager.recordCount)!0/>
    <#local matches = (matchList)![] >  <#t/>
    <div class="table_title"><@c.msg "portal.match_list.page.match.table.title" />
      <#if matches?has_content && pager?has_content> <#t/>
        &nbsp;(${(pager.recordOffset + 1)?c}-${(pager.recordOffset + matchList?size)?c}&nbsp;/&nbsp;${matchCount?c})  <#t/>
      </#if>  <#t/>
    </div>  <#t/>

    <#if ((error!{})?has_content)>  <#t/>
      <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
    </#if>  <#t/>

    <@c.drawMatchTable matchList=matches pageUrl="/portal/matches?page=" onClickUrl="/portal/match_details?id=" pager=pager sort=sort/>  <#t/>
</#macro>

<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.match_list.page.title" activeMenuItem="menu_matches"/>
