package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.exception.AjaxException;
import com.github.cloud0072.base.model.FileComponent;
import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.service.FileComponentService;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.common.constant.FileType;
import com.github.cloud0072.common.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caolei
 * @ClassName: FileUploadController
 * @Description: TODO
 * @date 2018/8/20 10:11
 */
@Api
@RequestMapping("/file")
@Controller
public class FileController implements BaseController {

    @Autowired
    private FileComponentService fileComponentService;
    @Autowired
    private UserService userService;

    @ApiOperation("统一文件上传方法")
    @PostMapping("/{fileType}")
    @ResponseBody
    public ResponseEntity fileUpload(@NonNull @PathVariable("fileType") FileType fileType,
                                     @NonNull @RequestParam("file") MultipartFile file) {
        try {

            FileComponent component = new FileComponent();
            User user = userService.findById(UserUtils.getCurrentUser().getId());

            component.createOrUpdateFile(file.getOriginalFilename(), file.getContentType(), fileType, user);
            component = fileComponentService.save(component, null, null);
            File f = new File(component.getAbsolutePath());
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(f);

            Map<String, Object> result = new HashMap<>();
            result.put("file_id", component.getId());
            result.put("file_url", component.getUrl());
            result.put("message", "文件上传成功！");
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

    @ApiOperation("更新文件方法")
    @PutMapping("/{fileType}/{id}")
    @ResponseBody
    public ResponseEntity fileUpdate(@NonNull @PathVariable("id") String id,
                                     @NonNull @PathVariable("fileType") FileType fileType,
                                     @NonNull @RequestParam("file") MultipartFile file) {
        try {

            FileComponent fileComponent = fileComponentService.findById(id);

            if (fileComponent != null && fileComponent.getCategory().equals(fileType)) {
                User user = userService.findById(UserUtils.getCurrentUser().getId());

                fileComponent.createOrUpdateFile(file.getOriginalFilename(),
                        file.getContentType(), fileType, user);
                fileComponent = fileComponentService.save(fileComponent, null, null);
                File f = new File(fileComponent.getAbsolutePath());
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                file.transferTo(f);

                Map<String, Object> result = new HashMap<>();
                result.put("file_id", fileComponent.getId());
                result.put("file_url", fileComponent.getUrl());
                result.put("message", "文件更新成功！");
                return ResponseEntity.ok().body(result);

            } else {
                throw new UnsupportedOperationException("您的参数有误,请确认后重试");
            }

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

    @ApiOperation("文件上传的删除方法")
    @DeleteMapping("/{fileType}/{id}")
    @ResponseBody
    public ResponseEntity fileRemove(@NonNull @PathVariable("id") String id,
                                     @NonNull @PathVariable("fileType") FileType fileType) {
        try {

            FileComponent fileComponent = fileComponentService.findById(id);

            if (fileComponent != null && fileComponent.getCategory().equals(fileType)) {
                fileComponent.deleteFile();
                fileComponentService.deleteById(id);

                Map<String, Object> result = new HashMap<>();
                result.put("message", "文件删除成功！");
                return ResponseEntity.ok().body(result);

            } else {
                throw new UnsupportedOperationException("您的参数有误,请确认后重试");
            }

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

    @ApiOperation("统一文件下载")
    @GetMapping("/{fileType}/{id}")
    @ResponseBody
    public ResponseEntity fileDownload(@PathVariable("id") @NonNull String id,
                                       @NonNull @PathVariable("fileType") FileType fileType,
                                       String defaultFile) {
        try {

            FileComponent fileComponent = fileComponentService.findById(id);
            if (fileComponent != null && fileComponent.getCategory().equals(fileType)) {
                return HttpUtils.downloadFile(fileComponent.getFile(), fileComponent.getFileName(),
                        fileComponent.getContentType());
            } else {
                throw new UnsupportedOperationException("您的参数有误,请确认后重试");
            }
        } catch (Exception e) {
            //FIXME:如果获取文件失败则返回 defaultFile 的文件
            throw new AjaxException(e);
        }
    }

}
