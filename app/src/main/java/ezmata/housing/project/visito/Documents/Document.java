package ezmata.housing.project.visito.Documents;

public class Document {
    String uri,name;

    public Document(String uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public Document()
    {}

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
