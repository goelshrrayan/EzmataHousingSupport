package ezmata.housing.project.visito.SuperAdmin.NoticeWork;

public class Notice {
    String content,title,date;

   public Notice()
    {}

    public Notice(String content, String title, String date) {
        this.content = content;
        this.title = title;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
