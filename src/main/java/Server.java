import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private final int PORT;
    private final BooleanSearchEngine ENGINE;

    public Server(int port, BooleanSearchEngine engine) {
        this.PORT = port;
        this.ENGINE = engine;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    String word = in.readLine();
                    List<PageEntry> pageEntryList;
                    pageEntryList = ENGINE.search(word);

                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();

                    String jsonPageEntryList = gson.toJson(pageEntryList, new TypeToken<List<PageEntry>>() {
                    }.getType());

                    out.println(jsonPageEntryList);
                }
            }
        } catch (IOException | java.io.IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}