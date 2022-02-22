package bd.dkltd.dscode;

public class SrcPaths {
    private String pathName,pathSource;

    public SrcPaths(String pathName, String pathSource) {
        this.pathName = pathName;
        this.pathSource = pathSource;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    public String getPathSource() {
        return pathSource;
    }
}
