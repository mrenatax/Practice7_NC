package pr7.Selenium;

public enum Role {
    ADMIN("Admin"),
    READ_ONLY("Read Only"),
    READ_WRITE("Read / Write");

    private String role;
    Role (String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
    public String getRoleAsAttribute(){
       if (role.equals(ADMIN.getRole())){
           return "Admin";
       }else if (role.equals(READ_ONLY.getRole())){
           return "RO";
       }else if(role.equals(READ_WRITE.getRole())){
           return "RW";
       }else {
           return null;
       }
    }
}
