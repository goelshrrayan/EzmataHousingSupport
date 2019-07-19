package ezmata.housing.project.visito;

public class Visitors {

    String name,reason,phone,dati,imageUri,allowance;


    public Visitors()
    {}

    public Visitors(String name, String reason, String phone, String dati, String imageUri,String allowance) {
        this.name = name;
        this.reason = reason;
        this.phone = phone;
        this.dati = dati;
        this.imageUri = imageUri;
        this.allowance=allowance;

    }

    public String getName() {
        return name;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDati() {
        return dati;
    }

    public void setDati(String dati) {
        this.dati = dati;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
