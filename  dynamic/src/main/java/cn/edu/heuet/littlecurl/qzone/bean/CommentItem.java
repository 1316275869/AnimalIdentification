package cn.edu.heuet.littlecurl.qzone.bean;

public class CommentItem {
    //评论
    // 回复人的名字
    private String name;
    // 被回复人的名字
    private String toName;
    // 评论内容
    private String content;

    public CommentItem(String name, String toName, String content) {
        this.name = name;
        this.toName = toName;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
