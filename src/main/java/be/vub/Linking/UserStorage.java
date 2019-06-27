package be.vub.Linking;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    //String fileNameUsers = "c:/temp/usersSample.txt";
    //String fileNameUsers = "usersData.txt";
    public static final PropertiesLoader propertiesLoader=new PropertiesLoader();
    public static final String fileNameUsers = propertiesLoader.getProp("users.filename");
    public boolean createUser(User user){
        try {
            writeUser(user);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void writeUser(User user) throws IOException
    {
        String fileContent = user.getFirstName()+";"+user.getName()+";"+user.getEmail()+";"+user.getPassword()+";"+user.getExperience()+";"+user.getAgentID()+"\n";

        FileWriter fileWriter = new FileWriter(fileNameUsers, true);
        fileWriter.write(fileContent);
        fileWriter.close();
    }

    private List<User> readUsers(){
        List<User> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileNameUsers), StandardCharsets.UTF_8);
            for (String element : lines) {
                System.out.println("element="+element);
                String[] arrOfStr = element.split(";", 0);
                User user = new User(arrOfStr[1],arrOfStr[0], arrOfStr[3], arrOfStr[2], arrOfStr[4], arrOfStr[5]);
                users.add(user);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return users;
    }

    public boolean userCorrect(String email, String password){
        User tempUser=new User(email, password);
        for (User element : readUsers()) {
            if (element.equals(tempUser)){
                return true;
            }
        }
        return false;
    }

    public boolean userAlreadyExcists(String email){
        for (User element : readUsers()) {
            if (element.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    public User getUserInfo(String email){
        User requestedUser=new User();
        for (User element : readUsers()) {
            if (element.getEmail().equals(email)){

                return new User(element.getName(), element.getFirstName(), "unknown", email, "0");
            }
        }
        return requestedUser;
    }
}
