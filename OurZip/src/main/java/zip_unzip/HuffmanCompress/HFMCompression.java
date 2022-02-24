package zip_unzip.HuffmanCompress;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class HFMCompression {
    public static void HFMZip(String src,String zip)
    {
        HFMCompression hc = new HFMCompression();
        File file  = new File(src);//源文件地址
        FileOperation fo = new FileOperation();
        int [] a = fo.getArrays(file);

        LinkedList<Node<String>> list = hc.createNodeList(a);   //把数组的元素转为节点并存入链表
        Node<String> root = hc.CreateHFMTree(list);             //建树
        HashMap<String,String> map = hc.getAllCode(root);       //获取字符编码HashMap
        String str = fo.GetStr(map, file);
        File fileCompress = new File(zip);                      //压缩文件地址
        fo.compressFile(fileCompress,map,str);                  //生成压缩文件
    }

    public static void HFMUnZip(String zip,String dst)
    {
        FileOperation fo = new FileOperation();
        File fileCompress = new File(zip);                      //压缩文件地址
        File fileUncompress = new File(dst);                    //解压后文件地址
        fo.uncompressFile(fileCompress,fileUncompress);         //解压文件至fileUncompress处
    }

    public LinkedList<Node<String>> createNodeList(int[] arrays)
    {
        LinkedList<Node<String>> list = new LinkedList<>();
        for(int i=0;i<arrays.length;i++)
        {
            if(arrays[i]!=0)
            {
                String ch = (char)i+"";
                Node<String> node = new Node<String>(ch,arrays[i]);     //构建节点并传入字符和权值
                list.add(node);
            }
        }
        return list;
    }

    public void sortList(LinkedList<Node<String>> list) {

        for(int i=list.size();i>1;i--) {

            for(int j=0; j<i-1;j++) {

                Node<String> node1 = list.get(j);
                Node<String> node2 = list.get(j+1);

                if(node1.getWeight()>node2.getWeight()) {

                    int temp ;
                    temp = node2.getWeight();
                    node2.setWeight(node1.getWeight());
                    node1.setWeight(temp);
                    String tempChar;
                    tempChar = node2.getData();
                    node2.setData(node1.getData());
                    node1.setData(tempChar);
                    Node<String> tempNode = new Node<String>(null, 0);
                    tempNode.setLeft(node2.getLeft());
                    tempNode.setRight(node2.getRight());
                    node2.setLeft(node1.getLeft());
                    node2.setRight(node1.getRight());
                    node1.setLeft(tempNode.getLeft());
                    node1.setRight(tempNode.getRight());
                }
            }
        }
    }

    public Node<String> CreateHFMTree(LinkedList<Node<String>> list) {

        while(list.size()>1) {

            sortList(list);     //排序节点链表
            Node<String> nodeLeft = list.removeFirst();
            Node<String> nodeRight = list.removeFirst();
            Node<String> nodeParent = new Node<String>( null ,nodeLeft.getWeight()+nodeRight.getWeight());
            nodeParent.setLeft(nodeLeft);
            nodeParent.setRight(nodeRight);
            list.addFirst(nodeParent);
        }
        return list.get(0);     //返回根节点
    }

    public HashMap<String, String> getAllCode(Node<String> root) {

        HashMap<String, String> map = new HashMap<>();
        inOrderGetCode("", map, root);
        return map;
    }

    public void inOrderGetCode(String code ,HashMap<String, String> map,Node<String> root) {

        if(root!=null) {

            inOrderGetCode(code+"0",map,root.getLeft());

            if(root.getLeft()==null&&root.getRight()==null) {
                map.put(root.getData(), code);
            }                                                    //存储叶子结点的哈夫曼编码

            inOrderGetCode(code+"1",map,root.getRight());
        }
    }

}
