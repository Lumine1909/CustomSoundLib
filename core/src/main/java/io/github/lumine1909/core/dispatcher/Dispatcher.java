package io.github.lumine1909.core.dispatcher;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import io.github.lumine1909.core.CustomSoundLib;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Dispatcher {
    public static File packFile;
    public static void runServer(File file) throws Exception {
        packFile = file;
        ServerSocket ss = new ServerSocket(CustomSoundLib.RESOURCE_PACK_PORT);
        CustomSoundLib.log("资源包构建完成, 正在启动分发服务器, 端口:" + CustomSoundLib.RESOURCE_PACK_PORT);
        CustomSoundLib.isDispatcherEnabled = true;
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = ss.accept();
                    new Handler(socket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}