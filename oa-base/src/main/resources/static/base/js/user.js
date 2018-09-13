// 手机号码验证
jQuery.validator.addMethod("isMobile", function (value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

$(document).ready(function () {
    $("#main_form").validate({
        rules: {
            userName: "required",
            account: {
                required: true,
                rangelength: [6, 20]
            },
            password: {
                required: true,
                rangelength: [6, 20]
            },
            passwordCheck: {
                required: true,
                rangelength: [6, 20],
                equalTo: "#password"
            },
            phone: {
                required: true,
                isMobile: true
            }
        },
        messages: {
            userName: "请输入用户名",
            account: {
                required:"请输入账号",
                rangelength: $.validator.format("账号长度必须在 {0} 到 {1} 之间")
            },
            password: {
                required: "请输入密码",
                rangelength: $.validator.format("账号长度必须在 {0} 到 {1} 之间")
            },
            passwordCheck: {
                required: "请再次输入密码",
                rangelength: $.validator.format("账号长度必须在 {0} 到 {1} 之间"),
                equalTo: "两次输入密码不一致"
            },
            phone: {
                required: "请输入手机号",
                isMobile: "请输入正确的手机号"
            }
        },
        errorElement: "em",
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

/*重置密码*/
$('password-button').on('click', function () {
    $.ajax({
        url: baseUrl + "/resetpwd",
        type: 'PUT',
        data: {
            id: $('id').val(),
            password: $('#password').val(),
            newpassword: $('newpassword').val()
        },
        success: function (data) {
            if (data && data.message) {
                console.log(data.message);
            }
            if (data && data.url) {
                location.href = data.url;
            }
        }
    })
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