package bd.dkltd.dscode;

public class Paths {
    private String pathName,pathValue;

	public Paths(String pathName, String pathValue) {
		this.pathName = pathName;
		this.pathValue = pathValue;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathValue(String pathValue) {
		this.pathValue = pathValue;
	}

	public String getPathValue() {
		return pathValue;
	}
}
