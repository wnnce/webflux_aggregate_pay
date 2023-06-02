package com.zeroxn.pay.module.union.business;

import com.zeroxn.pay.module.union.config.UnionPayProperties;
import com.zeroxn.pay.module.union.constant.UnionConstant;
import com.zeroxn.pay.module.union.utils.UnionUtil;

import java.util.Map;
import java.util.Set;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午7:59
 * @Description:
 */
public class UnionPayBusiness {
    private final UnionPayProperties properties;
    public UnionPayBusiness(UnionPayProperties properties){
        this.properties = properties;
    }

    public String wapConfirmOrder(Map<String, String> requestData){
        Map<String, String> filterMap = UnionUtil.filterEmpty(requestData);
        Map<String, String> formData = UnionUtil.sign(filterMap, properties.getPrivateKey(), properties.getCharset());
        return createFormHtml(formData, properties.getCharset());
    }

    private String createFormHtml(Map<String, String> data, String charset){
        StringBuilder sf = new StringBuilder();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(charset)
                .append("\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"").append(UnionConstant.TESTREQUESTURL).append("\" method=\"post\">");
        if (null != data && 0 != data.size()) {
            Set<Map.Entry<String, String>> set = data.entrySet();
            for (Map.Entry<String, String> ey : set) {
                String key = ey.getKey();
                String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"").append(key).append("\" id=\"").append(key)
                        .append("\" value=\"").append(value).append("\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }
}
