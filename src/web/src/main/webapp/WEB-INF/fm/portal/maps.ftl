<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
<#local mapCount=(mapList?size)!0/>
<table class="col_layout">
    <#-- ROW 1 -->
    <tr>
      <#-- First col -->
      <td style="width: 40%">
        <div class="table_title"><@c.msg "portal.map_list.page.map.table.title" />&nbsp;(${mapCount?c})</div>  <#t/>
        <#if ((error!{})?has_content)>  <#t/>
          <@c.handleError errorDetails=error id="map_list_error_"/>  <#t/>
        </#if>  <#t/>
      <#if mapList?has_content>  <#t/>
        <#local sortUrl><@spring.url "/portal/maps?"/></#local>
        <table class="attr_table">  <#t/>
          <tr>  <#t/>
            <#local colNum = 0/>  <#t/>
            <th class="borders<#if colNum=sort.column> sort</#if>" style="">  <#t/>
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_id" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <#local colNum = 1/>  <#t/>
            <th class="borders<#if colNum=sort.column> sort</#if>" style="">  <#t/>
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_map_name" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <#local colNum = 2/>  <#t/>
            <th class="borders<#if colNum=sort.column> sort</#if>" style="">  <#t/>
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_map_file_name" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <#local colNum = 3/>  <#t/>
            <th class="borders<#if colNum=sort.column> sort</#if>" style="">  <#t/>
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_map_type" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <#local colNum = 4/>  <#t/>
            <th class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right;" title="<@c.msg "portal.map_list.page.map.table.header_match_count_hint" />">  <#t/>
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_match_count" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <#local colNum = 5/>  <#t/>
            <th class="borders" style="text-align: center; " title="<@c.msg "portal.map_list.page.map.table.header_map_has_bots_hint"/> ">
              <@c.sortColumn colNum=colNum sortNum=sort.column isAsc=sort.asc colMsgCode="portal.map_list.page.map.table.header_map_has_bots" url=sortUrl/>  <#t/>
            </th>  <#t/>
            <th class="borders" style="">&nbsp;</th>  <#t/>
          </tr>  <#t/>
          <#list mapList as map>  <#t/>
            <#local onClick><@spring.url ("/portal/map_details?id=" + map.id?c) /></#local>
            <tr>  <#t/>
              <#local colNum = 0/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>">${map.id?c}</td>  <#t/>
              <#local colNum = 1/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>"><a href="${onClick}">${map.name?html}</a></td>  <#t/>
              <#local colNum = 2/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>">${map.fileName?html}</td>  <#t/>
              <#local colNum = 3/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>">  <#t/>
                <#if (map.mapType)?has_content>  <#t/>
                  <@c.msg messageCode=("enum.map_type." + map.mapType)/>  <#t/>
                </#if>  <#t/>
              </td>  <#t/>
              <#local colNum = 4/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: right;">  <#t/>
                ${map.matchCount?c}  <#t/>
              </td>  <#t/>
              <#local colNum = 5/>  <#t/>
              <td class="borders<#if colNum=sort.column> sort</#if>" style="text-align: center;">
                <#if (map.hasBots())>V<#else>&nbsp;</#if>  <#t/>
              </td>  <#t/>
              <td class="borders">  <#t/>
                <#if (map.images)?has_content>
                  <#list map.images as img>
                    <img src="<@spring.url "/img/screenshot.png"/>" onMouseOver="onImgMouseOver(${img.id?c}, this);" onMouseOut="onImgMouseOut(this);" style="cursor: pointer; "/>
                    <#if (img_index < map.images?size-1)>|</#if>
                  </#list>
                <#else>
                  &nbsp;
                </#if><#t/>
              </td>  <#t/>
            </tr>  <#t/>
          </#list>  <#t/>
        </table>  <#t/>
      <#else>  <#t/>
        <@c.msg "portal.map_list.page.map.table.no_maps_found"/>  <#t/>
      </#if>  <#t/>
    </td>
    <#-- Second col -->
    <td style="width: 60%">
      <div id="image_preview_container" class="map_img_preview_container_hidden" style="position: relative;"/>
        <img id="img_preview" src="<@spring.url "/img/wait.gif"/>" style="display: none;"/>
      </div>
    </td>
  </tr>
</table>
<script type="text/javascript">
  function onImgMouseOver(imgId, callerElt){
    var imgContainer = document.getElementById("image_preview_container");
    var imgElt = document.getElementById("img_preview");
    var topPos = window.pageYOffset || document.documentElement.scrollTop;
    imgContainer.className = "map_img_preview_container";
    imgContainer.style.top = topPos + "px";
    imgElt.style.display = "block";
    imgElt.src = "<@spring.url "/img/wait.gif"/>";
    var imgUrl = "<@spring.url "/map_img?id="/>" + imgId;
    var img = new Image();
    img.src = imgUrl;
    imgElt.src=imgUrl;
  }
  
  function onImgMouseOut(callerElt){
    var imgElt = document.getElementById("img_preview");
    imgElt.src="<@spring.url "/img/wait.gif"/>";
    imgElt.style.display = "none";
    var imgContainer = document.getElementById("image_preview_container");
    imgContainer.className = "map_img_preview_container_hidden";

  }
  
  
</script>
</#macro>

<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.maps.page.title" activeMenuItem="menu_maps"/>
