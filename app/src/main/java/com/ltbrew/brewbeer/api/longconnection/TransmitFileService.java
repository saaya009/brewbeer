package com.ltbrew.brewbeer.api.longconnection;

import com.ltbrew.brewbeer.api.longconnection.interfaces.FileSocketReadyCallback;
import com.ltbrew.brewbeer.api.longconnection.process.CommonParam;
import com.ltbrew.brewbeer.api.longconnection.process.ReqType;
import com.ltbrew.brewbeer.api.model.UploadParam;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Jason on 2015/6/9.
 */
public class TransmitFileService {

    private final String authorizeToken;
    private final FileSocketReadyCallback fileSocketCb;
    private InetAddress fileServerAddress;
    private Socket fileSocket;
    private CommonParam fileSocketLocker = new CommonParam();
    private OutputStream fileOutputStream;
    private final ExecutorService pool;
    private String ip;
    private int host;
    private SocketWriteProxy cmdsWrite;
    public int fileBeginSeqNo;
    private Object hbLocker = new Object();
    private SocketRead cmdRead;

    public TransmitFileService(String authorizeToken, ExecutorService pool, FileSocketReadyCallback fileSocketReadyCallback) {
        this.authorizeToken = authorizeToken;
        this.pool = pool;
        this.fileSocketCb = fileSocketReadyCallback;
    }

    public boolean initializeFileLongConn(String ip, int host) {
        try {
            initFileSocket(ip, host);
            startFileWriteRead(fileSocket, fileOutputStream, fileSocketLocker);
        } catch (IOException e) {

            fileSocketCb.onInitFileSocketFailed();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initFileSocket(String ip, int host) throws IOException {
        fileServerAddress = InetAddress.getByName(ip);
        fileSocket = new Socket(fileServerAddress, host);
        fileOutputStream = fileSocket.getOutputStream();
    }

    private void startFileWriteRead(Socket socket, OutputStream outputStream, CommonParam locker) {
        cmdsWrite = new SocketWriteProxy(outputStream, this.authorizeToken, ReqType.file);
        cmdsWrite.setLocker(locker);
        cmdsWrite.sethbLocker(hbLocker);
        cmdRead = newCmdRead(socket);
        cmdRead.setLocker(locker);
        cmdRead.sethbLocker(hbLocker);
        if(pool.isShutdown()){
            return;
        }
        pool.execute(cmdsWrite);
        pool.execute(cmdRead);
    }


    private SocketRead newCmdRead(Socket socket) {
        SocketRead cmdRead = new SocketRead(socket, new SocketReackCallback() {

            @Override
            public void onIPHostReceived(String[] ips) {
                boolean initFileSocket = false;
                for (int ipScount = 0; ipScount <= ips.length - 1; ipScount++) {
                    String ip = ips[ipScount];
                    String[] splitedIp = ip.split(":");
                    String ipaddr = splitedIp[0];
                    String host = splitedIp[1];
                    TransmitFileService.this.ip = ipaddr;
                    TransmitFileService.this.host = Integer.parseInt(host);
                }
            }

            @Override
            public void onReconnect(String command) {
                fileSocketCb.onFileSocketReconnect();
                closeFileWriteSocketConnection();
                initializeFileLongConn(TransmitFileService.this.ip, TransmitFileService.this.host);
            }

            @Override
            public void hasPush(List<String> msgBack, String ackSeqNo, String endQueueNo, long times) {

            }

            @Override
            public void onReady() {
                fileSocketCb.onFileSocketReady();
            }

            @Override
            public void hasHeartRr(ArrayList<Integer> result, String r_hrr_endtime, String linkedIndex, String endindex) {
                fileSocketCb.onGetPidHeart(result, r_hrr_endtime, linkedIndex, endindex);
            }

            @Override
            public void getHeartHistory(String hrh_endIndex, HashMap<String, ArrayList<Integer>> maps) {
                fileSocketCb.onGetPidHeartHistory(hrh_endIndex,maps);
            }

            @Override
            public void onGeBrewSessionResp(String tk, String state) {
                fileSocketCb.onGeBrewSessionResp(tk, state);
                sendCmnPrgsCmd(tk);
            }

            @Override
            public void onGetCmnPrgs(String percent, String seq_index, String body) {
                fileSocketCb.onGetCmdPrgs(percent, seq_index, body);
            }

            @Override
            public void onGetCmnMsg(String des) {
                fileSocketCb.onGetCmnMsg(des);
            }

            @Override
            public void onServerRespError(String cmd) {
                fileSocketCb.onServerRespError(cmd);
            }
        });
        cmdRead.registerSocketReadCb(new FileSocketReadCallback() {
            @Override
            public void onFileUploadBegin(int seqNo) {
                fileBeginSeqNo = seqNo;
                fileSocketCb.onFileBegined();
            }

            @Override
            public void onFileUploadSuccess() {
                fileSocketCb.onFileUploadSuccess();
            }

            @Override
            public void onFileUploadFailed() {
                fileSocketCb.onFileUploadFailed();
            }

            @Override
            public void onFileUploadEnd() {
                fileSocketCb.onFileUploadEnd();
            }

        });
        return cmdRead;
    }

    public void sendCmnPrgsCmd(String token){
        if(cmdsWrite != null){
            cmdsWrite.sendCmnPrgsCmd(token);
        }
    }

    public void closeFileSocketConnection() {
        if(cmdRead != null)
            cmdRead.endReadThread(false);
        closeFileWriteSocketConnection();
    }

    private void closeFileWriteSocketConnection() {
        if(cmdsWrite != null)
            cmdsWrite.setExcute(false);
        fileSocketLocker.seqNo = 0;
        synchronized (fileSocketLocker) {
            fileSocketLocker.notifyAll();
        }
        try {
            if(fileOutputStream != null)
                fileOutputStream.close();
            if(fileSocket != null)
                fileSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isfileSocketClose() {
        if (fileSocket == null || (fileSocket != null && fileSocket.isClosed()))
            return true;
        else
            return false;
    }

    public boolean isFileSocketAvailable() {
        if (fileSocket == null || cmdRead == null)
            return false;
        return true;
    }

    void beginFileUpload(UploadParam uploadParam) {
        cmdsWrite.setUploadParam(uploadParam);
    }

    public void fileUpload(byte[] fileParts) {
        cmdsWrite.setFileParts(fileParts);
    }

    public void endFileUpload() {
        cmdsWrite.setFileEnd();
    }

    public void getPidHeartRr(String id, String bTime, int linkIndex, int testBindex) throws IOException {

        cmdsWrite.sendHeartRr(id,bTime,linkIndex,testBindex);
    }

    public void getPidHearHistory(String id, int endIndex, int whats6day, boolean isZip) throws IOException {
        if(cmdsWrite != null)
            cmdsWrite.senHeartHistory(id,endIndex, whats6day, isZip);
    }

    public void sendBrewSessionCmd(Long package_id) {
        if(cmdsWrite != null)
            cmdsWrite.sendBrewSessionCmd(package_id);
    }

    public void checkCmnMsgLast(String pid, String token){
        if(cmdsWrite != null)
            cmdsWrite.checkLastCmnMsg(pid, token);
    }

    interface SocketReackCallback extends TransmitCmdService.SocketReadCallback{

        void onGeBrewSessionResp(String tk, String state);

        void onGetCmnPrgs(String percent, String seq_index, String body);

        void onGetCmnMsg(String des);
    }

    interface FileSocketReadCallback {
        void onFileUploadBegin(int seqNo);

        void onFileUploadSuccess();

        void onFileUploadFailed();

        void onFileUploadEnd();
    }
}
