<#import "/spring.ftl" as spring />

<#setting url_escaping_charset="UTF-8" />
<#setting number_format="0.00"/>
<#setting date_format="dd.MM.yyyy"/>
<#setting time_format="hh:mm:ss"/>
<#setting datetime_format="dd.MM.yyyy HH:mm"/>


<#macro msg messageCode="", defaultMessage="" suffix="" prefix="">
    <#assign defaultMessageCode = defaultMessage><#t/>
    <#if defaultMessageCode == ""><#t/>
        <#assign defaultMessageCode = messageCode><#t/>
    </#if><#t/>
    ${prefix}<@spring.messageText messageCode, defaultMessageCode />${suffix}<#t/>
</#macro>

<#macro drawMatchTable matchList=[] pageUrl="" onClickUrl="" pager={} containerId="" async=false sort={}>
  <#if matchList?has_content>  <#t/>
  <div id="${containerId}">
    <table class="match_paged_list">  <#t/>
      <tr>  <#t/>
        <#local sortUrl><@spring.url pageUrl + (((pager.pageNum)!0)?c) + "&"/></#local>  <#t/>
        <#local colNum = 0/>  <#t/>
        <th class="<#if colNum=sort.column> sort</#if>">  <#t/>
          <@sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="match.table.header_id" url=sortUrl containerId=containerId async=async/>  <#t/>
        </th>  <#t/>
        <#local colNum = 1/>  <#t/>
        <th class="th_center<#if colNum=sort.column> sort</#if>" style="">  <#t/>
          <@sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="match.table.header_start_date" url=sortUrl containerId=containerId async=async/>  <#t/>
        </th>  <#t/>
        <#local colNum = 2/>  <#t/>
        <th class="<#if colNum=sort.column> sort</#if>" style="width: 100px;">  <#t/>
          <@sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="match.table.header_game_type" url=sortUrl containerId=containerId async=async/>  <#t/>
        </th>  <#t/>
        <#local colNum = 3/>  <#t/>
        <th class="<#if colNum=sort.column> sort</#if>" style="width: 120px;">  <#t/>
          <@sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="match.table.header_map_name" url=sortUrl containerId=containerId async=async/>  <#t/>
        </th>  <#t/>
      </tr>  <#t/>
      <#list matchList as match>  <#t/>
        <#local onClick><@spring.url (onClickUrl + match.id?c) /></#local>
        <tr class="data_row">  <#t/>
          <#local colNum = 0/>  <#t/>
          <td class="<#if colNum=sort.column> sort</#if>"><a href="${onClick}">${match.id?c}</a></td>  <#t/>
          <#local colNum = 1/>  <#t/>
          <td class="td_center<#if colNum=sort.column> sort</#if>"><nobr><a href="${onClick}"><#if match.startTime?has_content>${(match.startTime)?datetime}<#else>&nbsp;</#if></a></nobr></td>  <#t/>
          <#local colNum = 2/>  <#t/>
          <td class="<#if colNum=sort.column> sort</#if>"><a href="${onClick}"><nobr><@c.msg messageCode=("enum.game_type." + match.gameType)/></nobr></a></td>  <#t/>
          <#local mapHref = ""/>
          <#if (match.map)?has_content>
            <#local mapHref><@spring.url ("/portal/map_details?id=" + match.map.id?c) /></#local>
          </#if>
          <#local colNum = 3/>  <#t/>
          <td class="<#if colNum=sort.column> sort</#if>"><a href="${mapHref}"><nobr>${((match.map.name)!"unknown map")?html}</nobr></a></td>  <#t/>
        </tr>  <#t/>
      </#list>  <#t/>
    </table>  <#t/>
    <#if pager?has_content>
        <div class="pager_container"><#t/>
          <nobr>  <#t/>
            <#list 1..pager.pageCount as p><#t/>
                <#if p=pager.pageNum>  <#t/>
                  <span>${p?c}</span>  <#t/>
                <#else>  <#t/>
                  <#local href><@spring.url (pageUrl + p?c) + "&sort_by=" + ((sort.column)!0)?c + "&sort_asc=" + (((sort.asc)!true)?string("true", "false")) /></#local>  <#t/>
                  <#if async>
                    <a href="#" onClick="onMatchTableReload('${href}', '${containerId}');return false;">${p?c}</a>  <#t/>
                  <#else>
                    <a href="${href}">${p?c}</a>  <#t/>
                  </#if>
                </#if>  <#t/>
            </#list>  <#t/>
          </nobr>  <#t/>
        </div><#t/>
    </#if>
  </div>
  <#else>  <#t/>
    <@c.msg "match.table.no_matches_found"/>  <#t/>
  </#if>  <#t/>
</#macro>

<#macro handleError errorDetails={} id="error_">
  <#if errorDetails?has_content>  <#t/>
    <div id="${id}_container" class="error_container">  <#t/>
      <div class="error_header_container">  <#t/>
        <div class="error_header">  <#t/>
          <@c.msg "error.error_occured"/>  <#t/>
        </div>  <#t/>
        <div class="error_message">  <#t/>
          ${(errorDetails.descr)!""}  <#t/>
        </div>  <#t/>
      </div>  <#t/>
      <#if errorDetails.stackTraceAsString?has_content>  <#t/>
        <a onclick="showErrorStack(this, '${id}_details', '<@c.msg "error.show_details"/>', '<@c.msg "error.hide_details"/>');return false;" href="#"><@c.msg "error.show_details"/></a>  <#t/>
        <div id="${id}_details" class="error_details" style="display:none;">  <#t/>
          <b>${(errorDetails.getMessage())!errorDetails.cause.getClass().getName()}</b><br/>
          ${errorDetails.stackTraceAsString}  <#t/>
        </div>  <#t/>
      </#if>  <#t/>
    </div>   <#t/>
  </#if>  <#t/>
</#macro>

<#macro sortColumn colNum=-1 sortNum=-1 isAsc=true colMsgCode="" url="" containerId="" async=false>
  <#local order = 0/>  <#t/>
  <#if colNum = sortNum>  <#t/>
      <#if isAsc>  <#t/>
        <#local order = 1/>  <#t/>
      <#else>  <#t/>
        <#local order = 2/>  <#t/>
      </#if>  <#t/>
  </#if>  <#t/>
  <#local onClick>${url}sort_by=${colNum?c}&sort_asc=<#if (order=2 || order=0)>true<#else>false</#if></#local>  <#t/>
  <#if async>
    <a href="#" onClick="onMatchTableReload('${onClick}', '${containerId}');return false;" ><@msg colMsgCode/><#if (order=1)>&nbsp;&darr;<#elseif order=2>&nbsp;&uarr;</#if></a>  <#t/>
  <#else>
    <a href="${onClick}"><@msg colMsgCode/><#if (order=1)>&nbsp;&darr;<#elseif order=2>&nbsp;&uarr;</#if></a>  <#t/>
  </#if>
</#macro>