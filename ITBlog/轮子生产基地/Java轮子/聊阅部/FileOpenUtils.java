package 聊阅部;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;


public class FileOpenUtils {
    private static String path = "E:\\AmesomeCloud\\BlogManagerCloud\\轮子生产基地\\Java轮子\\聊阅部\\测试.txt";
    private int mbBufferLen;
    private String charset = "UTF-8";

    private int curEndPos = 0;
    public void log(String mythig){
        System.out.println(mythig);
    }

    private MappedByteBuffer mbBuff;
    public int openBook(int inputCurEndPos) {
        curEndPos = inputCurEndPos;
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
     * 读取下一段落，对于这里的策略，应该是一个段落一个段落的读取，这可以成为自己的自己的一个基础的一个单位，
     *
     * @param readLength 当前页结束位置指针
     * @return
     */
    private byte[] readParagraphForward(int curEndPos) {
        byte b0;
        int i = curEndPos;
        while (i < mbBufferLen) {
            b0 = mbBuff.get(i++);
            if (b0 == 0x0a) {
                break;
            }
        }
        int nParaSize = i - curEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mbBuff.get(curEndPos + i);
        }
        return buf;
    }

    public int getCurEndPos(){
        return curEndPos;
    }

    public String readNextParagraph(){
        String strParagraph = "";
        byte[] parabuffer = readParagraphForward(curEndPos);
        curEndPos += parabuffer.length;
        try {
            strParagraph = new String(parabuffer, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        strParagraph = strParagraph.replaceAll("\r\n", "  ")
                .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
        return AsciiUtil.sbc2dbcCase(strParagraph);
    }
}