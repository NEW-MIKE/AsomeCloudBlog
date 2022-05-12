package 聊阅部;

import java.util.ArrayList;
import java.util.List;

public class SaveContentHistory {
    private List<String> SaveContent = new ArrayList<>();
    private int endCurPos;
    private String LastContent;

    public SaveContentHistory(){
        LastContent = new String();
    }
    public void setLastContent(String lastContent){
        this.LastContent = lastContent;
    }

    public String getLastContent(){
        return LastContent;
    }

    public void setEndCurPos(int endPos){
        this.endCurPos = endPos;
    }
    public int getEndCurPos(){
        return endCurPos;
    }

    public void setSaveContent(List<String> saveContent)
    {
        for(int i=0;i<SaveContent.size();i++){
            SaveContent.remove(i);
        }
        for(String content:saveContent){
            SaveContent.add(content);
        }
    }

    public List<String> getSaveContent(){
        return SaveContent;
    }
    
}