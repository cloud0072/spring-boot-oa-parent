package com.caolei.system.controller;

import com.caolei.common.constant.FileType;
import com.caolei.common.util.HttpUtils;
import com.caolei.common.util.StringUtils;
import com.caolei.system.exception.AjaxException;
import com.caolei.system.pojo.FileComponent;
import com.caolei.system.pojo.User;
import com.caolei.system.service.FileComponentService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: FileUploadController
 * @Description: TODO
 * @author caolei
 * @date 2018/8/20 10:11
 */
@RequestMapping("/file")
@Controller
public class FileComponentController {

    @Autowired
    private FileComponentService fileComponentService;
    @Autowired
    private UserService userService;

    /**
     * @Title: 统一文件上传
     * @Description:
     * @author caolei
     * @date 2018/8/20 11:04
     * @param fileId
     * @param fileType
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(String fileId, FileType fileType,
                                     @RequestParam("file") MultipartFile file) {
        try {

            FileComponent component = StringUtils.isEmpty(fileId) ?
                    new FileComponent() : fileComponentService.findById(fileId);
            User user = userService.findById(SecurityUtils.getCurrentUser().getId());

            component.createOrUpdateFile(file.getOriginalFilename(), file.getContentType(), fileType, user);
            component = fileComponentService.save(component);
            file.transferTo(new File(component.getAbsolutePath()));

            Map<String, Object> result = new HashMap<>();
            result.put("file_id", component.getId());
            result.put("file_url", component.getUrl());
            result.put("message", "文件上传成功！");
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

    /**
     * @Title:
     * @Description:
     * @author caolei
     * @date 2018/8/20 11:07
     * 统一文件下载
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity fileDownload(String fileId) {
        try {

            FileComponent fileComponent = fileComponentService.findById(fileId);
            return HttpUtils.downloadFile(fileComponent.getFile(), fileComponent.getFileName(),
                    fileComponent.getContentType());

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

}
