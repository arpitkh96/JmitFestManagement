package com.jmit.festmanagement.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class Registration implements Parcelable {
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("ph_no")
    @Expose
    private String phNo;
    @SerializedName("email")
    @Expose
    private String email;

    protected Registration(Parcel in) {
        rollNo = in.readString();
        userName = in.readString();
        department = in.readString();
        phNo = in.readString();
        email = in.readString();
    }

    public static final Creator<Registration> CREATOR = new Creator<Registration>() {
        @Override
        public Registration createFromParcel(Parcel in) {
            return new Registration(in);
        }

        @Override
        public Registration[] newArray(int size) {
            return new Registration[size];
        }
    };

    /**
     *
     * @return
     * The rollNo
     */
    public String getRollNo() {
        return rollNo;
    }

    /**
     *
     * @param rollNo
     * The roll_no
     */
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    /**
     *
     * @return
     * The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     * The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     * The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     *
     * @param department
     * The department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     *
     * @return
     * The phNo
     */
    public String getPhNo() {
        return phNo;
    }

    /**
     *
     * @param phNo
     * The ph_no
     */
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rollNo);
        dest.writeString(userName);
        dest.writeString(department);
        dest.writeString(phNo);
        dest.writeString(email);
    }
}