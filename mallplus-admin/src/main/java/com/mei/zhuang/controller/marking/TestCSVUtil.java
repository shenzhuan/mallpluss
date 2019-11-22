package com.mei.zhuang.controller.marking;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class TestCSVUtil extends Thread {
    private final int STOP = -1;
    private final int SUSPEND = 0;
    private final int RUNNING = 1;
    private int status = 1;
    private long count = 0;

    private String name;

    public TestCSVUtil(String name) {
        this.name = name;
    }
    public TestCSVUtil() {

    }

    public synchronized void run() {
        int aa=101;
        // 判断是否停止
        while (status != STOP) {
            // 判断是否挂起
            if (status == SUSPEND) {
                try {
                    // 若线程挂起则阻塞自己
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("线程异常终止...");
                }
            } else {
                count++;
                System.out.println(this.name + "第" + count + "次运行...");
                for(int i=1;i<=10;i++){
                    System.out.println(i+"----");
                }
                aa-=10;
                System.out.println(aa);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("线程异常终止...");
                }
            }
        }
    }

    /**
     * 恢复
     */
    public synchronized void myResume() {
        // 修改状态
        status = RUNNING;
        // 唤醒
        notifyAll();
    }

    /**
     * 挂起
     */
    public void mySuspend() {
        // 修改状态
        status = SUSPEND;
    }

    /**
     * 停止
     */
    public void myStop() {
        // 修改状态
        status = STOP;
    }




    /*  static class MyTask implements Runnable {*/


       /*  @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                while (true) {
                    System.out.println("线程一直执行中");
                    //有二种方式来中断线程的执行，不采取任何措施的话，线程会进行到执行完
                    //第一种：使用抛出异常的办法
                    if (Thread.currentThread().interrupted()) {
                        System.out.println("线程被中断了");
                        throw new InterruptedException();
                    }
                    //第二种：使用return；但是一般推荐第一种
//				if(Thread.currentThread().interrupted()){
//					System.out.print("线程被中断了");
//					return;
//				}
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }*/






    /*    private int user;
        private Semaphore semaphore;

        public  MyTask(Semaphore semaphore,int user){
            this.semaphore=semaphore;
            this.user=user;
        }*/
      /*  @Override
        public void run() {

            try {
                //获取信号量
                semaphore.acquire();
                //运行这说明
                System.out.println("用户"+user+"进窗口，准备买票");
                Thread.sleep((long)(Math.random()*4000));
                System.out.println("用户"+user+"进窗口，以买票准备离开");
                Thread.sleep((long)(Math.random()*4000));
                System.out.println("用户"+user+"进窗口，以买票离开");
                semaphore.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

   /* private void execute(){
        //窗口数
        final Semaphore s=new Semaphore(10);
        //线程池
        ExecutorService thread= Executors.newCachedThreadPool();
        for(int i=0; i<20 ;i++){
         //   thread.execute(new MyTask(s,i));
        }
        thread.shutdown();
    }*/

    //private static MemberCouponService couponMapper;
    @Resource
    public static EsMemberCouponMapper couponMapper;

    /**
     * @param multipartFile
     * @param request
     * @return
     * @throws Exception
     * @TODO spring mvc 方式文件上传
     */
    public static String uploadFileCSV(MultipartFile multipartFile,
                                       HttpServletRequest request, String realPath) throws Exception {
        // String realPath =
        // request.getSession().getServletContext().getRealPath(_path);
        String newFileName = System.currentTimeMillis() + ".csv";

        File path = new File(realPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String fileName = multipartFile.getOriginalFilename();
        fileName = realPath + File.separator + newFileName;
        File restore = new File(fileName);
        multipartFile.transferTo(restore);
        return newFileName;
    }


    //文件读取
    public List<String> readcsvFile(String URL) throws IOException {
        StringBuffer sb = new StringBuffer();
        URLConnection connection = null;
        BufferedReader br = null;
        String everyLine;
        List<String> allString = new ArrayList<>();
        try {

            URL url = new URL(URL);
            connection = url.openConnection();
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "GBK"));//gb2312
            String temp = null;
            while ((temp = br.readLine()) != null) {
                String[] info = temp.split(",");
                everyLine = new String(info[0]);
                allString.add(new StringBuilder().append(everyLine).toString());
            }
            allString.remove(0);
            System.out.println(allString.toString());
        } catch (Exception e) {
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return allString;
    }


    /**
     * 导出Excel
     */
    public static HSSFWorkbook getHSSFWorkbook(EsMemberCoupon coupon) throws IOException {

        /*// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式


        //声明列对象
        HSSFCell cell = null;*/
        //获取数据
        // List<Map<String, Object>> maps = couponMapper.selectMemberCoupon(coupon);
        //创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet("发放记录");
        //创建HSSFRow对象
        HSSFRow row = sheet.createRow(0);
        //创建HSSFCell对象
        HSSFCell cell = row.createCell(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //设置单元格的值
        cell.setCellValue("发放记录导出");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        //在sheet里创建第二行
        HSSFRow row2 = sheet.createRow(1);
        //创建单元格并设置单元格内容
       /* row2.createCell(0).setCellValue("优惠券类型");
        row2.createCell(1).setCellValue("优惠券名称");
        row2.createCell(2).setCellValue("用户信息");
        row2.createCell(3).setCellValue("获得方式");
        row2.createCell(4).setCellValue("获得时间");
        row2.createCell(5).setCellValue("状态");
        row2.createCell(6).setCellValue("使用时间");
        row2.createCell(7).setCellValue("使用订单号");*/
        String[] title = {"优惠券类型", "优惠券名称", "用户信息", "获得方式", "获得时间", "状态", "使用时间", "使用订单号"};
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
            System.out.println(cell + "22222222222222222222222");
        }
        String[][] values = new String[1][];
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        //输出Excel文件
        FileOutputStream output = new FileOutputStream("d:\\发放记录.xls");
        wb.write(output);
        output.flush();

        return wb;
    }


    public static void main(String[] args) {
       /* Thread thread = new Thread(new MyTask());
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        thread.interrupt();*/
        TestCSVUtil demo = new TestCSVUtil("测试线程");
        demo.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("主线程异常终止...");
        }
        System.out.println("测试线程即将被挂起...");
        demo.mySuspend();  //停止
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("主线程异常终止...");
        }
        System.out.println("测试线程即将被唤醒...");
        demo.myResume();  //恢复
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("主线程异常终止...");
        }
        System.out.println("终止测试线程...");
        demo.myStop();//-1  停止
        System.out.println("主线程终止...");
    }







       /* String sheetTitle = "导出发放优惠券信息";
        String[] title = {"优惠券类型","优惠券名称","用户信息","获得方式","获得时间","状态","使用时间","使用订单号"};
        EsMemberCoupon cou=new EsMemberCoupon();
        cou.setFroms(1);
        List<Map<String, Object>> maps = couponMapper.selectMemberCoupon(cou);
        List<Object> list = new ArrayList<Object>();
        list.add(maps);
        System.out.println(list.toString());*/
    //  byte b[] = ExcelExportUtil.export(sheetTitle,title, list);

    //    File f = new File("G:\\tmp\\"+sheetTitle+".xls");

    /*  String path="https://hrecminiprogramstorage.blob.core.chinacloudapi.cn/test-mini/file/4331250cf69050397fb78cf168a1b755.csv";
  System.out.println(readcsvFile(path));*/
    // String restor="C:\\Users\\mayn\\Desktop\\test\\openid.csv";
    // System.out.println(getSource(restor)+"--------------");
    EsMemberCoupon coupon = new EsMemberCoupon();
    // getHSSFWorkbook(null);
      /*  File f = new File("G:\\tmp\\"+sheetTitle+".xls");
        try {
           // FileUtils.writeByteArrayToFile(f, b, true);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    EsMemberCouponController couponController = new EsMemberCouponController();
    String schemeId = "发放记录";
    //List<Map<String, Object>> maps = couponMapper.selectMemberCoupon(coupon);
    //System.out.println(maps+"2222222222222222222");






}
