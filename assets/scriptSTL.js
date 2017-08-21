var command_pool_integers = [];
var command_pool_doubles = [];
var available_ids = [];
var _R2D = 57.295779513082;
for (var i=0; i<1792; i++)
	available_ids.push(i);
function Vector2(x, y)
{
	this.x = x || 0;
	this.y = y || 0;
	this.length = function() {return Math.sqrt(this.x * this.x + this.y * this.y); };
	this.nor = function()
	{
		var len = this.length();
		this.x = this.x / len;
		this.y = this.y / len;
		return this;
	};
	this.add = function(vec)
	{
		this.x = this.x + vec.x;
		this.y = this.y + vec.y;
		return this;
	};
	this.sub = function(vec)
	{
		this.x = this.x - vec.x;
		this.y = this.y - vec.y;
		return this;
	};
	this.scl = function(scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		return this;
	};
	this.set = function(x2, y2)
	{
		this.x = x2;
		this.y = y2;
		return this;
	};
	this.rotate = function(degrees)
	{
		var radians = degrees / _R2D;
		var cos = Math.cos(radians);
		var sin = Math.sin(radians);

		var newX = this.x * cos - this.y * sin;
		var newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;
		return this;
	};
	this.angle = function() {
		var ang = Math.atan2(this.y, this.x) * _R2D;
		if (ang < 0) ang += 360;
		return ang + 180;
	};
}
Vector2.add = function(v1, v2) { return new Vector2(v1.x+v2.x, v1.y+v2.y); };
Vector2.sub = function(v1, v2) { return new Vector2(v1.x-v2.x, v1.y-v2.y); };
Vector2._tmpVel = new Vector2();
Vector2._tmpVel2 = new Vector2();
function NewProj(type, posX, posY, velX, velY, scl)
{
	velY = velY||0;
	velX = velX||0;
	scl = scl||0.5;
	command_pool_integers.push(1, available_ids[0], Math.floor(type));
	command_pool_doubles.push(posX, posY, velX, velY, scl);
	return available_ids.shift();
}

function NewProjL(type, posX, posY, velX, velY, sclL, scl)
{
	velY = velY||0;
	velX = velX||0;
	scl = scl||0.5;
	sclL = sclL||1.75;
	command_pool_integers.push(2, available_ids[0], Math.floor(type));
	command_pool_doubles.push(posX, posY, velX, velY, sclL, scl);
	return available_ids.shift();
}

function NewProjAngL(type, posX, posY, angle, velocity, sclL, scl)
{
	angle = angle||0;
	velocity = velocity||0;
	scl = scl||0.5;
	sclL = sclL||1.75;
	Vector2._tmpVel.set(velocity, 0).rotate(angle);
	return NewProjL(type, posX, posY, Vector2._tmpVel.x, Vector2._tmpVel.y, sclL, scl);
}

function DelProj(id)
{
	dm.DelProj(id);
}

function SetProjLifeTime(id, time)
{
	command_pool_integers.push(17, 2, id, Math.floor(time));
}
function KillPlayer()
{
	dm.KillPlayer();
}
function NewProjAng(type, posX, posY, angle, velocity, scl)
{
	angle = angle||0;
	velocity = velocity||0;
	scl = scl||0.5;
	Vector2._tmpVel.set(velocity, 0).rotate(angle);
	return NewProj(type, posX, posY, Vector2._tmpVel.x, Vector2._tmpVel.y, scl);
}

function ShootPlayer(type, posX, posY, veloc, scale, deltaDeg)
{
	veloc = veloc||1;
	scale = scale||0.5;
	deltaDeg = deltaDeg||0;
	command_pool_integers.push(3, available_ids[0], type);
	command_pool_doubles.push(posX, posY, veloc, scale, deltaDeg);
	return available_ids.shift();
}

function ShootPlayerL(type, posX, posY, veloc, sclL, scale, deltaDeg)
{
	veloc = veloc||1;
	scale = scale||0.5;
	deltaDeg = deltaDeg||0;
	sclL = sclL||1.75;
	command_pool_integers.push(4, available_ids[0], type);
	command_pool_doubles.push(posX, posY, veloc, sclL, scale, deltaDeg);
	return available_ids.shift();
}

function SetProjShader(id, r, g, b, a)
{
	command_pool_integers.push(17, 3, id);
	command_pool_doubles.push(r, g, b, a);
}

function NewText(msg)
{
	dm.NewText(msg);
}
function AddText(msg)
{
	dm.AddText(msg);
}

function AddTexture(filename)
{
	return parseInt(dm.AddTexture(filename));
}

function SetSelfTexture(texture_index)
{
	dm.SetSelfTexture(texture_index);
}

function SetProjAI(id, type, arg0, arg1, arg2)
{
	arg0 = arg0 || 0;
	arg1 = arg1 || 0;
	arg2 = arg2 || 0;
	command_pool_integers.push(17, 1, id, type);
	command_pool_doubles.push(arg0, arg1, arg2);
}

function NewProjRandom(type, scl)
{
	scl = scl || 0.5;
	return (dm.NewProj(Math.floor(type), Math.random() * 539, Math.random()*539, Math.random()*3-1.5, Math.random()*3-1.5, scl));
}

function toast(txt)
{
	dm.toast(txt);
}

var POS_X = 0;
var POS_Y = 1;
var VEL_X = 2;
var VEL_Y = 3;
var CENTER_X = 4;
var CENTER_Y = 5;
var ACCEL_X = 6;
var ACCEL_Y = 7;
var VALID = 8;
var PARENT = 9;
var POS = 16;
var VEL = 17;
var CENTER = 18;
var ACCEL = 19;
function ClearProjs()
{
	dm.ClearProjs();
}

function SetProj(id, prop, value1, value2)
{
	value1 = value1 || 0;
	value2 = value2 || 0;
	command_pool_integers.push(16, id, prop);
	command_pool_doubles.push(value1, value2);
}
function GetProj(id, prop)
{
	return (dm.GetProj(id, prop));
}
function GetSelf(prop)
{
	return (dm.GetSelf(prop));
}

var SELF_ACTIVE = 64;
var FIGHTAREA = 66;
function Config(key, value)
{
	dm.Config(key, value);
}

function NewRelative(posXOrID, posY, velocX, velocY)
{
	velocX = velocX || 0;
	velocY = velocY || 0;
	if(!posY) return NewRelative(GetProj(posXOrID, CENTER_X), GetProj(posXOrID, CENTER_Y));
	var rid = NewProj(1, posXOrID, posY, velocX, velocY, 0.001);
	SetProj(rid, VALID, -1);
	SetProjShader(rid, 0, 0, 0, 0);
	return rid;
}

var DIE = 1;
function SetProjEvent(id, type, func)
{
	dm.SetProjEvent(id, type, func);
}

function PlrAng(posX, posY)
{
	return Vector2._tmpVel2.set(posX - GetSelf(CENTER_X), posY - GetSelf(CENTER_Y)).angle();
}