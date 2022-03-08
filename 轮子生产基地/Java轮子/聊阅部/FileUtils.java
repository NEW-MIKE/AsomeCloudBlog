package 聊阅部;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;


public class FileUtils {
    private static String path = "E:\\AmesomeCloud\\BlogManagerCloud\\轮子生产基地\\Java轮子\\聊阅部\\三国演义.txt";
    private int mbBufferLen;
    private String charset = "UTF-8";

    private int curEndPos = 0;
    public void log(String mythig){
        System.out.println(mythig);
    }

    private MappedByteBuffer mbBuff;
    public int openBook() {
        try {
            File file = new File(path);
            long length = file.length();
            if (length > 10) {
                mbBufferLen = (int) length;
                // 创建文件通道，映射为MappedByteBuffer
                mbBuff = new RandomAccessFile(file, "r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, length);
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

        /**
     * 读取下一段落
     *
     * @param readLength 当前页结束位置指针
     * @return
     */
    private byte[] readParagraphForward(int readLength) {
        byte b0;
        byte[] buf = new byte[readLength];
        for (int i = 0; i < readLength; i++) {
            b0 = mbBuff.get(curEndPos + i);
            if (b0 == '“') {
                log(b0+"woshiyi");
                //break;
            }
            buf[i] = b0;
        }
        return buf;
    }

    
    /**
     * 根据起始位置指针，读取一页内容
     *
     * @return
     */
    public String pageDown() {
        int mLength = 1000;
        String strParagraph = "";
        byte[] parabuffer = readParagraphForward(mLength);
        curEndPos += parabuffer.length;
        try {
            strParagraph = new String(parabuffer, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        strParagraph = strParagraph.replaceAll("\r\n", "  ")
                .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
        return strParagraph;
    }

    
}