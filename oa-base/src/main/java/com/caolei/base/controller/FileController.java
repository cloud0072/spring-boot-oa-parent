package com.caolei.base.controller;

import com.caolei.base.exception.AjaxException;
import com.caolei.base.model.FileComponent;
import com.caolei.base.model.User;
import com.caolei.base.service.BaseCrudService;
import com.caolei.base.service.FileComponentService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.UserUtils;
import com.caolei.common.constant.FileType;
import com.caolei.common.util.HttpUtils;
import com.caolei.common.util.StringUtils;
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
public class FileController extends BaseCrudController<FileComponent> {

    @Autowired
    private FileComponentService fileComponentService;
    @Autowired
    private UserService userService;

    @ApiOperation("统一文件上传方法")
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseEntity fileUpload(String fileId,
                                     @NonNull FileType fileType,
                                     @NonNull @RequestParam("file") MultipartFile file) {
        try {

            FileComponent component = StringUtils.isEmpty(fileId) ?
                    new FileComponent() : fileComponentService.findById(fileId);
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

    @ApiOperation("文件上传的删除方法")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity fileRemove(@PathVariable("id") @NonNull String fileId) {
        try {

            FileComponent component = fileComponentService.findById(fileId);
            component.deleteFile();
            fileComponentService.deleteById(fileId);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "文件删除成功！");
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            throw new AjaxException(e);
        }
    }

    @ApiOperation("统一文件下载")
    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity fileDownload(@PathVariable("id") @NonNull String fileId, String defaultFile) {
        try {

            FileComponent fileComponent = fileComponentService.findById(fileId);
            return HttpUtils.downloadFile(fileComponent.getFile(), fileComponent.getFileName(),
                    fileComponent.getContentType());

        } catch (Exception e) {
            //FIXME:如果获取文件失败则返回 defaultFile 的文件

            throw new AjaxException(e);
        }
    }

//    @ApiOperation("统一文件下载")
//    @GetMapping("/{id}")
//    @ResponseBody
//    @Override
//    protected ResponseEntity find(HttpServletRequest request, HttpServletResponse response, String id) {
//        return super.find(request, response, id);
//    }
//
//    @Override
//    protected ResponseEntity create(HttpServletRequest request, HttpServletResponse response, FileComponent fileComponent) {
//        return super.create(request, response, fileComponent);
//    }
//
//    @Override
//    protected ResponseEntity update(HttpServletRequest request, HttpServletResponse response, String id, FileComponent fileComponent) {
//        return super.update(request, response, id, fileComponent);
//    }
//
//    @Override
//    protected ResponseEntity delete(HttpServletRequest request, HttpServletResponse response, String id, FileComponent fileComponent) {
//        return super.delete(request, response, id, fileComponent);
//    }

    @Override
    protected BaseCrudService<FileComponent> service() {
        return fileComponentService;
    }
}
