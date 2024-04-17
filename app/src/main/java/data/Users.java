package data;

public final class Users {
    String firstName = "";
    String lastName = "";

    String email = "";

    String imagepath = "";

     public void user(String firstName, String lastName, String email, String imagepath){
         this.firstName= firstName;
         this.lastName = lastName;
         this.email = email;
         this.imagepath= imagepath;
     }
}
