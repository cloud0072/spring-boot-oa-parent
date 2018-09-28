$('.btn-logout').on('click', function () {
    location.href = hostUrl + "logout";
});

$('.btn.view').on('click', function () {
    var id = $(this).attr("entity_id");
    location.href = baseUrl + '/view/' + id;
});

$('.btn.create').on('click', function () {
    location.href = baseUrl + '/create';
});

$('.btn.update').on('click', function () {
    var id = $(this).attr("entity_id");
    location.href = baseUrl + '/update/' + id;
});

$('.btn.save').on('click', function () {
    var form = $("#main_form");
    var id = $(this).attr("entity_id");
    var method = id ? 'PUT' : 'POST';
    var url = id ? (baseUrl + "/" + id) : baseUrl;

    if (typeof validateAll == 'function' && !validateAll()) {
        bootbox.alert({
            title: "保存失败",
            message: "请填写正确的信息后重试!",
            size: 'small',
            buttons: {
                ok: {
                    label: "确认",
                    className: "btn-info"
                }
            }
        });
    } else {
        $.ajax({
            url: url,
            type: method,
            data: form.serialize(),
            success: function (data) {
                if (data && data.url) {
                    // console.log(data);
                    location.href = hostUrl + data.url;
                }
            },
            error: function (data) {
                console.log(typeof validateAll);
            }
        });
    }
});

$('.btn.remove').on('click', function () {
    var id = $(this).attr("entity_id");
    $.ajax({
        url: baseUrl + "/" + id,
        type: "DELETE",
        success: function (data) {
            // console.log(data);
            if (data && data.url) {
                location.href = hostUrl + data.url;
            }
        }
    });
});