package com.us.eoe.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by blitzfeng on 2017/7/19.
 */

public class DataUtil {

    private static List<String> brandList = Arrays.asList("vivo", "oppo","HUAWEI","Xiaomi","samsung","HTC","Meizu");
    private static List<String> vivoProduct = Arrays.asList("vivo Y67","vivo Y67A","vivo X5Max","vivo Y51A","vivo X5M","vivo X7","vivo V3",
            "vivo Y13L","vivo X6S A","vivo X9","vivo X9Plus");
    private static List<String> oppoProduct = Arrays.asList("R831T","R7007","R2017","OPPO A59s","R6007","R831S","OPPO R9 Plustm A","A31","OPPO R9sk",
            "OPPO R9tm","OPPO A33","N1T","OPPO R7sm","X9009","OPPO A53m","OPPO A37m","OPPO R9tm","OPPO A57");
    private static List<String> huaweiProduct = Arrays.asList("CAM-TL00H","DIG-AL00","PE-TL10","HUAWEI MLA-AL10","KIW-TL00","H30-U10",
            "BLN-AL10","HUAWEI P7-L07","HUAWEI TAG-TL00","HUAWEI G7-UL20","EVA-AL10","SCL-TL00","Y635-L03","H60-L01","PE-TL20");
    private static List<String> xiaomiProduct = Arrays.asList("MI 4LTE","Redmi 3X","MI 5s Plus","2014813","MI 5","MI 4LTE","MI NOTE LTE",
            "MI 2","Mi Note 2","MIX","Redmi Note 3","MI 3C","Redmi 4X","MI 5s Plus","HM 1SW");
    private static List<String> samsungProduct = Arrays.asList("SCH-I679","SCH-I739","GT-P5200","GT-S7278U","SM-J111F","SM-T311","GT-P5210","GT-I8262D",
            "SM-C5000","GT-I8268","SM-G900K","GT-I9003","SM-N9208","SM-G3559","GT-P6211","SM-N910U");
    private static List<String> htcProduct = Arrays.asList("HTC_D816x","HTC_E9x","HTC D516d","HTC_X9u","HTC One 801e","HTC_A9u","HTC One M8w","","HTC U11");
    private static List<String> meizuProduct = Arrays.asList("M5 Note","A680Q","M570C","m1 metal","M3X","M1 E","m2 note","M040","M5s","M681C","m1","m1 note");
    private  List<String> leProduct = Arrays.asList("");
    private static List<String> imeiList = new ArrayList<>();




    public static String getBrand(){
        Random random = new Random();
        return brandList.get(random.nextInt(7));
    }

