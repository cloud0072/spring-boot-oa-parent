package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.constant.FileType;
import com.caolei.common.util.DateUtils;
import com.caolei.common.util.StringUtils;
import com.caolei.system.util.SecurityUtils;
import org.springframework.util.FileCopyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
     * 修改人
     */
    @Column
    private User modifier;

    public FileComponent() {
    }

    private FileComponent(String fileName, FileType category) {
        createOrUpdate(fileName, category);
    }

    /**
     * 拷贝文件至资源文件夹下
     * 保存文件信息
     *
     * @param fileName
     * @param category
     * @param file
     * @return
     */
    public static FileComponent of(String fileName, FileType category, File file) throws IOException {
        FileComponent component = new FileComponent(fileName, category);
        component.copyFile(file);
        return component;
    }

    /**
     * 更新文件组件信息
     *
     * @param fileName
     * @param category
     */
    private void createOrUpdate(String fileName, FileType category) {
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
            this.creator = SecurityUtils.getCurrentUser();
        }
        this.modifier = SecurityUtils.getCurrentUser();
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
    public void deleteFile() {
        //没有保存过 直接返回;
        if (StringUtils.isEmpty(getId())) {
            return;
        }
        File file = new File(getAbsolutePath());
        if (file.exists()) {
            if (!file.delete()) {
                throw new UnsupportedOperationException("原有文件无法删除!");
            }
        } else {
            throw new UnsupportedOperationException("原文件已被移动或删除!");
        }
    }

    /**
     * 更新文件信息并拷贝文件到指定路径
     *
     * @param fileName
     * @param category
     * @param file
     * @throws IOException
     */
    public void updateFile(String fileName, FileType category, File file) throws IOException {
        if (StringUtils.isEmpty(getId())) {
            throw new UnsupportedOperationException("请先保存文件组件才能更新文件!");
        }
        deleteFile();
        createOrUpdate(fileName, category);
        copyFile(file);
    }

    /**
     * 获取文件的绝对路径
     *
     * @return
     */
    public String getAbsolutePath() {
        if (StringUtils.isEmpty(this.datePath) || this.category.name() == null || this.UUIDName == null) {
            throw new UnsupportedOperationException("无法获取文件路径信息!");
        }
        String path = this.category.name() + File.separator + this.datePath
                + File.separator + this.UUIDName;
        if (!StringUtils.isEmpty(extendName)) {
            path += "." + getExtendName();
        }
        return path;
    }

    public File getFile() {
        File file = new File(getAbsolutePath());
        if (!file.exists()) {
            throw new UnsupportedOperationException("您要找的文件已被移除!");
        }
        return file;
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
