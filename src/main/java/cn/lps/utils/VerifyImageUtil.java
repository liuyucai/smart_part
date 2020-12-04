package cn.lps.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @author liuyucai
 * @Created 2020/12/2
 * @Description
 */
public class VerifyImageUtil {

    /**
     * 源文件宽度
     */
    private static int ORI_WIDTH = 296;
    /**
     * 源文件高度
     */
    private static int ORI_HEIGHT = 182;
    /**
     * 模板图宽度
     */
    private static int CUT_WIDTH = 50;
    /**
     * 模板图高度
     */
    private static int CUT_HEIGHT = 50;
    /**
     * 抠图凸起圆心
     */
    private static int circleR = 6;
    /**
     * 抠图内部矩形填充大小
     */
    private static int RECTANGLE_PADDING = 6;
    /**
     * 抠图的边框宽度
     */
    private static int SLIDER_IMG_OUT_PADDING = 1;


    /**
     * 根据传入的路径生成指定验证码图片
     *
     * @param
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getVerifyImage() throws IOException {
        String filePath ="E:/develop/ideaProject/smart_part/src/main/webapp/image";
        File verifyImageImport = new File(filePath);
        File[] verifyImages = verifyImageImport.listFiles();

        Random random = new Random(System.currentTimeMillis());
        //  随机取得原图文件夹中一张图片
        File originImageFile = verifyImages[random.nextInt(verifyImages.length)];

        //  获取原图文件类型
        String originImageFileType = originImageFile.getName().substring(originImageFile.getName().lastIndexOf(".") + 1);

        //  读取原图
        BufferedImage srcImage = ImageIO.read(originImageFile);

        //设置图片尺寸2020/12/4
        BufferedImage image = new BufferedImage(ORI_WIDTH, ORI_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(srcImage, 0, 0, ORI_WIDTH, ORI_WIDTH, null);
        srcImage = image;
        //.................

        int locationX = CUT_WIDTH + new Random().nextInt(srcImage.getWidth() - CUT_WIDTH * 2);
        int locationY = CUT_HEIGHT + new Random().nextInt(srcImage.getHeight() - CUT_HEIGHT) / 2;
//        BufferedImage markImage = new BufferedImage(CUT_WIDTH,CUT_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage markImage = new BufferedImage(CUT_WIDTH,ORI_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);

        int[][] data = getBlockData2();
        cutImgByTemplate2(srcImage, markImage, data, locationX, locationY);
        Map<String, Object> resultMap = new HashMap<>();
        //放入背景图的加密信息
        resultMap.put("bigImg",getImageBASE64(srcImage));
        //放入滑块图的加密信息
        resultMap.put("slidImg",getImageBASE64(markImage));
        //放入抠图位置的X方向的信息，用于验证滑块位置是否正确
        resultMap.put("locationX",locationX);
        //放入抠图位置的Y方向的信息，用于前端控制定位信息
        resultMap.put("locationY",locationY);
        return resultMap;
    }


    /**
     * 生成随机滑块形状
     * <p>
     * 0 透明像素
     * 1 滑块像素
     * 2 阴影像素
     * @return int[][]
     */
    private static int[][] getBlockData() {
        int[][] data = new int[CUT_WIDTH][CUT_HEIGHT];
        Random random = new Random();
        //(x-a)²+(y-b)²=r²
        //x中心位置左右5像素随机
        double x1 = RECTANGLE_PADDING + (CUT_WIDTH - 2 * RECTANGLE_PADDING) / 2.0 - 5 + random.nextInt(10);
        //y 矩形上边界半径-1像素移动
        double y1_top = RECTANGLE_PADDING - random.nextInt(3);
        double y1_bottom = CUT_HEIGHT - RECTANGLE_PADDING + random.nextInt(3);
        double y1 = random.nextInt(2) == 1 ? y1_top : y1_bottom;


        double x2_right = CUT_WIDTH - RECTANGLE_PADDING - circleR + random.nextInt(2 * circleR - 4);
        double x2_left = RECTANGLE_PADDING + circleR - 2 - random.nextInt(2 * circleR - 4);
        double x2 = random.nextInt(2) == 1 ? x2_right : x2_left;
        double y2 = RECTANGLE_PADDING + (CUT_HEIGHT - 2 * RECTANGLE_PADDING) / 2.0 - 4 + random.nextInt(10);

        double po = Math.pow(circleR, 2);
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //矩形区域
                boolean fill;
                if ((i >= RECTANGLE_PADDING && i < CUT_WIDTH - RECTANGLE_PADDING)
                        && (j >= RECTANGLE_PADDING && j < CUT_HEIGHT - RECTANGLE_PADDING)) {
                    data[i][j] = 1;  //显示
                    fill = true;
                } else {
                    data[i][j] = 0; //透明
                    fill = false;
                }
                //凸出区域
                double d3 = Math.pow(i - x1, 2) + Math.pow(j - y1, 2);
                if (d3 < po) {
                    data[i][j] = 1;
                } else {
                    if (!fill) {
                        data[i][j] = 0;
                    }
                }
                //凹进区域
                double d4 = Math.pow(i - x2, 2) + Math.pow(j - y2, 2);
                if (d4 < po) {
                    data[i][j] = 0;
                }
            }
        }
        //边界阴影
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //四个正方形边角处理
                for (int k = 1; k <= SLIDER_IMG_OUT_PADDING; k++) {
                    //左上、右上
                    if (i >= RECTANGLE_PADDING - k && i < RECTANGLE_PADDING
                            && ((j >= RECTANGLE_PADDING - k && j < RECTANGLE_PADDING)
                            || (j >= CUT_HEIGHT - RECTANGLE_PADDING - k && j < CUT_HEIGHT - RECTANGLE_PADDING +1))) {
                        data[i][j] = 2;
                    }

                    //左下、右下
                    if (i >= CUT_WIDTH - RECTANGLE_PADDING + k - 1 && i < CUT_WIDTH - RECTANGLE_PADDING + 1) {
                        for (int n = 1; n <= SLIDER_IMG_OUT_PADDING; n++) {
                            if (((j >= RECTANGLE_PADDING - n && j < RECTANGLE_PADDING)
                                    || (j >= CUT_HEIGHT - RECTANGLE_PADDING - n && j <= CUT_HEIGHT - RECTANGLE_PADDING ))) {
                                data[i][j] = 2;
                            }
                        }
                    }
                }

                if (data[i][j] == 1 && j - SLIDER_IMG_OUT_PADDING > 0 && data[i][j - SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j - SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && j + SLIDER_IMG_OUT_PADDING > 0 && j + SLIDER_IMG_OUT_PADDING < CUT_HEIGHT && data[i][j + SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j + SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && i - SLIDER_IMG_OUT_PADDING > 0 && data[i - SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i - SLIDER_IMG_OUT_PADDING][j] = 2;
                }
                if (data[i][j] == 1 && i + SLIDER_IMG_OUT_PADDING > 0 && i + SLIDER_IMG_OUT_PADDING < CUT_WIDTH && data[i + SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i + SLIDER_IMG_OUT_PADDING][j] = 2;
                }
            }
        }

        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
              System.out.print(data[i][j]);
            }
            System.out.println();
        }
        return data;
    }

    private static int[][] getBlockData2() {
        int[][] data = new int[ORI_HEIGHT][CUT_WIDTH];

        int blockWidth = 38;
        int blockHeight =38;
        int rp = 2;



        int topPadding = 20;
        int bottomPadding = ORI_HEIGHT-topPadding-blockHeight-rp-circleR;


        int topx=RECTANGLE_PADDING + blockWidth/2;
        int topy = topPadding+circleR;

        int rightx = RECTANGLE_PADDING + blockWidth - rp;
        int righty = topPadding + circleR + rp+blockHeight/2;

        int bottomY = topPadding + circleR + rp +blockHeight;




        for (int i = 0; i < ORI_HEIGHT; i++) {
            for (int j = 0; j < CUT_WIDTH; j++) {
                //矩形区域
                boolean fill;

                //j ->x
                //i ->y

                if ((j >= RECTANGLE_PADDING && j < CUT_WIDTH - RECTANGLE_PADDING)
                        && (i >= topPadding && i <= bottomY)) {
                    data[i][j] = 1;  //显示
                    fill = true;

                    double d4 = Math.pow(j - topx, 2) + Math.pow(i - topy, 2);
                    double po = Math.pow(circleR, 2);
                    //上边突出
                    if((i<(topPadding+circleR+4))&&d4>po ){
                        data[i][j] = 0;  //不可见
                    }
                    //右边凹进去
                    if(i>(topPadding+circleR+rp)){
                        d4 = Math.pow(j-rightx, 2) + Math.pow(i - righty, 2);
                        if(d4<=po){
                            data[i][j] = 0;  //显示
                        }
                    }
                } else {
                    data[i][j] = 0; //透明
                    fill = false;
                }
            }


        }

        for (int i = 0; i < ORI_HEIGHT; i++) {
            for (int j = 0; j < CUT_WIDTH; j++) {
                System.out.print(data[i][j]);
            }
            System.out.println();
        }
        return data;
    }

    /**
     * 裁剪区块
     * 根据生成的滑块形状，对原图和裁剪块进行变色处理
     * @param oriImage 原图
     * @param targetImage 裁剪图
     * @param blockImage 滑块
     * @param x  裁剪点x
     * @param y  裁剪点y
     */
    private static void cutImgByTemplate(BufferedImage oriImage, BufferedImage targetImage, int[][] blockImage, int x, int y) {
        for (int i = 0; i < CUT_WIDTH; i++) {
//        for (int i = 0; i < ORI_HEIGHT; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
//            for (int j = 0; j < CUT_WIDTH; j++) {
                int _x = x + i;
                int _y = y + j;
                int rgbFlg = blockImage[i][j];
                int rgb_ori = oriImage.getRGB(_x, _y);
                // 原图中对应位置变色处理
                if (rgbFlg == 1) {
                    //抠图上复制对应颜色值
                    targetImage.setRGB(i,j, rgb_ori);
                    //原图对应位置颜色变化
                    oriImage.setRGB(_x, _y, rgb_ori & 0x363636);
                } else if (rgbFlg == 2) {
                    targetImage.setRGB(i, j, Color.WHITE.getRGB());
                    oriImage.setRGB(_x, _y, Color.GRAY.getRGB());
                }else if(rgbFlg == 0){
                    //int alpha = 0;
                    targetImage.setRGB(i, j, rgb_ori & 0x00ffffff);
                }
            }

        }
    }

    private static void cutImgByTemplate2(BufferedImage oriImage, BufferedImage targetImage, int[][] blockImage, int x, int y) {
//        for (int i = 0; i < CUT_WIDTH; i++) {
        for (int i = 0; i < CUT_WIDTH; i++) {
//            for (int j = 0; j < CUT_HEIGHT; j++) {
            for (int j = 0; j < ORI_HEIGHT; j++) {
                int _x = y + i;
                int _y = x + j;
//                int rgbFlg = blockImage[i][j];
                int rgbFlg = blockImage[j][i];
                int rgb_ori = oriImage.getRGB(_x, _y);
                // 原图中对应位置变色处理
                if (rgbFlg == 1) {
                    //抠图上复制对应颜色值
                    targetImage.setRGB(i,j, rgb_ori);
                    //原图对应位置颜色变化
                    oriImage.setRGB(_y, _x, rgb_ori & 0x363636);
                } else if (rgbFlg == 2) {
                    targetImage.setRGB(i, j, Color.WHITE.getRGB());
                    oriImage.setRGB(_y, _x, Color.GRAY.getRGB());
                }else if(rgbFlg == 0){
                    //int alpha = 0;
                    targetImage.setRGB(i, j, rgb_ori & 0x00ffffff);
                }
            }
        }
    }


    /**
     * 随机获取一张图片对象
     * @param path
     * @return
     * @throws IOException
     */
    public static BufferedImage getRandomImage(String path) throws IOException {
        File files = new File(path);
        File[] fileList = files.listFiles();
        List<String> fileNameList = new ArrayList<>();
        if (fileList!=null && fileList.length!=0){
            for (File tempFile:fileList){
                if (tempFile.isFile() && tempFile.getName().endsWith(".jpg")){
                    fileNameList.add(tempFile.getAbsolutePath().trim());
                }
            }
        }
        Random random = new Random();
        File imageFile = new File(fileNameList.get(random.nextInt(fileNameList.size())));
        return ImageIO.read(imageFile);
    }

    /**
     * 将IMG输出为文件
     * @param image
     * @param file
     * @throws Exception
     */
    public static void writeImg(BufferedImage image, String file) throws Exception {
        byte[] imagedata = null;
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        ImageIO.write(image,"png",bao);
        imagedata = bao.toByteArray();
        FileOutputStream out = new FileOutputStream(new File(file));
        out.write(imagedata);
        out.close();
    }

    /**
     * 将图片转换为BASE64
     * @param image
     * @return
     * @throws IOException
     */
    public static String getImageBASE64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image,"png",out);
        //转成byte数组
        byte[] bytes = out.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        //生成BASE64编码
        return encoder.encode(bytes);
    }

    /**
     * 将BASE64字符串转换为图片
     * @param base64String
     * @return
     */
    public static BufferedImage base64StringToImage(String base64String) {
        try {
            BASE64Decoder decoder=new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}