package erp.entity;

import erp.dao.BaseDao;

public enum BilesenTip {
    HAMMADDE(1,"Hammadde","1", BaseDao.BILESEN),
    YARIMAMUL(2,"Yarımamül","2", BaseDao.BILESEN),
    MAMUL(3,"Mamül","3", BaseDao.MAMUL),
    HAMMADDEYARIMAMUL(4,"Hammadde-Yarımamül","1,2", BaseDao.BILESEN);

    private Integer value;
    private String ad;
    private String id;
    private String tablo;
    
    BilesenTip(Integer value, String ad, String id, String tablo) {
        this.value = value;
        this.ad = ad;
        this.id = id;
        this.tablo = tablo;
    }
    
    public Integer value() {
        return value;
    }
    
    public String ad() {
        return ad;
    }
    public String id() {
        return id;
    }
    public String tablo() {
        return tablo;
    }
}