package com.sz7road.web.masterdata;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class MasterDataMessageDecoder extends MessageDecoderAdapter {

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        if (!hasSize(in)) return MessageDecoderResult.NEED_DATA;
        in.order(ByteOrder.LITTLE_ENDIAN);
        final int pos = in.position();
        final int length = getSize(in);
        in.position(pos);

        if (!(in.hasRemaining() && in.remaining() >= length)) {
            return MessageDecoderResult.NEED_DATA;
        }
        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        int size = getSize(in);
        try {
            //很重要
            IoBuffer buff = in.slice().order(in.order()).limit(size);
            out.write(buff);
        } catch (Exception e) {
            session.close(false);
        } finally {
            in.position(size);
        }
        return MessageDecoderResult.OK;
    }

    private int getSize(final IoBuffer in) {
        int size = in.getInt();
        in.position(0);
        return size;
    }

    private boolean hasSize(final IoBuffer in) {
        return in.remaining() >= 4;
    }

}
