/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import UserData.User;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SerializeJSON {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File jsonfile = new File("src/main/resources/JsonSerialization.json");
        try (FileOutputStream fos = new FileOutputStream(jsonfile)) {
            User user1 = new User(1, "Huy", "Nguyễn Thế", "huynt1608@gmail.com", "Nam", "127.0.0.1");
            User user2 = new User(2, "Nhựt", "Nguyễn Minh", "nhutnm0101@gmail.com", "Nam", "127.0.0.1");
            User user3 = new User(2, "Bảo", "Văn Hoàng", "baovh0106@gmail.com", "Nữ", "127.0.0.1");
            
            List<User> users;
            users = new ArrayList<>();
            users.add(user1);
            users.add(user2);
            users.add(user3);
            
            writeJsonStream(fos, users);
            fos.flush();
        }
        System.out.println("Done");
    }

    /**
     *Write the stream to JSON file
     * @param out
     * @param users
     * @throws IOException
     */
    public static void writeJsonStream(OutputStream out, List<User> users) throws IOException {
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"))) {
            writer.setIndent("    ");
            writer.setSerializeNulls(true);
            writer.setHtmlSafe(true);
            writeUserArray(writer, users);
        }
    }

    /**
     * Write many objects User to stream
     *
     * @param writer
     * @param users
     * @throws java.io.IOException
     */
    public static void writeUserArray(JsonWriter writer, List<User> users) throws IOException {
        writer.beginArray();
        users.forEach((User user) -> {
            try {
                writeUser(writer, user);
            } catch (IOException ex) {
                Logger.getLogger(SerializeJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        writer.endArray();
    }

    /**
     * Write object User to stream.
     *
     * @param writer
     * @param user
     * @throws IOException
     */
    public static void writeUser(JsonWriter writer, User user) throws IOException {
        //There's no need to write name/value pair in any order. But for readable code and JSON format, I suggest writing name/value pair
        //in order of User's fields which are specified in the User Class.
        writer.beginObject();
        writer.name("id").value(user.getId());
        writer.name("first_name").value(user.getFirst_name());
        writer.name("last_name").value(user.getLast_name());
        writer.name("email").value(user.getEmail());
        writer.name("gender").value(user.getGender());
        writer.name("ip_address").value(user.getIp_address());
        writer.endObject();
    }
}
