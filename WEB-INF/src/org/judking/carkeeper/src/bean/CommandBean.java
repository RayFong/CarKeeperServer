package org.judking.carkeeper.src.bean;

import java.util.List;

/**
 * Created by leilf on 2017/3/19.
 */
public class CommandBean {
    private long time;
    private List<PddDataBean> datas;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<PddDataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<PddDataBean> datas) {
        this.datas = datas;
    }
}
