package com.lesincs.simpleread.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;



/**
 * Created by Administrator on 2018/4/8.
 */
@Entity
public class DBZHNews {/*知乎日报收藏的数据类,因为GreenDao对kt支持还不太完善,故此处用Java编写*/
    @Id(autoincrement = true)
    private Long id;
    private String newsId; /*每个新闻的id*/
    private Boolean isCollected;/*是否收藏*/
    private Boolean isRead;/*是否阅读*/
    private String newsTitle;/*新闻标题*/
    private String imageUrl;/*新闻关联图片*/
    private Long collectTime;/*收藏时间*/
    @Generated(hash = 900788722)
    public DBZHNews(Long id, String newsId, Boolean isCollected, Boolean isRead,
            String newsTitle, String imageUrl, Long collectTime) {
        this.id = id;
        this.newsId = newsId;
        this.isCollected = isCollected;
        this.isRead = isRead;
        this.newsTitle = newsTitle;
        this.imageUrl = imageUrl;
        this.collectTime = collectTime;
    }
    @Generated(hash = 1817701210)
    public DBZHNews() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNewsId() {
        return this.newsId;
    }
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
    public Boolean getIsCollected() {
        return this.isCollected;
    }
    public void setIsCollected(Boolean isCollected) {
        this.isCollected = isCollected;
    }
    public Boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    public String getNewsTitle() {
        return this.newsTitle;
    }
    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Long getCollectTime() {
        return this.collectTime;
    }
    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }
   
}
