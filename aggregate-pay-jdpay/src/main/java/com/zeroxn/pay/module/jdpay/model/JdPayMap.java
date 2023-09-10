package com.zeroxn.pay.module.jdpay.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-10 17:29:36
 * @Description:
 */
@JacksonXmlRootElement(localName = "jdpay")
public class JdPayMap<K, V> extends HashMap<K, V> {

}