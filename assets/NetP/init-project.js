var tb = document.getElementById("contentTable");
var con = "";

var format = heredoc(function(){/*
	<tr>
		<td>$_(Name)</td>
		<td>$_(Author)</td>
		<td>$_(Control)</td>
	</tr>
*/});

var delButton = heredoc(function(){/*
	<button class="btn waves-effect waves-light" onclick="del('$_(ObjectID)')">删除</button>
*/});
var downButton = heredoc(function(){/*
	<button class="btn waves-effect waves-light" onclick="download('$_(ObjectID)')">下载</button>
*/});

var del = function(id)
{
	var dQ = new Bmob.Query(TProject);
	Materialize.toast("删除中……", 2000);
	dQ.get(id, {
		success: function(object) {
			// The object was retrieved successfully.
			if (Bmob.User.current().get("username") == object.get("Author"))
				object.destroy({
					success: function(deleteObject) {
						Materialize.toast("delete success\r\n删除成功", 2000);
						init();
					},
					error: function(GameScoretest, error) {
						Materialize.toast("delete fail\r\n删除失败：" + error.code + " " + error.message, 2000);
					}
				});
		},
		error: function(object, error) {
			alert("delete fail\r\n删除失败：" + error.code + " " + error.message);
		}
	});
};

var download = function(id)
{
	Materialize.toast("下载中……", 2000);
	var dQ = new Bmob.Query(TProject);
	dQ.get(id, {
		success: function(object) {
			var filenames = object.get("Filenames");
			var files = object.get("Files");
			var name = object.get("Name");
			for (var i=0; i<filenames.length; i++)
			{
				bridge.write(files[i], name + "/" + filenames[i]);
			}
			object.increment("Downloads");
			object.save();
			Materialize.toast("下载成功！", 2000);
		},
		error: function(object, error) {
			alert("download fail\r\n获取失败：" + error.code + " " + error.message);
		}
	});
};

var init = function()
{
	document.getElementById("preloader").className = "preloader-wrapper big active";
	var query = new Bmob.Query(TProject);
	query.select("Author", "Name", "objectId");
	query.find({
		success: function(results) {
			con = "";
			for (var i = 0; i < results.length; i++) {
				var object = results[i];
				var down = downButton.replace("$_(ObjectID)", object.id);
				var control = down;
				if (currentUser == object.get("Author"))
					control += delButton.replace("$_(ObjectID)", object.id);
				var toApp = format.replace("$_(Name)", object.get("Name")).replace("$_(Author)", object.get("Author")).replace("$_(Control)", control);
				con += toApp;
			}
			tb.innerHTML = con;
			document.getElementById("preloader").className = "preloader-wrapper big";
		},
		error: function(error) {
			alert("查询失败: " + error.code + " " + error.message);
		}
	});
};
init();