package com.mei.zhuang.controller;


import com.google.common.base.Strings;
import lombok.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ExcelExportUtil {
    //表头
    private String title = "优惠券发放记录";
    //各个列的表头
    private String[] heardList;
    //各个列的元素key值
    private String[] heardKey;
    //需要填充的数据信息
    private List<Map<String, Object>> data;
    //字体大小
    private int fontSize = 14;
    //行高
    private int rowHeight = 30;
    //列宽
    private int columWidth = 200;
    //工作表
    private String sheetName = "发放记录";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeardList() {
        return heardList;
    }

    public void setHeardList(String[] heardList) {
        this.heardList = heardList;
    }

    public String[] getHeardKey() {
        return heardKey;
    }

    public void setHeardKey(String[] heardKey) {
        this.heardKey = heardKey;
    }


    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getColumWidth() {
        return columWidth;
    }

    public void setColumWidth(int columWidth) {
        this.columWidth = columWidth;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 检查数据配置问题
     *
     * @throws IOException 抛出数据异常类
     */
    protected void checkConfig() throws IOException {
        if (heardKey == null || heardList.length == 0) {
            throw new IOException("列名数组不能为空或者为NULL");
        }

        if (fontSize < 0 || rowHeight < 0 || columWidth < 0) {
            throw new IOException("字体、宽度或者高度不能为负值");
        }

        if (Strings.isNullOrEmpty(sheetName)) {
            throw new IOException("工作表表名不能为NULL");
        }
    }


    //生成excel表格
    public HSSFWorkbook getHSSFWorkbook(HttpServletRequest request, HttpServletResponse response) {
        //创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet("验证码");
        //创建HSSFRow对象
        HSSFRow row = sheet.createRow(0);
        //创建HSSFCell对象
        HSSFCell cell = row.createCell(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 创建一个居中格式
        //设置单元格的值
        cell.setCellValue("验证码上传");

        //导出数据
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "验证码" + sf.format(date) + ".csv";
            response.setHeader("Content-disposition", "attachment;Filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {

            } finally {
                try {
                    wb.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return wb;
    }

    /**
     * 开始导出数据信息
     * <p>
     * List<Map>集合处理
     */
    public byte[] exportExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);

        // 标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontStyle.setBold(true);   //加粗
        fontStyle.setFontHeightInPoints((short) 16);  //设置标题字体大小
        cellStyle.setFont(fontStyle);

        //在第0行创建rows  (表标题)
        HSSFRow title = wbSheet.createRow((int) 0);
        title.setHeightInPoints(30);//行高
        HSSFCell cellValue = title.createCell(0);
        cellValue.setCellValue(this.title);
        cellValue.setCellStyle(cellStyle);
        wbSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (this.heardList.length - 1)));

        //设置表头样式，表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //设置单元格样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        //在第1行创建rows
        HSSFRow row = wbSheet.createRow((int) 1);
        //设置列头元素
        HSSFCell cellHead = null;
        for (int i = 0; i < heardList.length; i++) {
            cellHead = row.createCell(i);
            cellHead.setCellValue(heardList[i]);
            cellHead.setCellStyle(style);
        }

        //设置每格数据的样式 （字体红色）
        HSSFCellStyle cellParamStyle = wb.createCellStyle();
        HSSFFont ParamFontStyle = wb.createFont();
        cellParamStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellParamStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        ParamFontStyle.setColor(HSSFColor.DARK_RED.index);   //设置字体颜色 (红色)
        ParamFontStyle.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle.setFont(ParamFontStyle);
        //设置每格数据的样式2（字体蓝色）
        HSSFCellStyle cellParamStyle2 = wb.createCellStyle();
        cellParamStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellParamStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ParamFontStyle2 = wb.createFont();
        ParamFontStyle2.setColor(HSSFColor.BLUE.index);   //设置字体颜色 (蓝色)
        ParamFontStyle2.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle2.setFont(ParamFontStyle2);
        //开始写入实体数据信息
        byte result[] = null;

        FileOutputStream out = null;
        OutputStream outputStream = null;
        int a = 2;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow roww = wbSheet.createRow((int) a);
            Map<String, Object> map = data.get(i);
            System.out.println(map + "------");
            HSSFCell cell = null;
            for (int j = 0; j < heardKey.length; j++) {
                cell = roww.createCell(j);
                cell.setCellStyle(style);
                Object valueObject = map.get(heardKey[j]);
                String value = null;
                if (valueObject == null || valueObject.equals("")) {
                    valueObject = "";
                }
                if (valueObject instanceof String) {
                    //取出的数据是字符串直接赋值
                    value = (String) map.get(heardKey[j]);
                } else if (valueObject instanceof Integer) {
                    //取出的数据是Integer
                    value = String.valueOf(((Integer) (valueObject)).floatValue());
                    if (heardKey[j].equals("type")) {
                        if (value.equals("1.0")) {
                            value = "现金券";
                        } else if (value.equals("2.0")) {
                            value = "折扣券";
                        } else if (value.equals("3.0")) {
                            value = "实物券";
                        } else {
                            value = "赠品券";
                        }
                    }
                    if (heardKey[j].equals("froms")) {
                        if (value.equals("1.0")) {
                            value = "后台发放";
                        } else if (value.equals("2.0")) {
                            value = "新人发券";
                        } else if (value.equals("3.0")) {
                            value = "，满额发券";
                        } else if (value.equals("4.0")) {
                            value = "购物发券";
                        } else if (value.equals("5.0")) {
                            value = "手工发券";
                        }
                    }
                    if (heardKey[j].equals("status")) {
                        if (value.equals("1.0")) {
                            value = "未使用";
                        } else if (value.equals("3.0")) {
                            value = "使用";
                        } else if (value.equals("2.0")) {
                            value = "锁定";
                        }
                    }
                } else if (valueObject instanceof BigDecimal) {
                    //取出的数据是BigDecimal
                    value = String.valueOf(((BigDecimal) (valueObject)).floatValue());
                } else if (valueObject.equals("")) {
                    value = null;
                }
                cell.setCellValue(Strings.isNullOrEmpty(value) ? "" : value);
                System.out.println(cell.getStringCellValue());
            }
            a++;
        }
        //导出数据浏览下载
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "" + sheetName + sf.format(date) + ".xls";
            response.setHeader("Content-disposition", "attachment;Filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            // outputStream = response.getOutputStream();
            //  out = new FileOutputStream(""+sheetName+System.currentTimeMillis()+".xls");
            response.flushBuffer();
            wb.write(response.getOutputStream());
            result = wb.getBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    wb.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }
}
