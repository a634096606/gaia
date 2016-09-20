package org.apel.gaia.js.lib.loader;

import javax.servlet.ServletContext;

import org.apel.gaia.js.lib.consist.UIConsist;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class UIWebResourceLoader implements ServletContextAware{
	
	
	//构造系统app.js资源
	private String buildAppjsResource(String contextPath){
		String jsTempalte = "<script type=\"text/javascript\" src=\"" + contextPath + "/js/platformUI.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + contextPath + "/js/date.js\"></script>";
		return jsTempalte;
	}
	
	//构造jquery的资源
	private String buildJqueryResource(String contextPath){
		return "<script type=\"text/javascript\" src=\"" + contextPath + "/jquery/jquery-1.11.3.min.js\"></script>";
	}
	
	//构造toast的资源
	private String buildToastrResource(String contextPath){
		String css ="<link href=\"" + contextPath + "/js-module/toast/toastr.css\" rel=\"stylesheet\"/>";
		String js = "<script src=\"" + contextPath + "/js-module/toast/toastr.js\" type=\"text/javascript\"></script>";
		String setting_js = "<script src=\"" + contextPath + "/js-module/toast/toastr.setting.js\" type=\"text/javascript\"></script>";
		return css + js + setting_js;
	}
	
	//构造zTree的资源
	private String buildZtreeResource(String contextPath){
		String css = "<link href=\"" + contextPath + "/js-module/zTree/css/zTreeStyle/zTreeStyle.css\" rel=\"stylesheet\"/>";
		String js = "<script src=\"" + contextPath + "/js-module/zTree/js/jquery.ztree.all-3.5.min.js\" type=\"text/javascript\"></script>";
		return css + js;
	}
	
	//构造jqGrid资源 
	private String buildJqGridResource(String contextPath){
		String multiSelectPluginCss = "<link href=\"" + contextPath + "/js-module/jqGrid/plugins/ui.multiselect.css\" rel=\"stylesheet\"/>";
		String multiSelectPluginJs = "<script src=\"" + contextPath + "/js-module/jqGrid/plugins/ui.multiselect.js\" type=\"text/javascript\"></script>";
		String theme_css = "<link href=\"" + contextPath + "/js-module/jquery-ui/jquery-ui.theme.min.css\" rel=\"stylesheet\"/>";
		String css = "<link href=\"" + contextPath + "/js-module/jqGrid/css/ui.jqgrid.css\" rel=\"stylesheet\"/>";
		String local_js = "<script src=\"" + contextPath + "/js-module/jqGrid/js/i18n/grid.locale-cn.js\" type=\"text/javascript\"></script>";
		String js = "<script src=\"" + contextPath + "/js-module/jqGrid/js/jquery.jqGrid.js\" type=\"text/javascript\"></script>";
		String setting_js = "<script src=\"" + contextPath + "/js-module/jqGrid/js/jqGrid.setting.js\" type=\"text/javascript\"></script>";
		String dateSearchabaleDeal_js = "<script src=\"" + contextPath + "/js-module/jqGrid/plugins/dateSearchabaleDeal.js\" type=\"text/javascript\"></script>";
		return multiSelectPluginCss + multiSelectPluginJs + theme_css + css + local_js + js + setting_js+dateSearchabaleDeal_js;
	}
	
	//构造easyUI资源
	private String buildEasyUIResource(String contextPath){
		String theme_css = "<link href=\"" + contextPath + "/js-module/easyUI/themes/default/easyui.css\" rel=\"stylesheet\"/>";
		String icon_css = "<link href=\"" + contextPath + "/js-module/easyUI/themes/icon.css\" rel=\"stylesheet\"/>";
		String local_js = "<script src=\"" + contextPath + "/js-module/easyUI/easyui-lang-zh_CN.js\" type=\"text/javascript\"></script>";
		String js = "<script src=\"" + contextPath + "/js-module/easyUI/jquery.easyui.min.js\" type=\"text/javascript\"></script>";
		String validate_js = "<script src=\"" + contextPath + "/js-module/easyUI/easyui.validate.js\" type=\"text/javascript\"></script>";
		return theme_css + icon_css + js + local_js + validate_js;
	}
	
	//构造97Date资源
	private String build97DateResource(String contextPath){
		return "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/My97DatePicker/WdatePicker.js\"></script>";
	}
	
	//构造bootstrap资源
	private String buildBootstrapResource(String contextPath){
		String css = "<link href=\"" + contextPath + "/js-module/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\"/>";
		String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/bootstrap/js/bootstrap.min.js\"></script>";
		return css + js;
	}
	
	//构造bootstrap validate资源
		private String buildBootstrapValidateResource(String contextPath){
			String css = "<link href=\"" + contextPath + "/js-module/bootstrap-validate/css/bootstrapValidator.min.css\" rel=\"stylesheet\"/>";
			String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/bootstrap-validate/js/bootstrapValidator.min.js\"></script>";
			String js_18n = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/bootstrap-validate/js/language/zh_CN.js\"></script>";
			return css + js + js_18n;
		}
	
	//构造jquery validate资源
	private String buildJqueryValidate(String contextPath){
		String local_js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/jquery-validate/localization/messages_zh.js\"></script>";
		String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/jquery-validate/jquery.validate.js\"></script>";
		return  js + local_js;
	}
	
	//构造validate-tooltip资源
	private String buildValidateTooltip(String contextPath){
		String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/validate-toolip/jquery-validate.bootstrap-tooltip.min.js\"></script>";
		return js;
	}
	
	//构造jqueryUI资源
	private String buildJqueryUI(String contextPath){
		String css = "<link href=\"" + contextPath + "/js-module/jquery-ui/jquery-ui.css\" rel=\"stylesheet\"/>";
		String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/jquery-ui/jquery-ui.js\"></script>";
		return css + js;
	} 
	
	//构造colorPicker资源
	private String buildColorPicker(String contextPath){
		String css = "<link href=\"" + contextPath + "/js-module/color-picker/css/colorpicker.css\" rel=\"stylesheet\"/>";
		String js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/color-picker/js/colorpicker.js\"></script>";
		return css + js;
	} 
	
	//构造系统ajaxUpload资源
	private String buildAjaxUpload(String contextPath){
		return "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/ajaxUpload/jquery.ajaxfileupload.js\"></script>";
	}
	
	//构造colorPicker资源
	private String buildPjax(String contextPath){
		String pjax_js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/noprogress/jquery.pjax.js\"></script>";
		String noprogress_css = "<link href=\"" + contextPath + "/js-module/noprogress/nprogress.css\" rel=\"stylesheet\"/>";
		String noprogress_js = "<script type=\"text/javascript\" src=\"" + contextPath + "/js-module/noprogress/nprogress.js\"></script>";
		return pjax_js + noprogress_css + noprogress_js;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		servletContext.setAttribute(UIConsist.JQUERY,buildJqueryResource(contextPath));
		servletContext.setAttribute(UIConsist.TOAST,buildToastrResource(contextPath));
		servletContext.setAttribute(UIConsist.ZTREE,buildZtreeResource(contextPath));
		servletContext.setAttribute(UIConsist.JQGRID,buildJqGridResource(contextPath));
		servletContext.setAttribute(UIConsist.EASYUI,buildEasyUIResource(contextPath));
		servletContext.setAttribute(UIConsist.DATE97,build97DateResource(contextPath));
		servletContext.setAttribute(UIConsist.BOOTSTRAP,buildBootstrapResource(contextPath));
		servletContext.setAttribute(UIConsist.JQUERY_VALIDATE,buildJqueryValidate(contextPath));
		servletContext.setAttribute(UIConsist.VALIDATE_TOOLTIP,buildValidateTooltip(contextPath));
		servletContext.setAttribute(UIConsist.JQUERY_UI,buildJqueryUI(contextPath));
		servletContext.setAttribute(UIConsist.APP_JS,buildAppjsResource(contextPath));
		servletContext.setAttribute(UIConsist.COLOR_PICKER,buildColorPicker(contextPath));
		servletContext.setAttribute(UIConsist.AJAX_UPLOAD,buildAjaxUpload(contextPath));
		servletContext.setAttribute(UIConsist.BOOTSTRAP_VALIDATE,buildBootstrapValidateResource(contextPath));
		servletContext.setAttribute(UIConsist.PJAX,buildPjax(contextPath));
	}
	
	

}
