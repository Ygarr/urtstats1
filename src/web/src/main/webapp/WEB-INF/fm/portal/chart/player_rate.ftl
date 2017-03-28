<#import "../../common.ftl" as c />
<#include "../page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
 <#-- Player details -->
  <#local playerStats=playerStats!{}/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
  </#if>  <#t/>
  <#if playerStats?has_content>
      <div class="table_title"><@c.msg "portal.chart.player_rate.page.player.table.title" /></div>  <#t/>
      <table class="attr_table">  <#t/>
        <tr>  <#t/>
          <td><@c.msg "portal.chart.player_rate.page.player.table.header_player_name"/></th>  <#t/>
          <td style="text-align: right"><b>${playerStats.playerName?html}</b></td>  <#t/>
        </tr>
        <tr>  <#t/>
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_avg_kd_ratio"/></th>  <#t/>
          <td style="text-align: right">
            <b>${playerStats.avgKDRatio?string}</b>  <#t/>
          </td>  <#t/>
        </tr>
        <tr>  <#t/>
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_kd_ratio"/></th>  <#t/>
          <td style="text-align: right"><b>${playerStats.totalKDRatio?string}</b></td>  <#t/>
        </tr>
        <tr>  <#t/>
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_kills"/></th>  <#t/>
          <td style="text-align: right"><b>${playerStats.kills?c}</b></td>  <#t/>
        </tr>
        <tr>  <#t/>      
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_deaths"/></th>  <#t/>
          <td style="text-align: right"><b>${playerStats.deaths?c}</b></td>  <#t/>
        </tr>
        <tr>  <#t/>
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_games"/></th>  <#t/>
          <td style="text-align: right"><b>${playerStats.gamesPlayed?c}</b></td>  <#t/>
        </tr>  <#t/>
        <tr>  <#t/>
          <td style=""><@c.msg "portal.chart.player_rate.page.player.table.header_last_played"/></th>  <#t/>
          <td style="text-align: right"><#if playerStats.lastGameTime?has_content><b>${playerStats.lastGameTime?date}</b><#else>&nbsp;</#if></td>  <#t/>
        </tr>  <#t/>
      </table>  <#t/>
      <br/>
      <div class="table_title"><@c.msg "portal.chart.player_rate.title" /></div>  <#t/>
      <#if chartInfo??>
          <#local chartImgUrl><@spring.url "/portal/chart_img?chart_type=" + (chartType!"") + "&id=" + (playerStats.id?c) + "&t=" + (tstamp?c)/></#local>
          <#local fixedAreaWidth = chartInfo.fixedAreaWidth/>
          <#local areaHeight = chartInfo.fixedAreaHeight />
          <#local dataAreaWidth = chartInfo.dataAreaWidth />
          <#local dataAreaHeight = chartInfo.dataAreaHeight />
          
          <a href="${chartImgUrl}" target="_blank"><@c.msg "portal.chart.player_rate.get_image" /> </a>
          </br>

          <style>
            #fixedAreaImg {
               background-image:url('${chartImgUrl}');
               background-position:0px 0px;
               width: ${fixedAreaWidth?c}px; 
               height: ${areaHeight?c}px;
            }
            
            #dataAreaImg {
               background-image:url('${chartImgUrl}');
               background-position: -${fixedAreaWidth?c}px 0px;
               width: ${dataAreaWidth?c}px; 
               height: ${areaHeight?c}px;
            }
            #dataAreaContainer {
               overflow-x: auto; 
               overflow-y: visible;
               max-width: 85%;
               position: absolute;
            }
          </style>
          <table class="blank_table" style="width: 100%">
            <tr>
              <td style="border: solid black; border-width: 1px 0 1px 1px; vertical-align: top; width: ${fixedAreaWidth?c}px; height: ${(areaHeight+50)?c}px;">  <#t/>
                <div id="fixedAreaImg"></div>
              </td>  <#t/>
              <td style="border: solid black; border-width: 1px 1px 1px 0px; vertical-align: top; height: ${(areaHeight+50)?c}px;">  <#t/>
                <div id="dataAreaContainer">
                  <div id="dataAreaImg" >
                    <img src="<@spring.url "/img/blank.png"/>" usemap="#chartDataMap" style="width: ${dataAreaWidth?c}px; height: ${(areaHeight)?c}px;"/>
                  </div>
                </div>
              </td>
            </tr>
          </table>
<#--          <div class="chart_container">
            <img src="<@spring.url "/portal/chart_img?chart_type=" + (chartType!"") + "&id=" + (playerStats.id?c) + "&t=" + (tstamp?c)/>" />
          </div>
-->
        <#if chartInfo.chartData?has_content>
          <map id="chartDataMap" name="chartDataMap">
            <#local matchUrl><@spring.url "/portal/match_details?id="/></#local>
            <#local hintRate><@c.msg "portal.chart.player_rate.chart_item.rate" /></#local>
            <#local hintMap><@c.msg "portal.chart.player_rate.chart_item.map" /></#local>
            <#local hintDate><@c.msg "portal.chart.player_rate.chart_item.date" /></#local>
            <#list chartInfo.chartData as item>
                <#local hint>${hintRate}${item.playerMatchStats.getKDRatio()?string}&#10;${hintMap}${item.match.map.name?html}&#10;${hintDate}${item.match.startTime?string}</#local>
                <#local coords>${item.itemX?c}, ${item.itemY?c}, ${(item.itemX+item.itemWidth)?c}, ${(item.itemY+item.itemHeight)?c}</#local>
                <#local href>${matchUrl}${((item.match.id)!0)?c}</#local>
                <area href="${href}" shape="rect" coords="${coords}" alt="${hint}" title="${hint}" target="_blank"/>
            </#list> 
          </map>
        </#if>
      </#if>
  </#if>
</#macro>

<#assign pageTitleSuffix = ": " + ((playerStats.playerName)!"")?html/>
<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.chart.player_rate.page.title" activeMenuItem="menu_players" pageTitleSuffix=pageTitleSuffix/>