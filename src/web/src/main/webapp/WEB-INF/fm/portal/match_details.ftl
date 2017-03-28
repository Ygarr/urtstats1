<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
  <script type="text/javascript">
    function onClickShowPlayerHits(/*long*/ playerMatchId){
      var divId = "pm_hits_" + playerMatchId;
      var divElt = document.getElementById(divId);
      if(divElt.style.display == "none"){
        divElt.style.display = "block";
      } else {
        divElt.style.display = "none";
        return;
      }
      if(divElt && !divElt.isLoaded){
        if(divElt.isLocked) {
          return;
        }
        divElt.isLocked = true;
        var ajaxUrl = "<@spring.url "/portal/ajax/pmhits?player_match_id=" />" + playerMatchId;
        executeHtmlAjaxRequest(ajaxUrl, divId, false);
      }
    }
    
    function onClickShowPlayerAchvs(/*long*/ playerMatchId){
      var divId = "pm_achvs_" + playerMatchId;
      var divElt = document.getElementById(divId);
      if(divElt.style.display == "none"){
        divElt.style.display = "block";
      } else {
        divElt.style.display = "none";
        return;
      }
      if(divElt && !divElt.isLoaded){
        if(divElt.isLocked) {
          return;
        }
        divElt.isLocked = true;
        var ajaxUrl = "<@spring.url "/portal/ajax/pmachvs?player_match_id=" />" + playerMatchId;
        executeHtmlAjaxRequest(ajaxUrl, divId, false);
      }
    }
  </script>
  

<#local match=match!{}/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="match_error_"/>
  </#if>  <#t/>