    public static String getProduct(String brand){
        Random random = new Random();
        if("vivo".equals(brand))
            return vivoProduct.get(random.nextInt(vivoProduct.size()));
        else if("oppo".equals(brand))
            return oppoProduct.get(random.nextInt(oppoProduct.size()));
        else if("HUAWEI".equals(brand))
            return huaweiProduct.get(random.nextInt(huaweiProduct.size()));
        else if("Xiaomi".equals(brand))
            return xiaomiProduct.get(random.nextInt(xiaomiProduct.size()));
        else if("samsung".equals(brand))
            return samsungProduct.get(random.nextInt(samsungProduct.size()));
        else if("HTC".equals(brand))
            return htcProduct.get(random.nextInt(htcProduct.size()));
        else
            return meizuProduct.get(random.nextInt(meizuProduct.size()));

    }
    /**
     * 读取imei
     * @param context
     * @return
     */
    public static List<String> getImei(Context context){
        List<String> list = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("imei1");
            Reader rr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(rr);
            String imei = null;
            while ((imei = reader.readLine())!=null){
                System.out.println("im:"+imei);
                list.add(imei.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String generateSimSerialNum(){
        return "8"+getRandom(19,0);
    }

    public static String generateFingerPrint(String brand,String product){

        return brand+"/"+product+"/"+getRandom(7,0)+"/"+"release-keys";
    }

    /**
     * 读取imsi
     * @param context
     * @return
     */
    public static List<String> getImsi(Context context){
        List<String> list = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("imsi1");
            Reader rr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(rr);
            String imsi = null;
            while ((imsi = reader.readLine())!=null){
                System.out.println("imsi:"+imsi);
                list.add(imsi.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String generateIMEI() {// calculator IMEI
        int[] begins = {35,86,86,86};
        int begin = begins[new Random().nextInt(4)];
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = begin +"" + r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return input + last;
    }

    public static String generateIMSI() {
        // 460022535025034
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

    public static String generateSerial(){
        return getRandom(8,1);
    }

    public static String generateAndroidId(){
        return getRandom(16,1);
    }
    public static String generateSSID(){
        int[] num = {5,6,7,8};
        Random random = new Random();
        return getRandom(num[random.nextInt(4)],3);
    }
    public static String generateBSSID(){
        return getRandom(2,1)+":"+getRandom(2,1)+":"+getRandom(2,1)+":"+getRandom(2,1);
    }
    public static String generateMac(){
        return getRandom(2,1)+":"+getRandom(2,1)+":"+getRandom(2,1)+":"+getRandom(2,1);
    }

    public static int generateNetworkType(){
        int[] num = {13,17,18,10,0};
        Random random = new Random();
        return num[random.nextInt(5)];
    }
    public static int generatePhoneType(){
        int[] num = {0,1,2,3};
        Random random = new Random();
        return num[random.nextInt(4)];
    }
    public static String generateSimOperateorName(){
        String[] name = {"中国联通","中国电信","中国移动","中国移动"};
        Random random = new Random();
        return name[random.nextInt(4)];
    }
    public static String getSimOperator(String name){
        if("中国联通".equals(name))
            return "46001";
        else if("中国移动".equals(name))
            return "46000";
        else if("中国电信".equals(name))
            return "46003";
        else
            return "46001";
    }
    public static String generateSubscriberId(){
        String[] imsi = {"46000","46001","46002","46003"};
        Random random = new Random();
        return imsi[random.nextInt(4)];
    }


    public static String generateSDK(){
        String[] sdk = {"17","19","21","22","23","25"};
        Random random = new Random();
        return sdk[random.nextInt(6)];
    }
    public static String getRelease(String sdk){
        if("17".equals(sdk))
            return "4.2.2";
        else if("19".equals(sdk))
            return "4.4.4";
        else if("21".equals(sdk))
            return "5.0.1";
        else if("22".equals(sdk))
            return "5.1.1";
        else if("23".equals(sdk))
            return "6.0.1";
        else if("25".equals(sdk))
            return "7.1.1";
        else
            return "5.1.1";
    }

    /**
     *
     * @param length  需要获得的字符串的长度，如imei 15位
     * @param type  获得的字符串类型，0：全数字字符串，1：数字和小写字母随机字符串，2：数字和大小写字母随机字符串,3:小写字母
     * @return
     */
    public static String getRandom(int length,int type){
        if(length<1)
            return null;
        if(type>3||type<0)
            return null;
        StringBuffer buffer = new StringBuffer();
        List<String> number =new ArrayList<>();
        int total = 10;
        if(type == 0){//全数字
            number = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
            total = 10;
        }else if(type ==1){//数字和小写字母
            number = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
                    "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
            total = 36;
        }else if(type == 2){//数字、大小写字母
            number = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
                    "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "D", "G",
                    "L", "O", "S", "U", "X", "Z");
            total = 45;
        }else if(type == 3){//小写字母
            number = Arrays.asList( "a", "b", "c", "d", "e", "f", "g", "h",
                    "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
            total = 26;
        }


        for (int i = 0; i < length; i++) {
            buffer.append(number.get((int) (Math.random() * total)));
        }
        return buffer.toString();
    }

}
