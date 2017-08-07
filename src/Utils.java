public class Utils {

    public static String login(String username, String password) {
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")){
            return  "{result:true}";
        }else {
            return  "{result:false}";
        }
    }

    public static void SaveCanvas(String canvas) {
        
    }

}