<#if match?has_content>
  <table class="col_layout">
    <#-- ROW 1 -->
    <tr>
      <#-- First col -->
      <td style="width: 30%">
 
        <#-- Match attributes -->
         <div class="table_title"><@c.msg "portal.match_details.page.match_details"/></div>  <#t/>
         <table class="attr_table">
           <tr>
             <td><@c.msg "portal.match_details.page.match_start_time"/></td>
             <td><b>${match.startTime?datetime}</b></td>
           </tr>
           <tr>
             <td><@c.msg "portal.match_details.page.match_end_time"/></td>
             <td><b>${match.endTime?datetime}</b></td>
           </tr>
           <tr>
             <td><@c.msg "portal.match_details.page.match_game_type"/></td>
             <td><b><@c.msg messageCode=("enum.game_type." + match.gameType)/></b></td>
           </tr>
          <#local mapHref = ""/>
          <#if (match.map)?has_content>
            <#local mapHref><@spring.url ("/portal/map_details?id=" + match.map.id?c) /></#local>
          </#if>
           <tr>
             <td><@c.msg "portal.match_details.page.match_map_name"/></td>
             <td><a href="${mapHref}">${((match.map.name)!"unknown")?html}</a></td>
           </tr>
           <tr>
             <td><@c.msg "portal.match_details.page.log_file_name"/></td>
             <td><b>${match.logFileName}</b></td>
           </tr>
         </table>
      </td>
      <#-- Second col -->
      <td style="width: 30%">
        <#-- Teams -->
         <#if match.isTeamBasedMatch()>  <#t/>
           <#if (match.getTeamList())?has_content>  <#t/>
             <#local winnerTeam = ""/>  <#t/>
             <#local maxKills = 0 />  <#t/>
             <#list (match.getTeamList()![]) as team>  <#t/>
               <#local kills = team.kills />  <#t/>
               <#if (kills > maxKills)>  <#t/>
                 <#local winnerTeam = (team.teamType?string) />  <#t/>
                 <#local maxKills = kills />
               <#elseif kills = maxKills>  <#t/>
                 <#local winnerTeam = "both" />  <#t/>
               </#if>  <#t/>
             </#list>  <#t/>
             <div class="table_title"><@c.msg "portal.match_details.page.teams"/></div>  <#t/>
             <table class="attr_table">
               <tr>
                 <th colspan="2" class="borders"><@c.msg "portal.match_details.page.teams.team"/></th>
                 <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.teams.kills"/></th>
                 <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.teams.deaths"/></th>
                 <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.teams.ratio"/></th>
               </tr>
               <#list (match.getTeamList()![]) as team>
                 <#local teamType = team.teamType?string />
                 <tr>
                   <td class="borders team_${teamType}">  <#t/>
                     <#if winnerTeam = teamType>  <#t/>
                       <img src="<@spring.url "/img/portal/award.png"/>" title="<@c.msg "portal.match_details.page.teams.winner"/>"/><#t/>
                     <#elseif winnerTeam = "both">  <#t/>
                       <img src="<@spring.url "/img/portal/award.png"/>" title="<@c.msg "portal.match_details.page.teams.tie"/>"/>  <#t/>
                     <#else>  <#t/>
                       <img size="16" src="<@spring.url "/img/blank.png"/>"/>  <#t/>
                     </#if>
                   </td>  <#t/>
                   <td class="borders team_${teamType}"><@c.msg ("enum.team." + teamType)/>  <#t/></td>
                   <td class="borders" style="text-align: right">${team.kills?c}</td>
                   <td class="borders" style="text-align: right">${team.deaths?c}</td>
                   <td class="borders" style="text-align: right">${team.getKDRatio()?string}</td>
                 </tr>
               </#list>
             </table>
           <#else>
           </#if>
         </#if>
       </td>
       <#-- Third col -->
      <td style="width: 40%">
        &nbsp;
      </td>
    </tr>
    <#-- ROW 2 -->
    <tr>
      <#-- First col -->
      <td>
      <#-- Players general stats-->
        <div class="table_title"><@c.msg "portal.match_details.page.players"/></div>  <#t/>
        <table class="attr_table">
          <tr>
            <th class="borders" <#if !match.isTeamBasedMatch()>colspan="2"</#if>><@c.msg "portal.match_details.page.player.name"/></th>
            <#if match.isTeamBasedMatch()>
              <th class="borders"><@c.msg "portal.match_details.page.player.team"/></th>
            </#if>
            <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.player.ratio"/></th>
            <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.player.kills"/></th>
            <th class="borders" style="text-align: right"><@c.msg "portal.match_details.page.player.deaths"/></th>
            <th class="borders">&nbsp;</th>
          </tr>
          <#local maxKills = 0>
          <#local minDeaths = 999999>
          <#list (match.getPlayerList()![]) as player>
            <#if player_index = 0>
              <#local maxKills = player.kills?c>
              <#local minDeaths = player.deaths?c>
            </#if>
            <tr>
              <#if !match.isTeamBasedMatch()>
                <td class="borders" style="border-right-width: 0;"><#t/>
                  <#if (maxKills = player.kills?c) && (minDeaths = player.deaths?c)>  <#t/><#t/>
                    <img src="<@spring.url "/img/portal/award.png"/>" title="<@c.msg "portal.match_details.page.teams.winner"/>"/>  <#t/>
                  </#if><#t/>
                </td><#t/>
              </#if>
              <td class="borders" <#if !match.isTeamBasedMatch()>style="border-left-width: 0;"</#if>>
                <a href="<@spring.url "/portal/player_match_details?player_match_id=" + player.id?c/>">${player.playerName?html}</a>
              </td>
              <#if match.isTeamBasedMatch()>
                <td class="borders team_${(player.playerTeam)!""}"><#if (player.playerTeam)?has_content><@c.msg ("enum.team." + player.playerTeam)/></#if></td>
              </#if>
              <td class="borders" style="text-align: right">${player.getKDRatio()?string}</td>
              <td class="borders" style="text-align: right">${player.kills?c}</td>
              <td class="borders" style="text-align: right">${player.deaths?c}</td>
              <td class="borders">  <#t/>
                  <#if player.hasHits()>  <#t/>
                    <img style="cursor: pointer" src="<@spring.url "/img/portal/target.png"/>" onClick="onClickShowPlayerHits(${player.id?c});" title="<@c.msg "portal.match_details.page.player.show_hits"/>"/>  <#t/>
                  <#else>  <#t/>
                    <img src="<@spring.url "/img/portal/target_disabled.png"/>" title="<@c.msg "portal.match_details.page.player.no_hits"/>"/>  <#t/>
                  </#if>  <#t/>
                  <#if player.hasAchvs()>  <#t/>
                    <img style="cursor: pointer" src="<@spring.url "/img/portal/medal.png"/>" onClick="onClickShowPlayerAchvs(${player.id?c});" title="<@c.msg "portal.match_details.page.player.show_achvs"/>"/>  <#t/>
                  <#else>  <#t/>
                    <img src="<@spring.url "/img/portal/medal_disabled.png"/>" title="<@c.msg "portal.match_details.page.player.no_achvs"/>"/>  <#t/>
                  </#if>  <#t/>
                </nobr>  <#t/>
              </td>  <#t/>
            </tr>
          </#list>
        </table>
        <br/>
        <#-- Best hits -->
        <div class="table_title">  <#t/>
           <@c.msg "portal.match_details.page.best_hits"/>  <#t/>
        </div>  <#t/>
        <#if ((bestHitsError!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=bestHitsError id="bh_error_"/>
        </#if>  <#t/>
        <#if bestHits?has_content>
          <table class="attr_table">
            <#list bestHits as hitStat>
                <tr>
                  <td class="th borders" style="vertical-align: top;"><@c.msg ("enum.hit_target." + hitStat.target)/></td>
                  <td class="borders" style="padding: 2px 5px 2px 5px; text-align: right;vertical-align: top;">${hitStat.hitCount?c}</td>
                  <td class="borders" style="vertical-align: top;">  <#t/>
                    <#list (hitStat.players![]) as player>  <#t/>
                       <b>${player.playerName?html}</b><#if player_has_next><br/></#if>  <#t/>
                    </#list>  <#t/>
                  </td>  <#t/>
                </tr>
            </#list>
          </table>
        <#else>
          <@c.msg "portal.match_details.page.no_best_hits"/>  <#t/>
        </#if>
        <br/>
        <#-- Best achievs bestAchvs-->
        <div class="table_title">  <#t/>
           <@c.msg "portal.match_details.page.best_achvs"/>  <#t/>
        </div>  <#t/>
        <#if ((bestAchvsError!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=bestAchvsError id="ba_error_"/>
        </#if>  <#t/>
        <#if bestAchvs?has_content>
        <table class="attr_table">
            <#list bestAchvs as achv>
              <tr>
                <td class="borders" style="padding: 2px 0 0 0; vertical-align: top; border-width: 1px 0 1px 1px;">  <#t/>
                   <#if achv.achievementType.isPositive()>  <#t/>
                     <img src="<@spring.url "/img/portal/achv_pos.png"/>" />  <#t/>
                   <#else>  <#t/>
                     <img src="<@spring.url "/img/portal/achv_neg.png"/>" />  <#t/>
                   </#if>  <#t/>
                </td> <#t/>
                <td class="borders" style="vertical-align: top; border-width: 1px 1px 1px 0;"><@c.msg ("enum.count_achv." + achv.achievementType)/></td>
                <td class="borders" style="padding: 2px 5px 2px 5px; vertical-align: top; text-align: right;">${achv.achvCount?c}</td>
                <td class="borders" style="vertical-align: top;">  <#t/>
                    <#list (achv.players![]) as player>  <#t/>
                       <b>${player.playerName?html}</b><#if player_has_next><br/></#if>  <#t/>
                    </#list>  <#t/>
                </td>  <#t/>
              </tr>
            </#list>
        </table>
        <#else>
          <@c.msg "portal.match_details.page.no_best_achvs"/>  <#t/>
        </#if>
      </td>
      <#-- Second col -->
      <td>
        <#-- Player hit stats-->
        <#list (match.getPlayerList()![]) as player>
          <#-- player hits container -->
          <#local hitsDivId>pm_hits_${player.id?c}</#local>  <#t/>
          <div id="${hitsDivId}" style="display: none; padding: 0 0 10px 0;">  <#t/>
            <div class="table_title_normal">  <#t/>
              <b>${player.playerName?html}</b>&nbsp;<@c.msg "portal.match_details.page.player_hits"/>  <#t/>
              <img id="${hitsDivId}_wait_img" src="<@spring.url "/img/wait.gif"/>" />  <#t/>
            </div>  <#t/>
          </div>
        </#list>
      </td>
      <#-- Third col -->
      <td>
        <#-- Player achievements-->
        <#list (match.getPlayerList()![]) as player>
          <#-- player achvs container -->
          <#local achvsDivId>pm_achvs_${player.id?c}</#local>  <#t/>
          <div id="${achvsDivId}" style="display: none; padding: 0 0 10px 0;"">
            <div class="table_title_normal">  <#t/>
              <b>${player.playerName?html}</b>&nbsp;<@c.msg "portal.match_details.page.player_achvs"/>  <#t/>
              <img id="${achvsDivId}_wait_img" src="<@spring.url "/img/wait.gif"/>" />  <#t/>
            </div>  <#t/>
          </div>
        </#list>
      </td>
    </tr>
  </table>
</#if>
</#macro>

<#assign pageTitleSuffix = ": " + ((match.map.name)!"") + " " + ((match.startTime?datetime)!"")/>
<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.match_details.page.title" activeMenuItem="menu_matches" pageTitleSuffix=pageTitleSuffix/>
