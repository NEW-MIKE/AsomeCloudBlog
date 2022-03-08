package 聊阅部;

public class Demo{

    public static String bMarker = "dd";
    private String bMarkerFlag = "";
    FileUtils fileUtils = new FileUtils();
    /*判断接下来的符号是什么符号，以及位置是什么，其核心的意义，在于不断的将数据进行切割，然后获取到切割后的，
    需要判断什么时候开始，什么时候结束，我需要获取到完整的一个部分，完整的切割，首先，其本身就是一个文件流，用什么
    样的方式来进行阅读比较合适呢，以章节作为切割的的文集，统一放在同一个文档下面，*/
    public String getTalkContent(){
        return "";
    }

    public void init(){
        
        fileUtils.openBook();
    }

    public String getStudentTalk(){
        return "";
    }

    public String getTeacherTalk(){
        return "";
    }

    public String getFileContent(){
        return fileUtils.pageDown();
    }

    public String getValidContent(){
        String templeValue = getFileContent();
        String outputValue = "";
        while(!containMarker(templeValue)){
            outputValue += templeValue;
            templeValue = getFileContent();
        }
        outputValue += templeValue.substring(0, findNextBMarker(templeValue));;
        return outputValue;
    }
    
    // public String getMyWholeNextTalk(String input){
    //     input.substring(0, findNextBMarker());
    //     return input.substring(0, findNextBMarker());
    // }

    public int findNextBMarker(String inputvalue){
        int index = 0;
        if(bMarkerFlag !=""){
            index = inputvalue.indexOf(bMarkerFlag);
            bMarkerFlag = "";
        }
        return index;
    }

    public Boolean containMarker(String MarkerContent){
        if(MarkerContent.contains("“")){
            bMarkerFlag = "“";
            return true;
        }
        return false;
    }



    public static void main(String[] args){
        Demo demo = new Demo();
        demo.init();
        System.out.println(demo.getValidContent());
        System.out.println(demo.getValidContent());
        System.out.println(demo.getValidContent());
        System.out.println(demo.getValidContent());
        System.out.println(demo.getValidContent());

        //demo.
    }
}