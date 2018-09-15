/* 如果有多个需要验证的地方统一写在这个函数中 ， 提交时统一验证此方法 */
function validateAll() {
    return validateForm.valid();
}

// 手机号码验证
jQuery.validator.addMethod("isMobile", function (value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length === 11 && mobile.test(value));
}, "请正确填写您的手机号码");

//添加验证规则
var validateForm = $("#main_form");
$(document).ready(function () {
    validateForm.validate({
        rules: {
            userName: "required",
            account: {
                required: true,
                rangelength: [5, 20]
            },
            password: {
                required: true,
                rangelength: [5, 20]
            },
            passwordCheck: {
                required: true,
                rangelength: [5, 20],
                equalTo: "#password"
            },
            phone: {
                required: true,
                isMobile: true
            },
            email: {
                required: true,
                email: true
            }
        },
        messages: {
            userName: "请输入用户名",
            account: {
                required: "请输入账号",
                rangelength: $.validator.format("账号长度必须在 {0} 到 {1} 之间")
            },
            password: {
                required: "请输入密码",
                rangelength: $.validator.format("密码长度必须在 {0} 到 {1} 之间")
            },
            passwordCheck: {
                equalTo: "两次输入密码不一致",
                required: "请再次输入密码",
                rangelength: $.validator.format("密码长度必须在 {0} 到 {1} 之间")
            },
            phone: {
                required: "请输入手机号",
                isMobile: "手机号格式不正确"
            },
            email: {
                required: "请输入邮箱地址",
                email: "邮箱地址格式不正确"
            }
        },
        // errorElement: "em",
        /* 更改错误信息显示的位置 */
        errorPlacement: function (error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                error.insertAfter(element);
            }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-success").removeClass("has-error");
        }
    });
});

$("#pic").on('click', function () {
    $("#file_id").click();
});

//同时只有一个上传任务在执行
var uploading = false;

function fileUpload() {
    var file = document.getElementById("file_id").files[0];
    if (!uploading && file) {
        uploading = true;
        var formData = new FormData();
        formData.append("file", file);
        formData.append("fileType", 'PORTRAIT');
        $.ajax({
            url: hostUrl + "/file/upload",
            type: "POST",
            data: formData,
            //必须false才会自动加上正确的Content-Type
            contentType: false,
            //必须false才会避开jQuery对 formdata 的默认处理
            //XMLHttpRequest会对 formdata 进行正确的处理
            processData: false,
            success: function (data) {
                if (data && data.message) {
                    $('#head_photo_id').val(data.file_id);
                    $('img.portrait').attr('src', data.file_url);
                    console.log(data.message);
                }
                uploading = false;
            },
            error: function () {
                alert("上传失败！");
                uploading = false;
            }
        })
    }
}