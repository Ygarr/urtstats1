<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
    <#local playerCount=(playerList?size)!0 />
    <div class="table_title"><@c.msg "portal.player_list.page.player.table.title" />&nbsp;(${playerCount?c})</div>  <#t/>
    <#if ((error!{})?has_content)>  <#t/>
      <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
    </#if>  <#t/>
  <#if playerList?has_content>  <#t/>
    <#local sortUrl><@spring.url "/portal/players?"/></#local>  <#t/>

    <table class="attr_table">  <#t/>
      <tr>  <#t/>
        <#local colNum = 0/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_id" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 1/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_player_name" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 2/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_avg_kd_ratio" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 3/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_kd_ratio" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 4/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_kills" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 5/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_deaths" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 6/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_games" url=sortUrl/>  <#t/>
        </th>  <#t/>
        <#local colNum = 7/>  <#t/>
        <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: center; ">
          <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.player_list.page.player.table.header_last_seen" url=sortUrl/>  <#t/>
        </th>  <#t/>
      </tr>  <#t/>
      <#list playerList as player>  <#t/>
        <#local onClick><@spring.url ("/portal/player_details?id=" + player.id?c) /></#local>
        <tr>  <#t/>
          <#local colNum = 0/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>">${player.id?c}</td>  <#t/>
          <#local colNum = 1/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>"><a href="${onClick}">${player.playerName?html}</a></td>  <#t/>
          <#local colNum = 2/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">  <#t/>
            <a href="<@spring.url ("/portal/chart/player_rate?id=" + player.id?c) />" target="_blank" title="<@c.msg "portal.player_list.page.player.table.header_avg_kd_ratio_hint"/>" >${player.avgKDRatio?string}</a>  <#t/>
          </td>  <#t/>
          <#local colNum = 3/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">${player.totalKDRatio?string}</td>  <#t/>
          <#local colNum = 4/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">${player.kills?c}</td>  <#t/>
          <#local colNum = 5/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">${player.deaths?c}</td>  <#t/>
          <#local colNum = 6/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right; ">${player.gamesPlayed?c}</td>  <#t/>
          <#local colNum = 7/>  <#t/>
          <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: center; "><#if (player.lastGameTime)?has_content>${player.lastGameTime?date}<#else>&nbsp;</#if></td>  <#t/>
        </tr>  <#t/>
      </#list>  <#t/>
    </table>  <#t/>
  <#else>  <#t/>
    <@c.msg "portal.player_list.page.player.table.no_players_found"/>  <#t/>
  </#if>  <#t/>
</#macro>

<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.players.page.title" activeMenuItem="menu_players"/>
