<#import "/spring.ftl" as spring />
<#import "../../common.ftl" as c />
  <#if ((error!{})?has_content)>  <#t/>
    <@c.handleError errorDetails=error id="match_list_error_"/>  <#t/>
  </#if>  <#t/>
  <table class="attr_table">
      <#list (achvList![]) as achv>
        <tr>
          <td style="padding: 0 0 0 10px;">  <#t/>
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
