package com.github.cloud0072.base.model;

import com.github.cloud0072.base.model.extend.Creator;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.constant.FileType;
import com.github.cloud0072.common.module.BaseModuleEntity;
import com.github.cloud0072.common.util.DateUtils;
import com.github.cloud0072.common.util.FileUtils;
import com.github.cloud0072.common.util.HttpUtils;
import com.github.cloud0072.common.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * 文件上传和储存组件
 *
 * @author caolei
 */
@EntityInfo(description = "文件组件", entityName = "file", entityPath = "/file")
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Entity
@Table
public class FileComponent
        extends BaseEntity
        implements BaseModuleEntity {
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
     * 文件http传输中的类型
     */
    @Column
    private String contentType;
    /**
     * 文件分类
     * 哪个Entity中的文件
     */
    @Column
    private FileType category;
    /**
     * 时间路径
     */
    @Column
    private String datePath;
    /**
     * 下载次数
     */
    @Column
    private Integer downloadTimes;
    /**
     * 创建人时间戳等
     */
    @Embedded
    private Creator creator;

    public FileComponent() {
    }

    /**
     * 更新文件信息并拷贝文件到指定路径
     *
     * @param fileName
     * @param contentType
     * @param category
     */
    public void createOrUpdateFile(String fileName, String contentType, FileType category, User user) {
        deleteFile();

        LocalDate date = LocalDate.now();
        this.fileName = fileName;
        this.contentType = contentType;
        this.category = category;
        this.datePath = DateUtils.datePathFormat(date);
        this.UUIDName = StringUtils.UUID32();
        this.extendName = StringUtils.extendName(fileName);

        this.downloadTimes = downloadTimes == null ? Integer.valueOf(0) : downloadTimes;
        this.creator = this.creator == null ? new Creator().modifyBy(user) : creator.modifyBy(user);
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
            log.info("正在删除文件...类型:{},\tID:{}", getCategory(), getId());
            if (!file.delete()) {
                throw new UnsupportedOperationException("原有文件无法删除!");
            }
        } else {
            log.error("原文件已被移动或删除!");
//            throw new UnsupportedOperationException("原文件已被移动或删除!");
        }
    }

    /**
     * 获取文件的相对路径
     *
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
     * 获取文件的绝对路径
     *
     * @return
     */
    public String getAbsolutePath() {
        return FileUtils.getUploadPath() + getPath();
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
                if (file.getParentFile().mkdirs()) {
                    log.info("创建文件夹成功\t:\t" + file.getParentFile().getAbsolutePath());
                } else {
                    throw new UnsupportedOperationException("创建文件夹失败\t:\t" + file.getParentFile().getAbsolutePath());
                }
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
     * 获取访问路径
     *
     * @return
     */
    public String getUrl() {
        if (StringUtils.isEmpty(getId())) {
            throw new UnsupportedOperationException("请先保存文件组件才能获取访问路径!");
        }

        return HttpUtils.serverAddress() + "file/download/" + getId();
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

    @Override
    public String toString() {
        return "FileComponent{" +
                "fileName='" + fileName + '\'' +
                ", UUIDName='" + UUIDName + '\'' +
                ", extendName='" + extendName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", category=" + category +
                ", datePath='" + datePath + '\'' +
                ", downloadTimes=" + downloadTimes +
                ", creator=" + creator +
                '}';
    }
}
