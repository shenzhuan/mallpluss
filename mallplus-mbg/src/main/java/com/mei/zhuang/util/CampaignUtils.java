package com.mei.zhuang.util;//package com.arvato.common.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mei.zhuang.entity.sys.CrmNodeSchedule;
//import com.arvato.utils.constant.CommonConstant;
//import org.apache.commons.lang3.StringUtils;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import java.util.*;
//
//
//public class CampaignUtils {
//
//    private static final String TARGETVALUETYPE1001 = "1001";
//    private static final String TARGETVALUETYPE1002 = "1002";
//
//
//    /**
//     * 根据schedule检测活动是否结束
//     * @param schedule
//     * @return
//     */
//    public static boolean isCampaignOver(CrmNodeSchedule schedule){
//        Date endDate = schedule.getEndDate();
//        Calendar endCal=Calendar.getInstance();
//        endCal.setTime(endDate);
//        endCal.add(Calendar.HOUR, 23);
//        endCal.add(Calendar.MINUTE, 59);
//        endCal.add(Calendar.SECOND, 59);
//
//        Date nextExecDay = null;
//        Calendar nextExecCal = null;
//        String frequency = Objects.toString(schedule.getFrequency(),"");
//        String frecontent = Objects.toString(schedule.getFrecontent(),"");
//        if(frequency.equals(CommonConstant.MONTHLY)){
//        	nextExecDay = nextFrequenvyExecDate(Calendar.DAY_OF_MONTH,Calendar.MONTH,Integer.valueOf(frecontent));
//        } else if (frequency.equals(CommonConstant.WEEKLY)){
//        	nextExecDay = nextFrequenvyExecDate(Calendar.DAY_OF_WEEK,Calendar.WEEK_OF_MONTH,Integer.valueOf(frecontent));
//        } else if(frequency.equals(CommonConstant.DAILY)){
//        	nextExecDay = nextDaliyExecDate(Integer.valueOf(frecontent));
//        }
//        if(nextExecDay!=null){
//        	nextExecCal = Calendar.getInstance();
//        	nextExecCal.setTime(nextExecDay);
//        }
//        int e_ = endCal.compareTo(nextExecCal);
//        if ( e_ < 0){
//        	return true;
//        }
//        return false;
//    }
//
//    public static Date nextFrequenvyExecDate(int fre,int freType,int freContent) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(fre, freContent);
//        calendar.add(freType, 1);
//        return calendar.getTime();
//    }
//
//    public static Date nextDaliyExecDate(int freContent) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+freContent);
//        return calendar.getTime();
//    }
//
//    public static int getControlGroupCount(String targetValueType, Integer targetValue,Integer total) {
//        int count = 0;
//        if(StringUtils.equals(TARGETVALUETYPE1001, targetValueType)){
//            count  = (targetValue > 100)? total : (int)(total * (targetValue/100.0));
//        } else if(StringUtils.equals(TARGETVALUETYPE1002, targetValueType)){
//            count  = (total > targetValue) ? targetValue : total;
//        }
//        return count;
//    }
//
//    private static String[] columnArr = {"id","member_id","member_code","\"loop\"","node_id","data_type"};
//    private static String[] order= {" desc"," asc"};
//
//    /**
//     * 通过人群数据不同字段排序来实现控制不同的人
//     * @Title: randomControlGroup
//     * @Description: TODO(这里用一句话描述这个方法的作用)
//     * @returnString
//     * @date 2018-01-04 15:33
//     * @author QIPE001
//     * @updateDate 2018-01-04 15:33
//     * @updateAuthor QIPE001
//     * @version V1.0
//     * @throws
//     */
//    public static String randomControlGroup() {
//    	StringBuffer sb = new StringBuffer();
//    	Random random = new Random();
//		int loop=random.nextInt(columnArr.length);
//		for(int i=0;i<loop;i++) {
//			sb.append(columnArr[random.nextInt(columnArr.length)]);
//			sb.append(order[random.nextInt(order.length)]);
//			if(i!=(loop-1)) {
//				sb.append(",");
//			}
//		}
//    	return sb.toString();
//    }
//
//    public static JSONObject getMaxheightAndwidthFromXml(String xml){
//    	JSONObject returnformat = new JSONObject();
//    	try {
//			Document doc = DocumentHelper.parseText(xml);
//			Element rootElement = doc.getRootElement();
//			Element elementBPMNDiagram = rootElement.element("BPMNDiagram");
//			Element elementBPMNPlane = elementBPMNDiagram.element("BPMNPlane");
//			List<Element> bPMNShapeElements =  elementBPMNPlane.elements("BPMNShape");
//			List<Element> bPMNEdgeElements =  elementBPMNPlane.elements("BPMNEdge");
//			List<Integer> widthList = new ArrayList<Integer>();
//			List<Integer> heightList = new ArrayList<Integer>();
//			for(Element bPMNShapeElement : bPMNShapeElements){
//				Element boundElement = bPMNShapeElement.element("Bounds");
//				widthList.add(Math.round(Float.valueOf(boundElement.attributeValue("x"))));
//				heightList.add(Math.round(Float.valueOf(boundElement.attributeValue("y"))));
//			}
//			for(Element bPMNEdgeElement : bPMNEdgeElements){
//				List<Element> waypointElements = bPMNEdgeElement.elements("waypoint");
//				for(Element waypointElement : waypointElements){
//					widthList.add(Math.round(Float.valueOf(waypointElement.attributeValue("x"))));
//					heightList.add(Math.round(Float.valueOf(waypointElement.attributeValue("y"))));
//				}
//			}
//			Collections.sort(widthList);
//			Collections.sort(heightList);
//			returnformat.put("width", widthList.get(widthList.size()-1));
//			returnformat.put("height", heightList.get(widthList.size()-1));
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//    	return returnformat;
//    }
//}
