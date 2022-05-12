package 聊阅部;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class downReader implements Reader {

    
    private int curEndPos = 0;
    
    FileOpenUtils fileUtils = new FileOpenUtils();
    private String lastContent="";
    private List<String> mReaderContent = new ArrayList<>();
    public downReader(){
        mReaderContent.add("001");
        mReaderContent.add("002");
        mReaderContent.add("003");
        RecoveryLastOrInitContent();
    }

    private void RecoveryLastOrInitContent(){
        SaveContentHistory saveContentHistory = new SaveContentHistory();
        for(String content :saveContentHistory.getSaveContent()){
            mReaderContent.add(content);
        }
        System.out.println(fileUtils.openBook(saveContentHistory.getEndCurPos()));
        this.lastContent = saveContentHistory.getLastContent();
    }
    @Override
    public List<String> getContentList() {
        // TODO Auto-generated method stub
        if(mReaderContent.size() > 2)
            mReaderContent.remove(0);
        mReaderContent.add(ManageLastContent());
        return mReaderContent;
    }

    public void saveReadHistory(){
        int saveCurse = fileUtils.getCurEndPos();
        String saveLastContent = lastContent;
        //mReaderContent save
    }

    public int getReadHistory(){
        return 1;
    }

    /**此处的逻辑，是对于获取到的段落进行处理，如果不存在，那么，就需要进行进行下一次读取了，如果存在，那么，就从这里面获取，并且截断 */
    private String ManageLastContent(){
        int index;
        StringBuilder temp = new StringBuilder();
        String tempe;
        if((index = isContentValid(lastContent)) == -1){
            temp.append(lastContent);
            temp.append(readNewParagraph());
            return temp.toString();
        }
        tempe = lastContent.substring(0,index);
        lastContent = lastContent.substring(index+1);
        return tempe;
    }

    private StringBuilder readNewParagraph(){
        String paragraph = fileUtils.readNextParagraph();
        StringBuilder mBuffer = new StringBuilder();
        int index;
        while((index = isContentValid(paragraph)) == -1){
            mBuffer.append(paragraph);
            paragraph =  fileUtils.readNextParagraph();
        }
        mBuffer.append(paragraph.subSequence(0, index));
        lastContent = paragraph.substring(index+1);
       // System.out.println(lastContent+"000009");
        return mBuffer;
    }
    private int isContentValid(String inputParagraph){
        String[] flag = {"“","”","\"","’","‘","'"};
        int index = -1;
        for(int i=0;i<flag.length;i++){
            if(inputParagraph.indexOf(flag[i])==-1){
                //
                continue;
            }
            if (index == -1){
                index = inputParagraph.indexOf(flag[i]);
            }
            else if(index > inputParagraph.indexOf(flag[i])){
                index = inputParagraph.indexOf(flag[i]);
            }
        }
        return index;
    }

    public static void main(String[] args){
        int flag = 0;
        downReader mDownReader = new downReader();
        List<String> temp = mDownReader.getContentList();
        Scanner src = new Scanner(System.in);
        if(flag == 0)
        System.out.println(temp);
        temp = mDownReader.getContentList();
        if(flag == 0)
        System.out.println(temp);
        while(src.nextLine()!="aa"){

            temp = mDownReader.getContentList();
           
            System.out.println("////////////////////////////////");
            if(flag == 0)
            System.out.println(temp.get(2));
        }
        if(flag == 0)
        System.out.println(temp);
        temp = mDownReader.getContentList();
        if(flag == 0)
        System.out.println(temp);
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
        temp = mDownReader.getContentList();
        System.out.println("////////////////////////////////");
        if(flag == 0)
        System.out.println(temp.get(2));
    }

}
    