package bd.dkltd.dscode;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;

public class ExpandList {
    
    private ImageView dropdownIcon;
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private boolean expanded;

    public ExpandList() {
        super();
    }

    public ExpandList(ImageView dropdownIcon, RelativeLayout relativeLayout, boolean expanded) {
        this.dropdownIcon = dropdownIcon;
        this.relativeLayout = relativeLayout;
        this.expanded = expanded;
    }

    public ExpandList(ImageView dropdownIcon, RelativeLayout relativeLayout, FrameLayout frameLayout, boolean expanded) {
        this.dropdownIcon = dropdownIcon;
        this.relativeLayout = relativeLayout;
        this.frameLayout = frameLayout;
        this.expanded = expanded;
    }

    public void setDropdownIcon(ImageView dropdownIcon) {
        this.dropdownIcon = dropdownIcon;
    }

    public ImageView getDropdownIcon() {
        return dropdownIcon;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
