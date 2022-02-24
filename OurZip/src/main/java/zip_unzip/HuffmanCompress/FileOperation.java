package zip_unzip.HuffmanCompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class FileOperation {
    FileOutputStream fos;
    FileInputStream fis;

    public int[] getArrays(File file) {

        int[] arrays = new int[256];
        try{
            FileInputStream fis = new FileInputStream(file);
            int ascii=0;
            while((ascii=fis.read())!=-1) {
                arrays[ascii]++;
            }
            fis.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return arrays;
    }

    public String GetStr(HashMap map, File file) {

        String str="";      //定义字符串储存01码
        try{
            FileInputStream fis = new FileInputStream(file);
            int value=0;
            while((value=fis.read())!=-1) {
                str+=map.get((char)value+"");  //取单字符对应的01码，累加到字符串中
            }
            fis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 写HashMap到文件（写入编码个数+第一个key+第一个value所占字节数+value最后一个字节的补0情况+第一个value的若干字节+下一个key+。。。。）
     */
    public void writeHashMap(HashMap<String, String> map ,File file)
    {
        int size = map.size();          //获取编码的个数，即HashMap中的键值对个数
        String temp="";                 //存放临时8位01字符串
        int value=0;                    //存放01字符串转化得到的ASCII值
        try{
            fos = new FileOutputStream(file);
            fos.write(size);            //写HashMap长度
            Set<String> keySet = map.keySet(); //获取HashMap存放key的容器
            java.util.Iterator<String> it = (Iterator<String>) ((Set<?>) keySet).iterator();//通过容器获取迭代器
            while(it.hasNext()) {

                String key = it.next();         //取出下一个key
                String code = map.get(key);     //取出code
                fos.write(key.charAt(0));       //写key值
                int a = code.length()/8;        //能存满的字节数
                int b = code.length()%8;        //剩余的位数
                int c =1;                       //值对应的存储的字节数
                if(b==0) {

                    c=a;
                    fos.write(c);               //写code的字节数
                    fos.write(0);            //写补0数，为0个
                    for(int i=0;i<a;i++)        //写code值
                    {
                        temp="";
                        for(int j=0;j<8;j++) {
                            temp+=code.charAt(i*8+j);
                        }
                        value=StringToInt(temp);
                        fos.write(value);       //逐一把code的每一位写出去
                    }
                }
                else {
                    c=a+1;
                    fos.write(c);               //写code的字节数
                    fos.write(8-b);          //写补0数
                    for(int i=0;i<8-b;i++) {    //补0
                        code+="0";
                    }

                    for(int i=0;i<c;i++) {
                        temp="";
                        for(int j=0;j<8;j++) {
                            temp+=code.charAt(8*i+j);
                        }
                        value=StringToInt(temp);
                        fos.write(value);       //逐一写code，包括补的0
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 把文档转化为的哈夫曼编码写入文件
     */
    public void writeHFMcode(String HFMcode)
    {
        int len = HFMcode.length();     //获取HFMcode长度
        int a = len/8;                  //求出完整的字节的数目
        int b = len%8;                  //求出剩余的位数
        String temp = "";               //临时存放8位数据
        int value = 0;                  //存放8位01转化得到的值
        try {
            if(b==0) {                  //无不足八位的部分，不需要补0
                fos.write(0);        //写补0数
                for(int i=0;i<a;i++) {
                    temp="";
                    for(int j=0;j<8;j++) {
                        temp+=HFMcode.charAt(i*8+j);
                    }
                    value=StringToInt(temp);
                    fos.write(value);   //写哈夫曼编码
                }
            }
            else {                      //需要补0
                int c = 8-b;            //计算补0数
                fos.write(c);           //写补0数
                for(int i=0;i<c;i++) {
                    HFMcode+="0";
                }
                for(int i=0;i<a+1;i++) {
                    temp="";
                    for(int j=0;j<8;j++) {
                        temp+=HFMcode.charAt(i*8+j);
                    }
                    value=StringToInt(temp);
                    fos.write(value);    //写哈夫曼编码
                }
            }
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 把01字符串转化为ASCII码
     * @param temp
     * @return
     */
    public int StringToInt(String temp) {
        int value=0;
        for(int i=0;i<8;i++) {
            int x = temp.charAt(i)-48;
            if(x==1) {   //为1则累加入value
                value+=Math.pow(2,7-i);  //表示2的(7-i)次方
            }
        }
        return value;
    }
    /**
     * 把数值转化为01字符串
     * @param value
     */
    public String IntToString(int value) {
        String temp1="";                //存放反的字符串
        String temp="";                 //存放正的字符串
        while(value>0) {                //逐渐取出各个二进制位数，字符串为反向的
            temp1+=value%2;
            value=value/2;
        }
        for(int i=temp1.length()-1;i>=0;i--) {
            temp+=temp1.charAt(i);
        }
        return temp;
    }
    /**
     * 把数值转化为01字符串，数值范围在0~255,01串不超过8位
     * @param value
     */
    public String IntToStringEight(int value) {

        String temp1="";            //存放反的字符串
        String temp="";             //存放正的字符串
        int add=0;
        while(value>0) {            //逐渐取出各个二进制位数，字符串为反向的
            add++;
            temp1+=value%2;
            value=value/2;
        }
        add=8-add;
        for(int i=0;i<add;i++)  {                   //添0至8位
            temp1+="0";
        }
        for(int i=temp1.length()-1;i>=0;i--) {      //反向的字符串获取正向的字符串
            temp+=temp1.charAt(i);
        }
        return temp;
    }
    /**
     * 对外部的接口，实现把压缩后的数据和信息写入压缩文件
     * @param fileCompress
     */
    public void compressFile(File fileCompress,HashMap<String, String> map,String HFMcode) {
        writeHashMap(map, fileCompress);        //写HashMap的数据
        writeHFMcode(HFMcode);                  //继续写HFMcode 01字符串
    }
    /**
     * 解压获取HashMap
     * @param fileCompress
     */
    public HashMap<String, String> readHashMap(File fileCompress) {

        HashMap<String, String> mapGet = new HashMap<>();
        try {
            fis=new FileInputStream(fileCompress);
            int keyNumber = fis.read();     //读取key的数量
            String key = "";                //HashMap的键值对
            String code= "";                //未去0的字符串
            String codeRZ="";               //去0的字符串
            int length=0;                   //表示还原后的字符串的理论长度，解决字符串前面的0的问题
            int byteNum=1;                  //当前code占了几个字节
            int addZero=0;                  //补0数
            int value=0;                    //临时储值
            int zeroLength=0;               //code没有1的时候的字符串长度

            for(int i=0;i<keyNumber;i++) {
                key = (char)fis.read()+"";  //获取key值
                byteNum=fis.read();         //获取code的字节数
                addZero=fis.read();         //读取补0数量
                if(addZero==0) {            //没有补0，是整字节数

                    for(int k=byteNum-1;k>=0;k--) {
                        value+=fis.read()*(Math.pow(2, k*8));
                    }
                    code=IntToString(value);            //把数值转为01code
                    value=0;//清零
                    length=8*byteNum-code.length();     //计算在前面要补多少0
                    if(code.length()==0) {              //若code内数字都为0，只要去掉尾部即可
                        zeroLength=length-addZero;      //计算有多少个0
                        for(int k=0;k<zeroLength;k++) {
                            codeRZ+="0";
                        }
                    }
                    else {                              //code值不为0，补充前面的0，去掉后面的0
                        for(int k=0;k<length;k++) {
                            codeRZ+="0";
                        }
                        for(int k=0;k<code.length()-addZero;k++) {
                            codeRZ+=code.charAt(k);
                        }
                    }
                }
                else {      //有补0
                    for(int k=byteNum-1;k>=0;k--) {
                        value+=fis.read()*(Math.pow(2, k*8));
                    }
                    code=IntToString(value);            //把数值转为01code
                    value=0;                            //清0
                    length=8*byteNum-code.length();     //计算在前面要补多少0
                    if(code.length()==0) {              //若code内数字都为0，只要去掉尾部即可
                        zeroLength=length-addZero;      //计算有多少个0
                        for(int k=0;k<zeroLength;k++) {
                            codeRZ+="0";
                        }
                    }
                    else {                              //code值不为0，补充前面的0，去掉后面的0
                        for(int k=0;k<length;k++) {
                            codeRZ+="0";
                        }
                        for(int k=0;k<code.length()-addZero;k++) {  //不要后面的0
                            codeRZ+=code.charAt(k);
                        }
                    }
                }
                mapGet.put(codeRZ , key ); //把读取到的键值对存入创建的HashMap
                codeRZ=""; //清空
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return mapGet;
    }
    /**
     * 获取压缩文件中的数据，还原哈夫曼编码01串
     */
    public String readHFMStr() {

        String str1="";     //存放获取到的直接的01字符串
        String str="";      //存放去掉补0的字符串
        int value=0;
        String temp="";
        try{
            int addZero = fis.read();           //读取整个文件的补0个数
            while((value=fis.read())!=-1) {
                temp=IntToStringEight(value);   //把每个字节的数据转化为八位的01
                str1+=temp;
            }
            if(addZero!=0) {                    //有补0，获取补0前的字符串
                for(int i=0;i<str1.length()-addZero;i++) {   //补0的部分不赋值
                    str += str1.charAt(i) + "";
                }
                return str;
            }
            fis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return str1;
    }
    /**
     * 写入文件的保存路径
     * @param str
     * @param mapGet
     * @param fileCompress
     */
    public void writeFile(String str , HashMap<String, String> mapGet, File fileCompress) {

        try {
            fos = new FileOutputStream(fileCompress);   //获取文件输出流
            int len = str.length();                     //获取01串的长度
            String temp="";                             //临时存放段的01字符串
            for(int i=0;i<len;i++) {
                temp+=str.charAt(i);
                if(mapGet.containsKey(temp)) {
                    fos.write(mapGet.get(temp).charAt(0));  //一个字符的字符串转字符然后写出
                    temp="";
                }
            }
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 对外部的接口，实现解压文件，获取HashMap和文件内容
     * @param fileCompress, 压缩文件目录
     * @param fileUncompress, 解压到的目录
     */
    public void uncompressFile(File fileCompress,File fileUncompress) {
        HashMap<String, String> mapGet = readHashMap(fileCompress);     //获取哈希表
        String str = readHFMStr();                                      //获取01字符串
        writeFile(str,mapGet,fileUncompress);                           //写文件到保存路径
    }
}
