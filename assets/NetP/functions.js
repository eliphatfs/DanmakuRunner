var TDanmaku = Bmob.Object.extend("Danmaku");
var TProject = Bmob.Object.extend("Project");

function heredoc(fn) {
    return fn.toString().split('\n').slice(1,-1).join('\n') + '\n';
}

function signup(name, pwd)
{
	var user = new Bmob.User();
	user.set("username", name);
	user.set("password", pwd);

	user.signUp(null, {
		success: function(user) {
			Materialize.toast('注册成功！', 2000);
			currentUser = user;
		},
		error: function(user, error) {
			alert("Error: " + error.code + " " + error.message);
		}
	});
}

function login(name, pwd)
{
	Bmob.User.logIn(name, pwd, {
		success: function(user) {
			Materialize.toast('登录成功！', 2000);
			currentUser = user;
		},
		error: function(user, error) {
			alert("Error: " + error.code + " " + error.message);
		}
	});
}

var currentUser = Bmob.User.current().get("username");
/*var query = new Bmob.Query(TDanmaku);
query.find({
  success: function(results) {
    for (var i = 0; i < results.length; i++) {
      var object = results[i];
      alert(object.id + ' - ' + object.get('Name'));
    }
  },
  error: function(error) {
    alert("查询失败: " + error.code + " " + error.message);
  }
});*/