<#import "../common.ftl" as c />
<#include "page.ftl"/>

<#-- PAGE BODY NESTED CONTENT  -->  <#t/>
<#macro page_body>  <#t/>
<#local map=map!{}/>
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="map_error_"/>
  </#if>  <#t/>
<#if map?has_content>
  <#-- Map attributes -->
    <div class="table_title"><@c.msg "portal.map_details.page.map_details"/></div>  <#t/>
    <table class="attr_table">
      <tr>
        <td><@c.msg "portal.map_details.page.map_name"/></td>
        <td><b>${map.name?html}</b></td>
      </tr>
      <tr>
        <td><@c.msg "portal.map_details.page.map_file_name"/></td>
        <td><b>${map.fileName?html}</b></td>
      </tr>
      <tr>
        <td><@c.msg "portal.map_details.page.map_type"/></td>
        <td>  <#t/>
          <#if (map.mapType)?has_content>  <#t/>
            <b><@c.msg messageCode=("enum.map_type." + map.mapType)/></b>  <#t/>
          </#if>  <#t/>
        </td>  <#t/>
      </tr>
      <tr>
        <td><@c.msg "portal.map_details.page.match_count"/></td>
        <td><b>${map.matchCount?c}</b></td>
      </tr>
      <tr>
        <td><@c.msg "portal.map_details.page.has_bots"/></td>
        <td>  <#t/>
          <b>  <#t/>
            <#if (map.hasBots())>  <#t/>
              <@c.msg "boolean.yes"/>  <#t/>
            <#else>  <#t/>
              <@c.msg "boolean.no"/>  <#t/>
            </#if>  <#t/>
          </b>  <#t/>
        </td>
      </tr>
    </table>
    <br/><br/>
    <#if map.images?has_content>
      <#list map.images as img>
        <img src="<@spring.url "/map_img?id=" + img.id?c/>" />
        <br/>
      </#list>
    <#else>
      NO IMAGES
    </#if>
</#if>

</#macro>

<#-- Drawing page layout -->
<@pageLayout pageTitle="portal.map_details.page.title" activeMenuItem="menu_maps"/>
