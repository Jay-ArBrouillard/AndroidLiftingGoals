package liftinggoals.misc;

public class VolumeGroupItem {
    private int imageResource;
    private String volumeGroup;

    public VolumeGroupItem(int imageResource, String volumeGroup) {
        this.imageResource = imageResource;
        this.volumeGroup = volumeGroup;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getVolumeGroup() {
        return volumeGroup;
    }

    public void setVolumeGroup(String volumeGroup) {
        this.volumeGroup = volumeGroup;
    }
}
