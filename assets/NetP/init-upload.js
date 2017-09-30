var tb = document.getElementById("contentTable");
var con = "";

var format = heredoc(function(){/*
	<tr>
		<td>$_(Name)</td>
		<td>$_(Control)</td>
	</tr>
*/});

var upButton = heredoc(function(){/*
	<button class="btn waves-effect waves-light" onclick="upload('$_(ObjectID)')">上传！</button>
*/});

var upload = function(id)
{
	var type = items[id].split(" [工程]").length;
	if (!currentUser) {alert("请先登录！"); return;}
	Materialize.toast("上传中……", 2000);
	if (type == 1)
	{
		var dmk = new TDanmaku();
		dmk.set("Name", items[id]);
		dmk.set("Author", currentUser);
		dmk.set("Code_Base64", bridge.getDanmaku(items[id]));
		dmk.set("Downloads", 0);
		dmk.save(null, {
      success: function(object) {
        Materialize.toast("上传成功：" + object.get("Name"));
      },
      error: function(model, error) {
        alert("上传失败：" + error.code + " " + error.message);
      }
    });
	}
	else
	{
		var realname = items[id].split(" [工程]")[0];
		var filelist = bridge.getProjectDataList(realname).split("\n");
		var pro = new TProject();
		var filename = [];
		var file = [];
		pro.set("Name", realname);
		pro.set("Author", currentUser);
		pro.set("Downloads", 0);
		for (var i=0; i<filelist.length-1; i++)
		{
			filename.push(filelist[i]);
			file.push(bridge.getDanmaku(realname + "/" + filelist[i]));
		}
		pro.set("Filenames", filename);
		pro.set("Files", file);
		pro.save(null, {
      success: function(object) {
        Materialize.toast("上传成功：" + object.get("Name"));
      },
      error: function(model, error) {
        alert("上传失败：" + error.code + " " + error.message);
      }
    });
	}
};

var items;

var init = function()
{
	document.getElementById("preloader").className = "preloader-wrapper big active";
	con = "";
	items = bridge.getList().split("\n");
	for (var i = 0; i < items.length-1; i++) {
		var up = upButton.replace("$_(ObjectID)", i);
		var control = up;
		var toApp = format.replace("$_(Name)", items[i]).replace("$_(Control)", control);
		con += toApp;
	}
	tb.innerHTML = con;
	document.getElementById("preloader").className = "preloader-wrapper big";
};
init();