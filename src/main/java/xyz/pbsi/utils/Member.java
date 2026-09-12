package xyz.pbsi.utils;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Member {
    @SerializedName("first name")
    String firstName;
    @SerializedName("last name")
    String lastName;
    @SerializedName("preferred name")
    String preferredName;
    @SerializedName("pronouns")
    String pronouns;
    @SerializedName("id")
    String studentID;
    @SerializedName("email")
    String email;
    @SerializedName("phone number")
    String phoneNumber;
    @SerializedName("forms")
    Forms forms;
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getPreferredName(){
        return this.preferredName;
    }
    public String getPronouns(){
        return this.pronouns;
    }
    public String getStudentID(){
        return this.studentID;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public boolean getPermissionSlip(){
        return this.forms.permissionSlip;
    }
    public boolean getEquipmentContract(){
        return this.forms.equipmentContract;
    }




}


class Forms{
    @SerializedName("permission slip")
    boolean permissionSlip;
    @SerializedName("equipment contract")
    boolean equipmentContract;
}

