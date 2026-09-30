package xyz.pbsi.Utils;


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
    public void setFirstName(String x){this.firstName = x;}
    public void setLastName(String x){this.lastName = x;}
    public void setPreferredName(String x){this.preferredName = x;}
    public void setPronouns(String x){this.pronouns = x;}
    public void setPhoneNumber(String x){this.phoneNumber = x;}
    public void setEmail(String x){this.email = x;}
    public void setPermissionSlip(boolean x){this.forms.permissionSlip = x;}
    public void setEquipmentContract(boolean x){this.forms.equipmentContract = x;}
}


class Forms{
    @SerializedName("permission slips")
    boolean permissionSlip;
    @SerializedName("equipment contract")
    boolean equipmentContract;
}

