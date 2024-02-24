package io.github.lumine1909.core.dispatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

import static io.github.lumine1909.core.CustomSoundLib.instance;

public class Handler extends Thread {
    static String prefix = ChatColor.GREEN + "[PackDispatcher] ";
    Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = this.socket.getInputStream()) {
            try (OutputStream output = this.socket.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void log(String message) {
        Bukkit.getScheduler().runTask(instance, () -> Bukkit.getConsoleSender().sendMessage(prefix + message));
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        log("接受到一个新的资源包下载请求, 正在处理");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        PrintStream printer = new PrintStream(new BufferedOutputStream(output));
        String var0 = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(var0);
        if (!(tokenizer.hasMoreElements() && tokenizer.nextToken().equalsIgnoreCase("GET") && tokenizer.hasMoreTokens())) {
            socket.close();
            return;
        }
        printer.print("HTTP/1.0 200 OK\r\nContent-Type: application/zip" + "\r\nContent-Length: " + Dispatcher.packFile.length() + "\r\n\r\n");
        byte[] buffer = new byte[4096];
        FileInputStream stream = new FileInputStream(Dispatcher.packFile);
        while (true) {
            int len = stream.read(buffer);
            if (len <= 0) {
                printer.flush();
                printer.close();
                break;
            }
            printer.write(buffer, 0, len);
        }
        stream.close();
        reader.close();
    }
}