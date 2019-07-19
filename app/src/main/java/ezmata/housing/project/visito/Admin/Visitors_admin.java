package ezmata.housing.project.visito.Admin;

public class Visitors_admin {

    private String visitorName;
    private String visitorFlat;
    private String visitorPhone;
    private String visitorReason;
    private String dati;
    private String imageUri;
    private String allowance,id;

public Visitors_admin()
{}

    public Visitors_admin(String visitorName, String visitorFlat, String visitorPhone, String visitorReason,String dati,String imageUri,String allowance,String id) {
        this.visitorName = visitorName;
        this.visitorFlat = visitorFlat;
        this.visitorPhone = visitorPhone;
        this.visitorReason = visitorReason;
        this.dati=dati;
        this.id=id;
        this.imageUri=imageUri;
        this.allowance=allowance;
    }


    public String getAllowance() {
        return allowance;
    }
    public String getId() {
        return id;
    }

    public void setAllowance(String allowance) {
        this.allowance=allowance;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getdati() {
        return dati;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getVisitorFlat() {
        return visitorFlat;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public String getVisitorReason() {
        return visitorReason;
    }
}
