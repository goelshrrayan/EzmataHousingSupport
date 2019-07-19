package ezmata.housing.project.visito.SuperAdmin;

public class AllotList {

    private String gmail,flat_no;

    public AllotList(){

    }

    public AllotList(String gmail, String flat_no) {
        this.gmail = gmail;
        this.flat_no = flat_no;
    }

    public String getGmail() {
        return gmail;
    }

    public String getFlat_no() {
        return flat_no;
    }
}
