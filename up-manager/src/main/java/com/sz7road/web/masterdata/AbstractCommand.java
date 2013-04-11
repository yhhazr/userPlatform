package com.sz7road.web.masterdata;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommand implements Command {
    private ListenerEvent event;

    /**
     * 包装发送数据包的头部
     * @param total   数据长度
     * @param tokenId  标识号
     * @return
     */
    public IoBuffer packetHeader(int total, int tokenId) {
        IoBuffer buff = IoBuffer.allocate(16 + total);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        // 数据包大小
        buff.putInt(16 + total);
        // 版本号
        buff.put((byte) 0x01);
        buff.put((byte) 0x00);
        buff.putShort((short) 0x00);
        buff.putShort((short) 0x00);

        // 命令ID
        buff.putShort((short) tokenId);

        // 随机号
        buff.putInt((int) System.currentTimeMillis());
        return buff;
    }


    @Override
    public void setUpdateHandler(ListenerEvent event) {
        this.event = event;
    }

    public ListenerEvent getUpdateHandler() {
        return this.event;
    }
}
