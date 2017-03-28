<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
  <#local match=match!{}/>
  <#if ((matchError!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=matchError id="match_error_"/>
  </#if>  <#t/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="ps_error_"/>  <#t/>
  </#if>  <#t/>
<#if match?has_content && playerMatchStats?has_content>
  <table class="col_layout">
    <#-- ROW 1 -->
    <tr>
      <#-- First col -->
      <td style="width: 30%">
 
        <#-- Match attributes -->
         <div class="table_title"><@c.msg "portal.player_match_details.page.match_details"/></div>  <#t/>
         <table class="attr_table">
           <tr>
             <td><@c.msg "portal.player_match_details.page.player_name"/></td>
             <td>  <#t/>
               <a href="<@spring.url "/portal/player_details?id=" + playerMatchStats.playerId?c/>">${playerMatchStats.playerName?html}</a>  <#t/>
             </td>  <#t/>
           </tr>
           <tr>
             <td><@c.msg "portal.player_match_details.page.match_start_time"/></td>
             <td><b>${match.startTime?datetime}</b></td>
           </tr>
           <tr>
             <td><@c.msg "portal.player_match_details.page.match_end_time"/></td>
             <td><b>${match.endTime?datetime}</b></td>
           </tr>
           <tr>
             <td><@c.msg "portal.player_match_details.page.match_game_type"/></td>
             <td><b><@c.msg messageCode=("enum.game_type." + match.gameType)/></b></td>
           </tr>
          <#local mapHref = ""/>
          <#if (match.map)?has_content>
            <#local mapHref><@spring.url ("/portal/map_details?id=" + match.map.id?c) /></#local>
          </#if>
           <tr>
             <td><@c.msg "portal.player_match_details.page.match_map_name"/></td>
             <td><a href="${mapHref}">${((match.map.name)!"unknown")?html}</a></td>
           </tr>
           <tr>
             <td><@c.msg "portal.player_match_details.page.log_file_name"/></td>
             <td><b>${match.logFileName}</b></td>
           </tr>
         </table>
      </td>
      <#-- Second col -->
      <td style="width: 30%">
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
        <div class="table_title"><@c.msg "portal.player_match_details.page.players"/></div>  <#t/>
        <table class="attr_table">
          <tr>
            <th class="borders"><@c.msg "portal.player_match_details.page.player.name"/></th>
            <#if match.isTeamBasedMatch()>
              <th class="borders"><@c.msg "portal.player_match_details.page.player.team"/></th>
            </#if>
            <th class="borders" style="text-align: right"><@c.msg "portal.player_match_details.page.player.ratio"/></th>
            <th class="borders" style="text-align: right"><@c.msg "portal.player_match_details.page.player.kills"/></th>
            <th class="borders" style="text-align: right"><@c.msg "portal.player_match_details.page.player.deaths"/></th>
          </tr>
          <tr>
            <td class="borders">  <#t/>
              <a href="<@spring.url "/portal/player_details?id=" + playerMatchStats.playerId?c/>">${playerMatchStats.playerName?html}</a>  <#t/>
            </td>
            <#if match.isTeamBasedMatch()>
              <td class="borders team_${(playerMatchStats.playerTeam)!""}"><#if (playerMatchStats.playerTeam)?has_content><@c.msg ("enum.team." + playerMatchStats.playerTeam)/></#if></td>
            </#if>
            <td class="borders" style="text-align: right">${playerMatchStats.getKDRatio()?string}</td>
            <td class="borders" style="text-align: right">${playerMatchStats.kills?c}</td>
            <td class="borders" style="text-align: right">${playerMatchStats.deaths?c}</td>
          </tr>
        </table>
      </td>
      <#-- SECOND col -->
      <td>
        <div class="table_title">  <#t/>
          <@c.msg "portal.player_match_details.page.player_hits"/>  <#t/>
        </div>  <#t/>
        <#if ((bestHitsError!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=bestHitsError id="hit_list_error_"/>  <#t/>
        </#if>  <#t/>
      <#if hitStatList?has_content>
        <table class="attr_table">
          <tr>
            <#list (hitStatList![]) as hitStat>
              <th class="borders"  style="text-align: right"><@c.msg ("enum.hit_target." + hitStat.target)/></th>
            </#list>
          </tr>
          <tr>
            <#list (hitStatList![]) as hitStat>
              <td class="borders" style="text-align: right">${hitStat.hitCount?c}</td>
            </#list>
          </tr>
        </table>
      </#if>
      </td>
      <#-- THIRD col -->
      <td>
        <div class="table_title">  <#t/>
          <@c.msg "portal.player_match_details.page.player_achvs"/>  <#t/>
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
    <tr>
      <#-- First col -->
      <td colspan="3">
          <#if ((wpnsError!{})?has_content)>  <#t/>
            <@c.handleError errorDetails=wpnsError id="wpns_error_"/>  <#t/>
          </#if>  <#t/>
          <#if weapons?has_content>
            <div class="table_title">
              <@c.msg "portal.player_match_details.page.wpns.table.title" />  <#t/>
            </div>  <#t/>
            <#local totalKills = playerMatchStats.kills />
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
                      <@c.msg "portal.player_match_details.page.wpns.table.header.kills" />  <#t/>
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
  </table>
</#if>
</#macro>

<#assign pageTitleSuffix = ": " + ((match.startTime?datetime)!"") + " " + ((playerMatchStats.playerName)!"")?html/>
<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.player_match_details.page.title" activeMenuItem="menu_matches" pageTitleSuffix=pageTitleSuffix/>
