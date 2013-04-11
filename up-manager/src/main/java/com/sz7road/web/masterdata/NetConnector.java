package com.sz7road.web.masterdata;

import com.sz7road.utils.ConfigurationUtils;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午4:06
 * 连接主数据的客户端
 */
public class NetConnector {
    //    private final static String host = Configuration.getValue("host");
//    private final static int port = Integer.parseInt(Configuration.getValue("port"));
    private final static Logger log = LoggerFactory.getLogger(NetConnector.class);

    private NioSocketConnector connector;
    private ConnectFuture futrue;
    public static NetConnector INSTANCE = new NetConnector();
    private InetSocketAddress address;
    private IoSession session = null;

    private NetConnector() {
        init();
    }

    public static NetConnector getInstance() {
        if (INSTANCE != null) {
            INSTANCE = new NetConnector();
//            log.info("新建一个实例");
        }
        return INSTANCE;
    }


    public void init() {
        try {
            if (connector != null) {
                connector.dispose();
            }
            connector = new NioSocketConnector();
            connector.setConnectTimeoutMillis(30000L);
            DefaultIoFilterChainBuilder chain = connector.getFilterChain();
            DemuxingProtocolCodecFactory factory = new DemuxingProtocolCodecFactory();
            factory.addMessageDecoder(MasterDataMessageDecoder.class);
            chain.addLast("DEMUX", new ProtocolCodecFilter(factory));
            //从系统变量表中拿到数据
            String masterDataIpAndPort = ConfigurationUtils.get("masterDataIpAndPort");
            String[] content = masterDataIpAndPort.split(":");
            address = new InetSocketAddress(content[0], Integer.parseInt(content[1]));
            SocketSessionConfig cfg = connector.getSessionConfig();
            cfg.setUseReadOperation(true);
            if (session != null && session.isClosing() == false) {
                session.close(true);
                log.info("已经关闭连接！");
            }
            session = connector.connect(address).awaitUninterruptibly().getSession();


        } catch (Exception ex) {
            log.error("建立连接失败!");
            ex.printStackTrace();
        }
    }


    public void request(final Command command) {
//        futrue.addListener(new IoFutureListener<IoFuture>() {
//            @Override
//            public void operationComplete(IoFuture future) {
//                IoSession session = futrue.getSession();
        try {
//            log.info("连接已经建立：" + session.isConnected());
            command.writePacket(session);

            ReadFuture future = session.read().awaitUninterruptibly();

            command.readPacket(session, future.getMessage());
        } catch (Exception ex) {
            log.error("主数据通信异常!");
            ex.printStackTrace();
        } finally {
            // 拿到数据之后，断开，释放资源
//                session.close(false);
//                session.getService().dispose();
        }
    }

    public void request(String ip, int port, IoFutureListener<IoFuture> listener) {
        futrue = connector.connect(new InetSocketAddress(ip, port));
        futrue.addListener(listener);
    }

    public void setCommand(Command command) {
        request(command);
    }
}
