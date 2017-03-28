<#import "../../common.ftl" as c />
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
  </#if>  <#t/>
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
