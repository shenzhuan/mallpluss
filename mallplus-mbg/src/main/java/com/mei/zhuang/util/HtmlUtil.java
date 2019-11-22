package com.mei.zhuang.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martin on 2017/11/23.
 */
public class HtmlUtil {

    /**
     * 简单html解析成文本，仅支持一下标签及解析方式 <code>
     * <p><f>[Smile]</f>&nbsp;&nbsp;test</p>
     * <p><br></p>
     * <p>test</p>
     * <p>
     * 解析成  [Smile]  test\r\n\r\n\r\ntest
     * </code>
     *
     * @param html
     * @return
     */
    public static String simpleParseToText(String html) {
        if (StringUtils.isEmpty(html)) {
            return null;
        }
        Document doc = Jsoup.parse(html);

        Elements eles = doc.select("f");
        if (eles != null && eles.size() > 0) {
            for (Element e : eles) {
                e.after(e.html());
            }
            eles.remove();
        }

        String text = doc.body().html().replaceAll("\\r\\n", "").replaceAll("\\n", "").replaceAll("<p>", "")
                .replaceAll("</p>", "<br>").replaceAll("\\&nbsp;", " ");

        if (text.endsWith("<br>")) {
            text = text.substring(0, text.lastIndexOf("<br>"));
        }

        text = text.replaceAll("<br>", "\r\n");

        return text;
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容
     *
     * @param soap
     * @return
     */
    public static String getSubUtil(String soap) {
        String rgex = "\\[(.*?)\\]";
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            list.add(m.group(1));
        }
        for (String str : list) {
            //<img style="" src="../../../../static/libs/wechat-emoji/image/Grimace.png" data-codename="[Grimace]">
            soap = soap.replace("[" + str + "]", "<img style=\"\" src=\"../../../../static/libs/wechat-emoji/image/" + str + ".png\" data-codename=\"[" + str + "]\">");
        }
        return soap;
    }

    public static void main(String[] args) {
        String html = "<p>aa<f>[Smile]</f>bb<f>[Scream]</f>dd<f>[Joyful]</f></p>";
        System.out.println(simpleParseToText(html));
    }
}
