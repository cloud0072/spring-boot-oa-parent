package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.FileCategory;
import com.caolei.system.utils.DateUtils;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.utils.StringUtils;
import org.springframework.util.FileCopyUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class FileComponent extends BaseEntity {
    /**
     * 文件名
     */
    @Column
    private String fileName;
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
     * 那个Entity中的文件
     */
    @Column
    private FileCategory category;
    /**
     * 下载次数
     */
    @Column
    private Integer downloadTimes;
    /**
     * 创建时间
     */
    @Column
    private Date createTime;
    /**
     * 修改时间
     */
    @Column
    private Date modifyTime;
    /**
     * 创建人
     */
    @Column
    private User creator;
    /**
     * 关联文件
     */
    @Transient
    private File file;

    public FileComponent() {
    }

    public FileComponent(File file, String fileName, FileCategory category) {
        this.file = file;
        this.fileName = fileName;
        this.category = category;

        Date date = new Date();
        this.createTime = date;
        this.modifyTime = date;
        this.downloadTimes = 0;
        this.creator = RequestUtils.getCurrentUser();
        this.datePath = DateUtils.datePathFormat(date);
        this.extendName = StringUtils.extendName(fileName);
    }

    @Override
    public String tableName() {
        return "文件组件";
    }

    @Override
    public String moduleName() {
        return "system";
    }

    /**
     * 获取文件的绝对路径
     *
     * @return
     */
    public String getAbsolutePath() {
        if (StringUtils.isEmpty(getId())) {
            throw new UnsupportedOperationException("文件组件需要先持久化才能获取绝对路径");
        }
        if (StringUtils.isEmpty(this.datePath) || getCategory() == null) {
            throw new UnsupportedOperationException("错误的文件创建方式");
        }
        String path = File.separator + getCategory().name() + File.separator + getDatePath()
                + File.separator + getId();
        if (!StringUtils.isEmpty(extendName)) {
            path += "." + getExtendName();
        }
        return path;
    }

    public void copyFile(File file) throws IOException {
        File target = new File(getAbsolutePath());
        if (target.exists() || target.createNewFile()) {
            FileCopyUtils.copy(file, target);
        } else {
            throw new UnsupportedEncodingException("无法复制文件到指定路径:\t" + getAbsolutePath());
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
