<#import "/spring.ftl" as spring />
<#import "../../common.ftl" as c />

  <#assign matches=(matchList![])/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
  </#if>  <#t/>
  <@c.drawMatchTable matchList=matches pageUrl="/portal/ajax/pmhist?player_id=${(playerId!-1)?c}&page=" onClickUrl="/portal/match_details?id=" pager=pager containerId="match_history_container" async=true sort=sort/>  <#t/>
