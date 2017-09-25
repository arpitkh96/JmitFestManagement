package pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arpitkh96 on 29/9/16.
 */

public class Fest  {

    @SerializedName("fest_id")
    @Expose
    private String festId;
    @SerializedName("fest_name")
    @Expose
    private String festName;
    @SerializedName("fest_desc")
    @Expose
    private String festDesc;

    /**
     *
     * @return
     * The festId
     */
    public String getFestId() {
        return festId;
    }

    /**
     *
     * @param festId
     * The fest_id
     */
    public Fest setFestId(String festId) {
        this.festId = festId;
        return this;
    }


    /**
     *
     * @return
     * The festName
     */
    public String getFestName() {
        return festName;
    }

    /**
     *
     * @param festName
     * The fest_name
     */
    public void setFestName(String festName) {
        this.festName = festName;
    }

    /**
     *
     * @return
     * The festDesc
     */
    public String getFestDesc() {
        return festDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fest fest = (Fest) o;

        return festId != null ? festId.equals(fest.festId) : fest.festId == null;

    }

    /**
     *
     * @param festDesc
     * The fest_desc
     */
    public void setFestDesc(String festDesc) {
        this.festDesc = festDesc;
    }

   
}
