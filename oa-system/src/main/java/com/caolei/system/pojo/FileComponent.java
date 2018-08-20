package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.constant.FileType;
import com.caolei.common.util.DateUtils;
import com.caolei.common.util.FileUtils;
import com.caolei.common.util.HttpUtils;
import com.caolei.common.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.FileCopyUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 文件上传和储存组件
 *
 * @author caolei
 */
@Entity
@Table
public class FileComponent extends BaseEntity {
    /**
     * 文件名
     */
    @Column
    private String fileName;
    /**
     * 本地储存的uuid名称
     */
    @Column(name = "UUID_NAME")
    private String UUIDName;
    /**
     * 文件扩展名
     */
    @Column
    private String extendName;
    /**
     * 时间路径
     */
    @Column
    private String datePath;
    /**
     * 文件分类
     * 哪个Entity中的文件
     */
    @Column
    private FileType category;
    /**
     * 下载次数
     */
    @Column
    private Integer downloadTimes;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date modifyTime;
    /**
     * 创建人
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    /**
     * 修改人
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id")
    private User modifier;

    public FileComponent() {
    }

    /**
     * 更新文件组件信息
     *
     * @param fileName
     * @param category
     */
    private void createOrUpdate(String fileName, FileType category, User user) {
        this.fileName = fileName;
        this.category = category;

        Date date = new Date();
        this.datePath = DateUtils.datePathFormat(date);
        this.UUIDName = StringUtils.UUID32();
        this.extendName = StringUtils.extendName(fileName);
        this.downloadTimes = 0;
        if (this.createTime == null) {
            this.createTime = date;
        }
        this.modifyTime = date;
        if (this.creator == null) {
            this.creator = user;
        }
        this.modifier = user;
    }

    /**
     * 将保存文件组件实体和复制文件分开
     *
     * @param file
     * @throws IOException
     */
    private void copyFile(File file) throws IOException {
        if (StringUtils.isEmpty(getId())) {
            throw new UnsupportedOperationException("请先保存文件组件才能复制文件!");
        }
        File target = new File(getAbsolutePath());
        if (target.createNewFile()) {
            FileCopyUtils.copy(file, target);
        } else {
            throw new IOException("创建文件失败!");
        }
    }

    /**
     * 如果有则删除原有文件
     */
    private void deleteFile() {
        //没有保存过 直接返回;
        if (StringUtils.isEmpty(getId())) {
            return;
        }
        File file = new File(getAbsolutePath());
        if (file.exists()) {
            logger().info("正在删除文件...类型:{},\tID:{}", getCategory(), getId());
            if (!file.delete()) {
                throw new UnsupportedOperationException("原有文件无法删除!");
            }
        } else {
//            throw new UnsupportedOperationException("原文件已被移动或删除!");
        }
    }

    /**
     * 更新文件信息并拷贝文件到指定路径
     *
     * @param fileName
     * @param category
     */
    public void createOrUpdateFile(String fileName, FileType category, User user) {
        deleteFile();
        createOrUpdate(fileName, category, user);
    }

    /**
     * 获取文件的绝对路径
     * @return
     */
    public String getAbsolutePath() {
        return FileUtils.uploadPath() + getPath();
    }

    /**
     * 获取文件的相对路径
     * @return
     */
    public String getPath() {
        if (StringUtils.isEmpty(this.datePath) || this.category.name() == null || this.UUIDName == null) {
            throw new UnsupportedOperationException("无法获取文件路径信息!");
        }
        String path = "/" + this.category.name() + "/" + this.datePath + "/" + this.UUIDName;
        if (!StringUtils.isEmpty(extendName)) {
            path += "." + extendName;
        }
        return path;
    }

    /**
     * 获取文件
     *
     * @return
     */
    public File getFile() {
        File file = new File(getAbsolutePath());
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new UnsupportedOperationException("创建文件失败!");
            }
        }
        return file;
    }

    /**
     * 是否是空文件
     *
     * @return
     */
    public Boolean isEmptyFile() {
        File file = new File(getAbsolutePath());
        if (!file.exists()) {
            throw new UnsupportedOperationException("文件已被移动或删除!");
        } else {
            return file.length() == 0;
        }
    }

    /**
     * 获取访问路径
     * @return
     */
    public String getUrl() {
        if (StringUtils.isEmpty(getId())) {
            throw new UnsupportedOperationException("请先保存文件组件才能获取访问路径!");
        }

        return HttpUtils.getServerAddress() + "/static" + getPath();
    }

    @Override
    protected String getTableName() {
        return "文件组件";
    }

    @Override
    protected String getModuleName() {
        return "system";
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUUIDName() {
        return UUIDName;
    }

    public void setUUIDName(String UUIDName) {
        this.UUIDName = UUIDName;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public String getDatePath() {
        return datePath;
    }

    public void setDatePath(String datePath) {
        this.datePath = datePath;
    }

    public FileType getCategory() {
        return category;
    }

    public void setCategory(FileType category) {
        this.category = category;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

}
