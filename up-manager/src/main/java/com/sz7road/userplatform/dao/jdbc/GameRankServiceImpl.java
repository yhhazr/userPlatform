package com.sz7road.userplatform.dao.jdbc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.DataObject;
import com.sz7road.userplatform.pojo.RankObject;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.userplatform.service.outinterface.IGameRankService;
import com.sz7road.utils.ConfigurationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 上午9:50
 * 接口实现类
 */
@Singleton
public class GameRankServiceImpl implements IGameRankService {

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;

    private static final Logger log = LoggerFactory.getLogger(GameRankServiceImpl.class);

    private static final int SERVER_HOT_STATUS=1;

    private static final int SERVER_MAINTAIN_STATUS=-2;


    /**
     * 根据传入的游戏ID，得到一个json数据，
     * json数据的格式是｛“规则”，rule,"data",[{"gameId",gameId,"url",url}...]｝
     *
     * @param gameId
     * @return
     */
    @Override
    public String getGameRankInfo(int gameId) {
        //固定的key
        final String roleKey = "sq.role.rankUrl";
        String returnData = null;
        if (gameId <= 0) {
            throw new IllegalArgumentException("gameId 参数不合法！");
        }
        RankObject rankObject = new RankObject();

        //1,从系统变量表中拿到规则
        String role = null;
        try {
            role = ConfigurationUtils.get(roleKey);
        } catch (Exception e) {
            log.error("从系统变量表中拿排行数据异常！");
        }
        //2,从服务区变量表中拿到url数据
        GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
        Collection<ServerTable.ServerEntry> serverEntryCollection = gameWithServerDecorateService.getGameTable().getServerTable(gameId).values();

        List<ServerTable.ServerEntry> serverEntryList = new ArrayList<ServerTable.ServerEntry>();
        for (ServerTable.ServerEntry serverEntry : serverEntryCollection)
        {  //增加一层过滤，只拿正在开启或者正在维护的服务器
            int serverStatus=serverEntry.getServerStatus();
             if(serverStatus==SERVER_HOT_STATUS||serverStatus==SERVER_MAINTAIN_STATUS)
             {
                 serverEntryList.add(serverEntry);
             }
        }
        //对集合进行排序
        Collections.sort(serverEntryList, new Comparator<ServerTable.ServerEntry>() {
            @Override
            public int compare(ServerTable.ServerEntry o1, ServerTable.ServerEntry o2) {
                int rel = 0;
                if (o1.getServerNo() > o2.getServerNo()) rel = 1;
                if (o1.getServerNo() < o2.getServerNo()) rel = -1;
                return rel;
            }
        });
        List<DataObject> data = new ArrayList<DataObject>();
        for (int i = 0; i < serverEntryList.size(); i++) {
            DataObject dataObject = new DataObject();
            ServerTable.ServerEntry serverEntry = serverEntryList.get(i);
            //先把id和serverName设置进去
            dataObject.setId(serverEntry.getId());
            dataObject.setServerName(serverEntry.getServerName());
            Map<String, String> map = serverEntry.getVariables();
            String url = null;
            if (map != null && !map.isEmpty()) url = map.get(roleKey);
            if (url != null && url.trim().length() > 0) {
                dataObject.setUrl(url);
            } else {
                dataObject.setUrl(role.replace("%d", String.valueOf(i + 1)));
            }
            data.add(dataObject);
        }
        rankObject.setTotal(data.size());
        rankObject.setData(data);
        //3,转换成json数据返回
        ObjectMapper mapper = new ObjectMapper();
        try {
            returnData = mapper.writeValueAsString(rankObject);
        } catch (IOException e) {
            log.error("转换成json数据出错！");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return returnData;
    }
}
