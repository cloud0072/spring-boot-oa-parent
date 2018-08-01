package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.FileCategory;
import com.caolei.system.utils.DateUtils;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.utils.StringUtils;
import org.springframework.util.FileCopyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 文件上传和储存组件
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
     * 修改人
     */
    @Column
    private User modifier;

    public FileComponent() {
    }

    private FileComponent(String fileName, FileCategory category) {
        createOrUpdate(fileName, category);
    }

    /**
     * 拷贝文件至资源文件夹下
     * 保存文件信息
     * @param fileName
     * @param category
     * @param file
     * @return
     */
    public static FileComponent of(String fileName, FileCategory category, File file) throws IOException {
        FileComponent component = new FileComponent(fileName, category);
        component.copyFile(file);
        return component;
    }

    /**
     * 更新文件组件信息
     * @param fileName
     * @param category
     */
    private void createOrUpdate(String fileName, FileCategory category) {
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
            this.creator = RequestUtils.getCurrentUser();
        }
        this.modifier = RequestUtils.getCurrentUser();
    }

    /**
     * 将保存文件组件实体和复制文件分开
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
            throw new IOException("创建文件失败");
        }
    }

    /**
     * 如果有则删除原有文件
     */
    public void deleteFile() {
        try {
            if (StringUtils.isEmpty(getId())) {
                //没有保存 直接返回;
                return;
            }
            File file = new File(getAbsolutePath());
            if (file.exists()) {
                if (!file.delete()) {
                    throw new UnsupportedOperationException("原有文件无法删除");
                }
            } else {
                throw new UnsupportedOperationException("原文件已被移动或删除!");
            }
        } catch (UnsupportedOperationException e) {
            //转化所有异常为 操作异常
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * 更新文件信息并拷贝文件到指定路径
     * @param fileName
     * @param category
     * @param file
     * @throws IOException
     */
    public FileComponent updateFile(String fileName, FileCategory category, File file) throws IOException {
        deleteFile();
        createOrUpdate(fileName, category);
        copyFile(file);
        return this;
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
    public String tableName() {
        return "文件组件";
    }

    @Override
    public String moduleName() {
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

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

}
