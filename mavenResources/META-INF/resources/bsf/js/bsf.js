/*
 
 Copyright 2014 Riccardo Massera (TheCoder4.Eu)
 BootsFaces JS
 author: TheCoder4.eu
*/
BsF={ajax:{},callback:{},isFunction:function(a){var c={};return a&&"[object Function]"===c.toString.call(a)}};BsF.ajax.onevent=function(a){"success"==a.status&&(a=a.source.id.replace(/[^a-zA-Z0-9]+/g,"_"),a=BsF.callback[a],"undefined"!=typeof a&&a())};BsF.ajax.cb=function(a,c,b,d){BsF.ajax.callAjax(a,c,b,"@all",d,null)};
BsF.ajax.callAjax=function(a,c,b,d,f,h){var k=arguments.length,g=a.id,l=g.replace(/[^a-zA-Z0-9]+/g,"_"),e={};h&&(e.params="BsFEvent="+h);b=BsF.ajax.resolveJQuery(b);(d=BsF.ajax.resolveJQuery(d))&&null!=d&&(e.execute=d);e[g]=g;5==k&&null!=f&&(BsF.callback[l]=f,e.render=b,e.onevent=BsF.ajax.onevent);3<=k&&(BsF.isFunction(b)?(BsF.callback[l]=b,e.onevent=BsF.ajax.onevent):e.render=b);jsf.ajax.request(a,c,e);return!1};
BsF.ajax.resolveJQuery=function(a){if("undefined"==typeof a||null==a)return"";var c="";a=a.split(" ");for(i=0;i<a.length;i++)if(part=a[i],0==part.indexOf("@(")&&part.lastIndexOf(")")==part.length-1){var b=part.substring(2,part.length-1);(b=$(b))&&b.each(function(a,b){c+=" "+b.id})}else c+=part+" ";return c.trim()};BsF.ajax.paginate=function(a,c,b,d,f){a={execute:"@this"};a.render=f;a[d]=b;jsf.ajax.request(d,c,a);return!1};
if($.datepicker){var generateHTML_orig=$.datepicker._generateHTML;$.datepicker._generateHTML=function(){var a=generateHTML_orig.apply(this,arguments),a=a.replace(/<span\s+class='ui-icon\s+ui-icon-circle-triangle-w'\s*>[^<]+<\/span>/,'<span class="glyphicon glyphicon-chevron-left"></span>');return a=a.replace(/<span\s+class='ui-icon\s+ui-icon-circle-triangle-e'\s*>[^<]+<\/span>/,'<span class="glyphicon glyphicon-chevron-right"></span>')}};