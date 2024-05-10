package data;

public final class Users {
    String name = "";
    String username = "";
    String downloadUrl = "";

    String[] posts;

     public void user(String name, String username, String downloadUrl){
         this.name= name;
         this.username = username;
         this.downloadUrl = downloadUrl;
     }

     public String getName(){
         return name;
     }

     public String getPrice(){
         return "1";
     }

     public String getImageUrl(){
         return downloadUrl;
     }
}
