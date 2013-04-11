package com.sz7road.web.masterdata;

import com.sz7road.utils.MD5Utils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */
public class LoginCommand extends AbstractCommand {

    private String userName = null;
    private String password = null;
    private static Logger log = LoggerFactory.getLogger(LoginCommand.class);
    private Map<String, Map<String, Object>> masterDataObject = null;
    CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
    CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Map<String, Map<String, Object>> getMasterDataObject() {
        return masterDataObject;
    }


    public LoginCommand() {

    }

    @Override
    public void readPacket(IoSession session, Object message) {

        IoBuffer buff = (IoBuffer) message;
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.position(16);

        try {
            buff.get();
            byte result = buff.get();
            if (result == 1) {
                int jsonLen = buff.getInt();// 字符长度
                String json = buff.getString(jsonLen, decoder);
//                log.info("成功从主数据读到数据:" + json);
                ObjectMapper objectMapper = new ObjectMapper();
                masterDataObject = objectMapper.readValue(json, Map.class);

                if (masterDataObject != null && masterDataObject.get("data") != null) {
                    log.info("用户：" + userName + " 进入系统! ");
//                    log.info(json);
                } else {
                    log.info("主数据刚刚拒绝了 " + userName + " 进入系统!原因如下：\n" + json);
                }

            } else {
                log.info("没有数据");
            }
        } catch (Exception e) {
            log.error("读取主数据代理返回的数据异常！");
            e.printStackTrace();
        }


    }


    @Override
    public void writePacket(IoSession session) {
        try {
            String strPassword = MD5Utils.digestAsHex(password);
            String ip = "127.0.0.1";
            String version = "1.0.0.0";

            int total = 0;
            byte[] userByte = null;
            byte[] passwordByte = null;
            byte[] ipByte = null;
            byte[] versionByte = null;

            try {
                userByte = userName.getBytes("utf-8");
                passwordByte = strPassword.getBytes("utf-8");
                ipByte = ip.getBytes("utf-8");
                versionByte = version.getBytes("utf-8");

            } catch (Exception e) {
                log.error(e.getMessage());
            }
            int userLen = userByte.length;
            int passwordLen = passwordByte.length;
            int ipLen = ipByte.length;
            int versionLen = versionByte.length;

            total = userLen + passwordLen + ipLen + versionLen;

            IoBuffer buff = packetHeader(total + 1 + 4 + 4 + 4 + 4, 0x10);

            buff.put((byte) 1);
            buff.putInt(userLen);
            buff.put(userByte);

            buff.putInt(passwordLen);
            buff.put(passwordByte);

            // 写过去ip 和版本号
            buff.putInt(ipLen);
            buff.put(ipByte);

            buff.putInt(versionLen);
            buff.put(versionByte);

            buff.flip();
            session.write(buff).awaitUninterruptibly();
        } catch (Exception e) {
            log.error("发送登录请求失败!" + e.getMessage());
            e.printStackTrace();
        }

    }
}
