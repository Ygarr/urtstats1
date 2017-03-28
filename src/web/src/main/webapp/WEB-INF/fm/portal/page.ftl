<#import "/spring.ftl" as spring />
<#import "../common.ftl" as c />
<#-- 
###############################
# Portal page layout macro
################################
-->
<#macro pageLayout pageTitle="" activeMenuItem="" pageTitleSuffix="">
<html>
<#-- PAGE HEADER  -->
  <head>
    <title><@c.msg "portal.page.title"/>&nbsp;${((urtStatsConfig.version)!"")?html}:&nbsp;<@c.msg pageTitle />${pageTitleSuffix}</title>
        <#local resourceSuffix>?v=${(urtStatsConfig.version)!"1"}</#local>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <#--meta http-equiv="Pragma" content="no-cache" /-->
        <link rel="icon" href="<@spring.url "/img/favicon.ico"/>" />
        <#--link rel="shortcut icon" href="favicon.ico" type="image/x-icon" /-->
        <link rel="stylesheet" type="text/css" href='<@spring.url "/css/common.css" + resourceSuffix/>' />
        <link rel="stylesheet" type="text/css" href='<@spring.url "/css/portal.css" + resourceSuffix/>' />
        <script type="text/javascript" src='<@spring.url "/script/jquery-1.8.0.min.js"/>'></script>
        <script type="text/javascript" src='<@spring.url "/script/common.js" + resourceSuffix/>'></script>
        <script type="text/javascript" src='<@spring.url "/script/portal.js" + resourceSuffix/>'></script>
  </head>
  
  <body>
    <div class="page_container">
    <#-- HEADER  -->  <#t/>
      <div class="page_header">
          <table class="blank_table" width="100%">
            <tr>
              <td style="width: 50%">
                <img class="page_header_img" src="<@spring.url "/img/portal/title.png"/>"/>
              </td>
              <td style="width: 25%">
                &nbsp;
              </td>
              <td style="width: 25%;">
                <table class="blank_table" style="width: 100%; height: 100%">
                  <tr style="height: 33%">
                    <td>&nbsp;</td>
                  </tr>
                  <tr style="height: 33%">
                    <td>&nbsp;</td>
                  </tr>
                  <tr style="height: 33%">
                    <td style="text-align: right;">  <#t/>
                      <#local langURL = "" />
                       <#if ((reqParams!"")?has_content)>  <#t/>
                         <#local langURL = "?" + reqParams + "&lang=" /><#t/>
                       <#else>  <#t/>
                         <#local langURL = "?lang=" /><#t/>
                       </#if>  <#t/>
                        <a href="${langURL}en">  <#t/>
                          <img size="16" src="<@spring.url "/img/lang_en.png"/>" title="<@c.msg "lang.en" />"/>  <#t/>
                        </a>  <#t/>
                        <a href="${langURL}ru">  <#t/>
                          <img size="16" src="<@spring.url "/img/lang_ru.png"/>" title="<@c.msg "lang.ru" />"/>  <#t/>
                        </a>  <#t/>
                    </td>  <#t/>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
      </div>
    <#-- MENU  -->  <#t/>
      <div class="page_menu">
         <ul><#t/>
           <li id="menu_main" class="first <#if activeMenuItem="menu_main">active</#if>"><#t/>
             <a href="<@spring.url "/"/>"><@c.msg "portal.page.menu.main"/></a><#t/>
           </li><#t/>
           <li id="menu_matches" class="<#if activeMenuItem="menu_matches">active</#if>"><#t/>
             <a href="<@spring.url "/portal/matches"/>"><@c.msg "portal.page.menu.matches"/></a><#t/>
           </li><#t/>
           <li id="menu_players" class="<#if activeMenuItem="menu_players">active</#if>"><#t/>
             <a href="<@spring.url "/portal/players"/>"><@c.msg "portal.page.menu.players"/></a><#t/>
           </li><#t/>
           <li id="menu_maps" class="<#if activeMenuItem="menu_maps">active</#if>"><#t/>
             <a href="<@spring.url "/portal/maps"/>"><@c.msg "portal.page.menu.maps"/></a><#t/>
           </li><#t/>
         </ul><#t/>
      </div>
      <#t/>
    <#-- BODY  -->  <#t/>
      <div class="page_body">
          <@page_body/>  <#t/>
      </div>
      <#t/>
    <#-- FOOTER  -->  <#t/>
      <#t/>
      <div class="page_footer">
          <@c.msg "portal.page.footer"/>
      </div>
    </div>
    <#t/>
  </body>
</html>
</#macro>


