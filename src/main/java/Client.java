import com.itextpdf.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, java.io.IOException {
        int PORT = 8989;
        String HOST = "localhost";
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out.println("бизнес");
            String prettyJson;
            do {
                prettyJson = in.readLine();
                System.out.println(prettyJson);
            } while (!prettyJson.endsWith("]"));
        }
    }
}