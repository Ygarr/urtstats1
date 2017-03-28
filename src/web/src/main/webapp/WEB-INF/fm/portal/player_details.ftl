<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
 <#-- Player details -->
  <#local playerStats=playerStats!{}/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="ps_list_error_"/>  <#t/>
  </#if>  <#t/>
  <#if playerStats?has_content>
    <div class="table_title"><@c.msg "portal.player_details.page.player.table.title" /></div>  <#t/>
    <table class="attr_table">  <#t/>
      <tr>  <#t/>
        <td><@c.msg "portal.player_details.page.player.table.header_player_name"/></th>  <#t/>
        <td style="text-align: right"><b>${playerStats.playerName?html}</b></td>  <#t/>
      </tr>
      <tr>  <#t/>
        <td style=""><@c.msg "portal.player_details.page.player.table.header_avg_kd_ratio"/></th>  <#t/>
        <td style="text-align: right">
          <a href="<@spring.url ("/portal/chart/player_rate?id=" + playerStats.id?c) />" target="_blank" title="<@c.msg "portal.player_list.page.player.table.header_avg_kd_ratio_hint"/>" >${playerStats.avgKDRatio?string}</a>  <#t/>
        </td>  <#t/>
      </tr>
      <tr>  <#t/>
        <td style=""><@c.msg "portal.player_details.page.player.table.header_kd_ratio"/></th>  <#t/>
        <td style="text-align: right"><b>${playerStats.totalKDRatio?string}</b></td>  <#t/>
      </tr>
      <tr>  <#t/>
        <td style=""><@c.msg "portal.player_details.page.player.table.header_kills"/></th>  <#t/>
        <td style="text-align: right"><b>${playerStats.kills?c}</b></td>  <#t/>
      </tr>
      <tr>  <#t/>      
        <td style=""><@c.msg "portal.player_details.page.player.table.header_deaths"/></th>  <#t/>
        <td style="text-align: right"><b>${playerStats.deaths?c}</b></td>  <#t/>
      </tr>
      <tr>  <#t/>
        <td style=""><@c.msg "portal.player_details.page.player.table.header_games"/></th>  <#t/>
        <td style="text-align: right"><b>${playerStats.gamesPlayed?c}</b></td>  <#t/>
      </tr>  <#t/>
      <tr>  <#t/>
        <td style=""><@c.msg "portal.player_details.page.player.table.header_last_played"/></th>  <#t/>
        <td style="text-align: right"><#if playerStats.lastGameTime?has_content><b>${playerStats.lastGameTime?date}</b><#else>&nbsp;</#if></td>  <#t/>
      </tr>  <#t/>
    </table>  <#t/>
  </#if>
  <br/>
  <table class="col_layout">
    <#-- ROW 1 -->
    <tr>
      <#-- First col -->
      <td style="width: 35%">
          <#-- Player match history -->
          <#local matchHistDivId = "match_history_container">
          <#local matches=(matchList![])>
          <#local matchCount=(pager.recordCount)!0/>
          <#if ((matchError!{})?has_content)>  <#t/>
            <@c.handleError errorDetails=matchError id="match_list_error_"/>  <#t/>
          </#if>  <#t/>
        <#if matches?has_content && playerStats?has_content>
          <div class="table_title">
            <@c.msg "portal.player_details.page.match.table.title" />&nbsp;(${matchCount?c})  <#t/>
            <img id="${matchHistDivId}_wait_img" src="<@spring.url "/img/wait.gif"/>" style="display: none;"/>  <#t/>
          </div>  <#t/>
          <@c.drawMatchTable matchList=matches pageUrl="/portal/ajax/pmhist?player_id=${playerStats.id?c}&page=" onClickUrl="/portal/match_details?id=" pager=pager containerId="${matchHistDivId}" async=true sort=sort/>  <#t/>
          <script type="text/javascript">
            function onMatchTableReload(/*String*/ ajaxUrl, /*String*/ divId){
              var divElt = document.getElementById(divId);
              if(divElt && divElt.isLocked) {
                return;
              }
              divElt.isLocked = true;
              var waitImgElt = document.getElementById(divId + "_wait_img");
              waitImgElt.style.display = "block";
              executeHtmlAjaxRequest(ajaxUrl, divId, true);
            }
          </script>
        </#if>
        </td>
      <#-- Second col -->
        <td style="width: 65%">
          <#if ((wpnsError!{})?has_content)>  <#t/>
            <@c.handleError errorDetails=wpnsError id="wpns_error_"/>  <#t/>
          </#if>  <#t/>
          <#if weapons?has_content && playerStats?has_content>
            <div class="table_title">
              <@c.msg "portal.player_details.page.wpns.table.title" />  <#t/>
            </div>  <#t/>
            <#local totalKills = playerStats.kills />
              <table class="blank_table">
              <tr>
              <#list weapons?keys as weaponType>  <#t/>
                <td style="padding-right: 5px;">
                <table class="attr_table">  <#t/>
                  <tr>  <#t/>
                    <td class="borders th">  <#t/>
                      <@c.msg "enum.weapon_type." + weaponType />  <#t/>
                    </td>  <#t/>
                    <td class="borders th" style="text-align: right; padding: 0 2px 0 2px;">  <#t/>
                      <@c.msg "portal.player_details.page.wpns.table.header.kills" />  <#t/>
                    </td>  <#t/>
                  </tr>
                  <#local wpnList = (weapons[weaponType]![])/>
                  <#list wpnList as wpnStat>  <#t/>
                   <#local kills = wpnStat.kills />
                   <#local percent = (kills * 100) / totalKills />
                   <#local percentFormat = "0.##"/>
                   <#if (percent > 10) >
                     <#local percentFormat = "0.#"/>
                   </#if>
                   <#local wpnImgUrl><@spring.url "/img/weapon/128x44/" + wpnStat.weapon.toString()?lower_case + ".png" /></#local>
                   <tr>  <#t/>
                     <td class="borders" style="text-align: center; vertical-align: top; background-color: grey; color: black;">  <#t/>
                       <@c.msg "enum.weapon." + wpnStat.weapon.toString() /><#t/><br/><#t/>
                       <img src="${wpnImgUrl}" />  <#t/>
                     </td>  <#t/>
                     <td class="borders" style="text-align: right; vertical-align: top; padding: 0 2px 0 2px;">  <#t/>
                       <b>${kills?c}</b> <#t/><br/><br/>
                       ${percent?string(percentFormat)}% <#t/>
                     </td>  <#t/>
                   </tr>
                  </#list>  <#t/>
                </table>  <#t/>
                </td>
              </#list>  <#t/>
              </tr>
            </table>
          </#if>
        </td>
      </tr>
    <#-- ROW 3 -->
    <tr>
      <#-- First col -->
      <td>
        <div class="table_title">  <#t/>
          <@c.msg "portal.player_details.page.player_hits"/>  <#t/>
        </div>  <#t/>
        <#if ((bestHitsError!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=bestHitsError id="hit_list_error_"/>  <#t/>
        </#if>  <#t/>
        <#if playerStats?has_content && hitStatList?has_content>
          <#local totalHits = 0 />
          <#list (hitStatList![]) as hitStat>
            <#local totalHits = totalHits + hitStat.hitCount />
          </#list>
          <table class="attr_table">
            <tr>
              <#list (hitStatList![]) as hitStat>
                <th class="borders"  style="text-align: right"><@c.msg ("enum.hit_target." + hitStat.target)/></th>
              </#list>
              <th class="borders"  style="text-align: right">&nbsp;</th>
            </tr>
            <tr>
              <#list (hitStatList![]) as hitStat>
                <#local percent = (hitStat.hitCount * 100) / totalHits />
                <#local percentFormat = "0.##"/>
                <#if (percent > 10) >
                  <#local percentFormat = "0.#"/>
                </#if>
                <td class="borders" style="text-align: right">  <#t/>
                  <b>${hitStat.hitCount?c}</b><br/><br/>  <#t/>
                  ${percent?string(percentFormat)}% <#t/>
                </td>  <#t/>
              </#list>
                <td class="borders" style="text-align: right">  <#t/>
                  ${totalHits?c}
                </td>
            </tr>
          </table>
        </#if>
      </td>
      <#-- First col -->
      <td>
        
        <div class="table_title">  <#t/>
          <@c.msg "portal.player_details.page.player_achvs"/>  <#t/>
        </div>  <#t/>
        <#if ((bestAchvsError!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=bestAchvsError id="achv_list_error_"/>  <#t/>
        </#if>  <#t/>
        <#if achvList?has_content>
          <table class="attr_table">
              <#list (achvList![]) as achv>
                <tr>
                  <td >  <#t/>
                     <#if achv.achievementType.isPositive()>  <#t/>
                       <img src="<@spring.url "/img/portal/achv_pos.png"/>" />  <#t/>
                     <#else>  <#t/>
                       <img src="<@spring.url "/img/portal/achv_neg.png"/>" />  <#t/>
                     </#if>  <#t/>
                  </td> <#t/>
                  <td style="padding: 2px 0 0 5px; border: solid black; border-width: 0 0 1px 0;"><@c.msg ("enum.count_achv." + achv.achievementType)/>:</td>
                  <td style="padding: 2px 0 0 5px; border: solid black; border-width: 0 0 1px 0; text-align: right; font-weight: bold;">${achv.achvCount?c}</td>
                </tr>
              </#list>
          </table>
        </#if>
      </td>
    </tr>
    </table>
        
</#macro>

<#-- Drawing page layout -->
<#assign pageTitleSuffix = ": " + ((playerStats.playerName)!"")?html/>
<@pageLayout pageTitle="portal.player_details.page.title" activeMenuItem="menu_players" pageTitleSuffix=pageTitleSuffix/>
