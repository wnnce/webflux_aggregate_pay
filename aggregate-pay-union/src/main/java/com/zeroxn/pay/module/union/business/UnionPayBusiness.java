package com.zeroxn.pay.module.union.business;

import com.zeroxn.pay.module.union.config.UnionPayProperties;
import com.zeroxn.pay.module.union.constant.UnionConstant;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午7:59
 * @Description: 云闪付业务类
 */
public class UnionPayBusiness {
    private final UnionPayProperties properties;
    private final RestTemplate restTemplate;
    public UnionPayBusiness(UnionPayProperties properties, RestTemplate restTemplate){
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public String wapConfirmOrder(Map<String, String> requestData){
        Map<String, String> formData = UnionUtils.sign(requestData);
        return createFormHtml(formData, properties.getCharset());
    }
    public String queryOrder(Map<String, String> requestData){
        ResponseEntity<String> responseEntity = sendPost(requestData, UnionConstant.TEST_QUERY_URL);
        return responseEntity.getBody();
    }
    public String wapRevokeOrder(Map<String, String> requestData){
        ResponseEntity<String> responseEntity = sendPost(requestData, UnionConstant.TEST_REFUND_URL);
        return responseEntity.getBody();
    }
    public String refundOrder(Map<String, String> requestData){
        ResponseEntity<String> responseEntity = sendPost(requestData, UnionConstant.TEST_REFUND_URL);
        return responseEntity.getBody();
    }
    private String createFormHtml(Map<String, String> data, String charset){
        StringBuilder sf = new StringBuilder();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(charset)
                .append("\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"").append(UnionConstant.TEST_FRONT_URL).append("\" method=\"post\">");
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
    private ResponseEntity<String> sendPost(Map<String, String> requestData, String requestUrl){
        Map<String, String> signRequestData = UnionUtils.sign(requestData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> multiValueMap = UnionUtils.mapToMultiValueMap(signRequestData);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, headers);
        return restTemplate.postForEntity(requestUrl, entity, String.class);
    }
}
