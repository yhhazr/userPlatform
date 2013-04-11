package com.sz7road.userplatform.utils.IPUtil;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-13
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
//一条IP范围记录，不仅包括国家和区域，也包括起始IP和结束IP
public class IPEntry {
    public String beginIp;
    public String endIp;
    public String country;
    public String area;

    public IPEntry() {
        beginIp = "";
        endIp = "";
        country = "";
        area = "";
    }

    @Override
    public String toString() {
        return this.area + "  " + this.country + "  IP范围:" + this.beginIp + "-" + this.endIp;
    }
}




