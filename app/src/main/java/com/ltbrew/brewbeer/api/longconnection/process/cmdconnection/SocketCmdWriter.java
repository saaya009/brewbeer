package com.ltbrew.brewbeer.api.longconnection.process.cmdconnection;


import com.ltbrew.brewbeer.api.longconnection.process.ParsePackKits;
import com.ltbrew.brewbeer.api.longconnection.process.BaseSocketWriter;

import java.io.IOException;

/**
 * Created by Jason on 2015/6/13.
 * 定义cmd的写操作
 */
public class SocketCmdWriter extends BaseSocketWriter {

    private String ackSeqNo;
    private String endQueueNo;
    private Long pack_id = -1l;
    private String token;


    @Override
    public void sendCmdReqPacks() {
        while (excute) {
            try {
//                if (cmdType == CmdsConstant.CMDSTR.idle) {
//                    //实时 关注pushservice是不是关掉了
////                    System.out.println("空转好吗？？？？？？");
//                    continue;
//                }
                writeCmdPacks();
                System.out.println("sendCmdReqPacks lock");
                synchronized (locker) {
                    locker.wait();
                }
                System.out.println("sendCmdReqPacks unlocked");
            } catch (IOException e) {
                socketWriteCb.onOutputStreamIOError();
                e.printStackTrace();
            } catch (InterruptedException e) {
                socketWriteCb.onWriteInerrupt();
                e.printStackTrace();
            }
        }
    }

    private void writeCmdPacks() throws IOException, InterruptedException {
        switch (cmdType) {
            case auth:
                writeAuthorizePack();
                cmdType = CmdsConstant.CMDSTR.st;
                break;
            case st:
                writeSt();
                cmdType = CmdsConstant.CMDSTR.hb;
                break;
            case hb:
                writeHeartbeat();
                cmdType = CmdsConstant.CMDSTR.idle;
                break;
            case sendPok:
                writePok();
                cmdType = CmdsConstant.CMDSTR.idle;
                break;

        }
    }

    @Override
    public void writeAuthorizePack() throws IOException {
        super.writeAuthorizePack();
    }

    @Override
    public void writeSt() throws IOException {
        super.writeSt();

    }


    private void writeProbeMss() {
        int reduceCount = 0;
        do {
            locker.seqNo++;
            try {
                outputStream.write(toByteArr(ParsePackKits.makePack(Cmds.buildPockCmd(locker.seqNo, ackSeqNo, endQueueNo, this.pushServiceKits))));
                break;
            } catch (IOException e) {
                e.printStackTrace();
                reduceCount++;
            }
        } while (true);

        if (socketWriteCb != null) {
            socketWriteCb.onMaximumFileLen(CmdsConstant.MSSMAX - reduceCount * CmdsConstant.DECREASERATE);
        }
    }

    @Override
    public void writeHeartbeat() {
        super.writeHeartbeat();
    }

    private void writePok() {
        locker.seqNo++;
        try {
            String requestStr = Cmds.buildPockCmd(locker.seqNo, ackSeqNo, endQueueNo, this.pushServiceKits);
            outputStream.write(toByteArr(ParsePackKits.makePack(requestStr)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //******************************************************** 外部控制修改命令******************************************//
    //外部修改命令类型， 内部更加命令类型执行命令
    @Override
    public void changeCmdToSendPok(String ackSeqNo, String endQueueNo) throws IOException, InterruptedException {
        super.changeCmdToSendPok(ackSeqNo, endQueueNo);
        cmdType = CmdsConstant.CMDSTR.sendPok;
        this.ackSeqNo = ackSeqNo;
        this.endQueueNo = endQueueNo;
        synchronized (locker){
            locker.notifyAll();
        }
    }

    public byte[] toByteArr(String str) {
        return ParsePackKits.charToByteArray(str.toCharArray());
    }


}
