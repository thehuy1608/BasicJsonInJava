/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import UserData.User;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class DeserializeJSON implements Serializable {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File jsonfile = new File("src/main/resources/JsonDeserialization.json");
        InputStream is = new FileInputStream(jsonfile);
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        List<User> user = readUserArray(reader);
        user.forEach((u) -> {
            System.out.println(u.getLast_name() + " " + u.getFirst_name());
        });
    }

    /**
     *Get the list of all objects User that are "stored" in JSON file
     * @param reader
     * @return
     * @throws IOException
     */
    public static List<User> readUserArray(JsonReader reader) throws IOException {
        List<User> users = new ArrayList<>();
        
        reader.beginArray();
        while (reader.hasNext()) {
            users.add(readUser(reader));
        }
        reader.endArray();
        return users;
    }

    /**
     * Read object user from JSON file
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public static User readUser(JsonReader reader) throws IOException {
        //Set default value for user's field in case its not specified in JSON file.
        int id = -1;
        String first_name = null;
        String last_name = null;
        String email = null;
        String gender = null;
        String ip_address = null;

        //Start to read the JSON object, in this case, the User object
        reader.beginObject();
        
        //Loop ends when the "reader cursor" reach the end of the JSON object, and there is no name/value pair to "read"
        //In this case, the "reader cursor" treats ONE name/value pair as ONE object to "read"
        while (reader.hasNext()) {
            
            //This line of code "tell" the "reader cursor" to enter the name/value pair and point exactly at the name part in the name/value pair
            //After this line of code, the "reader cursor" treats ONE name or ONE value as ONE object to "read"
            String name = reader.nextName();
            
            //According to the official documentation of GSON, JsonToken is an enum that specified  a structure, name or value type in a JSON-encoded string.
            //In this case, we use JsonToken to specified the value part in the name/value pair.
            //The reader.peak() call will return the type of the value goes with the name part above without cosuming it.
            //Means that the reader.peak() call tells us the value without moving the "reader cursor" to the value part
            JsonToken token = reader.peek();
            
            //In case the value is null, the "reader cursor" will go through it, and point to the next name/value pair - if it's exist, of course
            //If the value is null, and the name is one of the cases below - in this case, the user's field -  then it will be set to the default value,  
            //as specified in the very first lines of this function
            //Otherwise, access the switch-case block
            if (token == JsonToken.NULL) {
                reader.skipValue();
            } else {
                //If the name is not in any case below, means that it's not a field of User Class, simply omit it by calling reader.skipValue() in the default case
                //Otherwise, assign the value to the user's field
                switch (name) {
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "first_name":
                        first_name = reader.nextString();
                        break;
                    case "last_name":
                        last_name = reader.nextString();
                        break;
                    case "email":
                        email = reader.nextString();
                        break;
                    case "gender":
                        gender = reader.nextString();
                        break;
                    case "ip_address":
                        ip_address = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        }
        
        //Consumes the next token from the JSON stream and asserts that it is the end of the current object.
        //In this case, the "reader cursor" treats ONE name/value pair as ONE object to "read"
        reader.endObject();
        return new User(id, first_name, last_name, email, gender, ip_address);
    }
}

