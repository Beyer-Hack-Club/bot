package xyz.pbsi.utils;

public class Roles {
    public static enum RolesList {
        ADMIN,
        LEADERSHIP,
        SOCIALMEDIA,
        MEMBER,
    }
    public static String getRoleID(RolesList role)
    {
        return switch (role) {
            case ADMIN -> "1488741240353722500";
            case LEADERSHIP -> "1488731960053469337";
            case SOCIALMEDIA -> "1549091886361215028";
            case MEMBER -> "1488732014982074408";
        };
    }
}
